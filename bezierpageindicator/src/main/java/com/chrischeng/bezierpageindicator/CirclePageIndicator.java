package com.chrischeng.bezierpageindicator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class CirclePageIndicator extends View implements IPageIndicator {

    private int mOrientation;
    private float mRadius;
    private float mSpacing;
    private int mCount;

    private Paint mNormalPaint;
    private Paint mSelectedPaint;
    private Paint mStrokePaint;

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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mCurrentPosition = position;
        mPositionOffset = positionOffset;
        postInvalidate();
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;
        postInvalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        postInvalidate();
    }

    public void setOrientation(int orientation) {
        if (orientation == LinearLayout.HORIZONTAL || orientation == LinearLayout.VERTICAL) {
            mOrientation = orientation;
            requestLayout();
        }
    }

    public void setRadius(float radius) {
        mRadius = radius;
        invalidate();
    }

    public void setSpacing(float spacing) {
        mSpacing = spacing;
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

    public void setStrokeColor(int color) {
        mStrokePaint.setColor(color);
        invalidate();
    }

    public void setStrokeWidth(float strokeWidth) {
        mStrokePaint.setStrokeWidth(strokeWidth);
        invalidate();
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

        if (mCount <= 0)
            return;

        int longSideLength;
        int longSidePaddingBefore;
        int longSidePaddingAfter;
        int shortSidePaddingBefore;

        if (mOrientation == LinearLayout.HORIZONTAL) {
            longSideLength = getWidth();
            longSidePaddingBefore = getPaddingLeft();
            longSidePaddingAfter = getPaddingRight();
            shortSidePaddingBefore = getPaddingTop();
        } else {
            longSideLength = getHeight();
            longSidePaddingBefore = getPaddingTop();
            longSidePaddingAfter = getPaddingBottom();
            shortSidePaddingBefore = getPaddingLeft();
        }

        float longOffset = longSidePaddingBefore + mRadius;
        float shortOffset = shortSidePaddingBefore + mRadius;

        float normalRadius = mRadius;
        normalRadius -= mStrokePaint.getStrokeWidth() > 0 ? mStrokePaint.getStrokeWidth() / 2.0f : 0;

        float cx;
        float cy;

        for (int i = 0; i < mCount; i++) {
            float longStart = longOffset + mRadius * 2 + mSpacing;

            if (mOrientation == LinearLayout.HORIZONTAL) {
                cx = longStart;
                cy = shortOffset;
            } else {
                cx = shortOffset;
                cy = longStart;
            }

            if (mNormalPaint.getAlpha() > 0)
                canvas.drawCircle(cx, cy, normalRadius, mSelectedPaint);

            if (normalRadius != mRadius)
                canvas.drawCircle(cx, cy, mRadius, mStrokePaint);
        }

        if (mOrientation == LinearLayout.HORIZONTAL) {
            cx = longOffset + (2 * mRadius + mSpacing) * mCurrentPosition + mPositionOffset;
            cy = shortOffset;
        } else {
            cx = shortOffset;
            cy = longOffset + (2 * mRadius + mSpacing) * mCurrentPosition + mPositionOffset;
        }

        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
    }

    private void init(Context context, AttributeSet attrs) {
        initPaint();
        initAttrs(context, attrs);
    }

    private void initPaint() {
        mNormalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNormalPaint.setStyle(Paint.Style.FILL);

        mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectedPaint.setStyle(Paint.Style.FILL);

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStyle(Paint.Style.STROKE);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        Resources res = context.getResources();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CirclePageIndicator);
        mOrientation = a.getInt(R.styleable.CirclePageIndicator_android_orientation,
                res.getInteger(R.integer.default_circle_orientation));
        mRadius = a.getDimension(R.styleable.CirclePageIndicator_radius,
                res.getDimension(R.dimen.default_circle_radius));
        mSpacing = a.getDimension(R.styleable.CirclePageIndicator_spacing,
                res.getDimension(R.dimen.default_circle_spacing));
        mNormalPaint.setColor(a.getColor(R.styleable.CirclePageIndicator_normalColor,
                res.getColor(R.color.default_circle_normal_color)));
        mSelectedPaint.setColor(a.getColor(R.styleable.CirclePageIndicator_selectedColor,
                res.getColor(R.color.default_circle_selected_color)));
        mStrokePaint.setColor(a.getColor(R.styleable.CirclePageIndicator_strokeColor,
                res.getColor(R.color.default_circle_stroke_color)));
        mStrokePaint.setStrokeWidth(a.getDimension(R.styleable.CirclePageIndicator_strokeWidth,
                res.getDimension(R.dimen.default_circle_stroke_width)));
        Drawable background = a.getDrawable(R.styleable.CirclePageIndicator_android_background);
        if (background != null)
            setBackgroundDrawable(background);
        mCount = a.getInteger(R.styleable.CirclePageIndicator_count,
                res.getInteger(R.integer.default_circle_count));

        a.recycle();
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
}
