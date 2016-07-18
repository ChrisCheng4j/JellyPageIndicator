package com.chrischeng.pageindicator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class CirclePageIndicator extends View implements IPageIndicator {

    private static final String KEY_INSTANCE = "instance";
    private static final String KEY_POSITION = "position";

    private Resources mRes;
    private int mOrientation;
    private float mRadius;
    private float mSpacing;
    private float mCenterDistance;
    private int mCount;
    private boolean mSingleShow;
    private int mScrollStyle;
    private float mJellyMinRadius;
    private float mJellyRadiusOffset;

    private Paint mNormalPaint;
    private Paint mSelectedPaint;
    private Path mJellyPath;
    private JellyPoint mStartPoint;
    private JellyPoint mEndPoint;
    private float mStartScrolledOffset;
    private float mEndScrolledOffset;
    private float mRadiusStartOffset;
    private float mRadiusEndOffset;
    private float mJellyScrolledAcceleration;

    private int mCurrentPosition;
    private float mPositionOffset;

    public CirclePageIndicator(Context context) {
        this(context, null);
    }

    public CirclePageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    public void setCurrentItem(int item) {
        mCurrentPosition = item;
        invalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        switch (mScrollStyle) {
            case CircleScrollStyle.SWIPE:
                slideScrolled(position, positionOffset);
                break;
            case CircleScrollStyle.JELLY:
                jellyScrolled(position, positionOffset, position * mCenterDistance + mRadius, -mCenterDistance);
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        mPositionOffset = 0;
        mCurrentPosition = position;
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        invalidate();
    }

    public void setOrientation(int orientation) {
        if (orientation == LinearLayout.HORIZONTAL || orientation == LinearLayout.VERTICAL) {
            mOrientation = orientation;
            requestLayout();
        }
    }

    public void setCount(int count) {
        mCount = count;
        requestLayout();
        invalidate();
    }

    public void setRadius(float radius) {
        mRadius = radius;
        calDistance();
        invalidate();
    }

    public void setSpacing(float spacing) {
        mSpacing = spacing;
        calDistance();
        invalidate();
    }

    public void setNormalColor(int color) {
        mNormalPaint.setColor(color);
        invalidate();
    }

    public void setSelectedColor(int color) {
        mSelectedPaint.setColor(color);
        invalidate();
    }

    public void setSingleShow(boolean isSingleShow) {
        mSingleShow = isSingleShow;
        if (mCount == 1)
            invalidate();
    }

    public void setScrollStyle(@CircleScrollStyle int scrollStyle) {
        mScrollStyle = scrollStyle;
    }

    public void setJellyMinRadius(float jellyMinRadius) {
        mJellyMinRadius = jellyMinRadius;
        calDistance();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOrientation == LinearLayout.HORIZONTAL)
            setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec));
        else
            setMeasuredDimension(measureShort(widthMeasureSpec), measureLong(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mCount <= 0 || (mCount == 1 && !mSingleShow))
            return;

        float longOffset = mRadius;
        float shortOffset = mRadius;

        if (mOrientation == LinearLayout.HORIZONTAL) {
            longOffset += getPaddingLeft();
            shortOffset += getPaddingTop();
        } else {
            longOffset += getPaddingTop();
            shortOffset += getPaddingLeft();
        }

        float cx;
        float cy;

        for (int i = 0; i < mCount; i++) {
            float longStart = longOffset + (mRadius * 2 + mSpacing) * i;

            if (mOrientation == LinearLayout.HORIZONTAL) {
                cx = longStart;
                cy = shortOffset;
            } else {
                cx = shortOffset;
                cy = longStart;
            }

            if (mNormalPaint.getAlpha() > 0)
                canvas.drawCircle(cx, cy, mRadius, mNormalPaint);
        }

        if (mOrientation == LinearLayout.HORIZONTAL) {
            cx = longOffset + (2 * mRadius + mSpacing) * (mCurrentPosition + mPositionOffset);
            cy = shortOffset;
        } else {
            cx = shortOffset;
            cy = longOffset + (2 * mRadius + mSpacing) * (mCurrentPosition + mPositionOffset);
        }

        switch (mScrollStyle) {
            case CircleScrollStyle.SWIPE:
                if (mSelectedPaint.getAlpha() > 0)
                    canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
                break;
            case CircleScrollStyle.JELLY:
                generateJellyPath();
                canvas.drawPath(mJellyPath, mSelectedPaint);
                canvas.drawCircle(mStartPoint.getX(), mStartPoint.getY(), mStartPoint.getRadius(), mSelectedPaint);
                canvas.drawCircle(mEndPoint.getX(), mEndPoint.getY(), mEndPoint.getRadius(), mSelectedPaint);
                break;
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(KEY_POSITION, mCurrentPosition);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentPosition = bundle.getInt(KEY_POSITION);
            super.onRestoreInstanceState(bundle.getParcelable(KEY_INSTANCE));
        } else
            super.onRestoreInstanceState(state);
    }

    private void init(Context context, AttributeSet attrs) {
        mRes = getResources();
        initGeneralStyle();
        initAttrs(context, attrs);
        initJellyStyle();
        calDistance();
    }

    private void initGeneralStyle() {
        mNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNormalPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectedPaint.setStyle(Paint.Style.FILL);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CirclePageIndicator);
        mOrientation = a.getInt(R.styleable.CirclePageIndicator_android_orientation,
                mRes.getInteger(R.integer.default_circle_orientation));
        mRadius = a.getDimension(R.styleable.CirclePageIndicator_pi_circle_radius,
                mRes.getDimension(R.dimen.default_circle_radius));
        mSpacing = a.getDimension(R.styleable.CirclePageIndicator_pi_circle_spacing,
                mRes.getDimension(R.dimen.default_circle_spacing));
        mNormalPaint.setColor(a.getColor(R.styleable.CirclePageIndicator_pi_circle_normalColor,
                mRes.getColor(R.color.default_circle_normal_color)));
        mSelectedPaint.setColor(a.getColor(R.styleable.CirclePageIndicator_pi_circle_selectedColor,
                mRes.getColor(R.color.default_circle_selected_color)));
        Drawable background = a.getDrawable(R.styleable.CirclePageIndicator_android_background);
        if (background != null)
            setBackgroundDrawable(background);
        mCount = a.getInteger(R.styleable.CirclePageIndicator_pi_circle_count,
                mRes.getInteger(R.integer.default_circle_count));
        mSingleShow = a.getBoolean(R.styleable.CirclePageIndicator_pi_circle_single_show,
                mRes.getBoolean(R.bool.default_circle_single_show));
        mScrollStyle = a.getInteger(R.styleable.CirclePageIndicator_pi_scroll_style,
                mRes.getInteger(R.integer.default_circle_scroll_style));
        mJellyMinRadius = a.getDimension(R.styleable.CirclePageIndicator_pi_jelly_radius_min,
                mRes.getDimension(R.dimen.default_circle_jelly_min_radius));

        a.recycle();
    }

    private void initJellyStyle() {
        mStartScrolledOffset = getResources().getFraction(R.fraction.default_circle_jelly_start_offset, 1, 1);
        mEndScrolledOffset = 1 - mStartScrolledOffset;
        mRadiusStartOffset = mRes.getFraction(R.fraction.default_circle_jelly_radius_start_offset, 1, 1);
        mRadiusEndOffset = mRes.getFraction(R.fraction.default_circle_jelly_radius_end_offset, 1, 1);
        mJellyScrolledAcceleration = getResources().getFraction(R.fraction.default_circle_jelly_scrolled_acceleration, 1, 1);

        mJellyPath = new Path();

        mStartPoint = new JellyPoint();
        mEndPoint = new JellyPoint();
        mStartPoint.setX(mRadius);
        mStartPoint.setY(mRadius);
        mEndPoint.setX(mRadius);
        mEndPoint.setY(mRadius);
    }

    private void calDistance() {
        mCenterDistance = mRadius * 2 + mSpacing;
        mJellyRadiusOffset = mRadius - mJellyMinRadius;
    }

    private int measureLong(int measureSpec) {
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY)
            result = specSize;
        else {
            result = (int) (getPaddingLeft() + mRadius * 2 * mCount + mSpacing * (mCount - 1) + getPaddingRight());
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    private int measureShort(int measureSpec) {
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY)
            result = specSize;
        else {
            result = (int) (getPaddingTop() + mRadius * 2 + getPaddingBottom());
            if (specMode == MeasureSpec.AT_MOST)
                result = Math.min(result, specSize);
        }

        return result;
    }

    private void slideScrolled(int position, float positionOffset) {
        mCurrentPosition = position;
        mPositionOffset = positionOffset;
        invalidate();
    }

    private void generateJellyPath() {
        float startOffsetX = (float) (mStartPoint.getRadius() *
                Math.sin(Math.atan((mEndPoint.getY() - mStartPoint.getY()) /
                        (mEndPoint.getX() - mStartPoint.getX()))));
        float startOffsetY = (float) (mStartPoint.getRadius() *
                Math.cos(Math.atan((mEndPoint.getY() - mStartPoint.getY()) /
                        (mEndPoint.getX() - mStartPoint.getX()))));
        float endOffsetX = (float) (mEndPoint.getRadius() *
                Math.sin(Math.atan((mEndPoint.getY() - mStartPoint.getY()) /
                        (mEndPoint.getX() - mStartPoint.getX()))));
        float endOffsetY = (float) (mEndPoint.getRadius() *
                Math.cos(Math.atan((mEndPoint.getY() - mStartPoint.getY()) /
                        (mEndPoint.getX() - mStartPoint.getX()))));

        float x1 = mStartPoint.getX() - startOffsetX;
        float y1 = mStartPoint.getY() + startOffsetY;

        float x2 = mStartPoint.getX() + startOffsetX;
        float y2 = mStartPoint.getY() - startOffsetY;

        float x3 = mEndPoint.getX() - endOffsetX;
        float y3 = mEndPoint.getY() + endOffsetY;

        float x4 = mEndPoint.getX() + endOffsetX;
        float y4 = mEndPoint.getY() - endOffsetY;

        float anchorX = (mEndPoint.getX() + mStartPoint.getX()) / 2;
        float anchorY = (mEndPoint.getY() + mStartPoint.getY()) / 2;

        mJellyPath.reset();
        mJellyPath.moveTo(x1, y1);
        mJellyPath.quadTo(anchorX, anchorY, x3, y3);
        mJellyPath.lineTo(x4, y4);
        mJellyPath.quadTo(anchorX, anchorY, x2, y2);
        mJellyPath.lineTo(x1, y1);
    }

    private void jellyScrolled(int position, float positionOffset, float startX, float dis) {
        if (position < mCount - 1) {
            if (positionOffset < mRadiusStartOffset)
                mStartPoint.setRadius(mJellyMinRadius);
            else
                mStartPoint.setRadius(((positionOffset - mRadiusStartOffset) / (1 - mRadiusStartOffset) * mJellyRadiusOffset + mJellyMinRadius));

            if (positionOffset < mRadiusEndOffset)
                mEndPoint.setRadius((1 - positionOffset / mRadiusEndOffset) * mJellyRadiusOffset + mJellyMinRadius);
            else
                mEndPoint.setRadius(mJellyMinRadius);

            float startFraction = 1f;
            if (positionOffset < mStartScrolledOffset) {
                float positionOffsetTemp = positionOffset / mStartScrolledOffset;
                startFraction = (float) ((Math.atan(positionOffsetTemp * mJellyScrolledAcceleration * 2 - mJellyScrolledAcceleration) + (Math.atan(mJellyScrolledAcceleration))) / (2 * (Math.atan(mJellyScrolledAcceleration))));
            }
            mStartPoint.setX(startX - startFraction * dis);

            float endFraction = 0f;
            if (positionOffset > mEndScrolledOffset) {
                float positionOffsetTemp = (positionOffset - mEndScrolledOffset) / (1 - mEndScrolledOffset);
                endFraction = (float) ((Math.atan(positionOffsetTemp * mJellyScrolledAcceleration * 2 - mJellyScrolledAcceleration) + (Math.atan(mJellyScrolledAcceleration))) / (2 * (Math.atan(mJellyScrolledAcceleration))));
            }
            mEndPoint.setX(startX - endFraction * dis);

            if (positionOffset == 0) {
                mStartPoint.setRadius(mRadius);
                mEndPoint.setRadius(mRadius);
            }
        } else {
            mStartPoint.setX(startX);
            mEndPoint.setX(startX);
            mStartPoint.setRadius(mRadius);
            mEndPoint.setRadius(mRadius);
        }

        invalidate();
    }
}
