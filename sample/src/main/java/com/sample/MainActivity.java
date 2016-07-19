package com.sample;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.chrischeng.pageindicator.CirclePageIndicator;
import com.chrischeng.pageindicator.CircleScrollStyle;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final int[] mAds = {R.drawable.ad1, R.drawable.ad2, R.drawable.ad3};

    private CirclePageIndicator mIndicator;
    private int mStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.vp);
        mIndicator = (CirclePageIndicator) findViewById(R.id.cpi);
        final TextView styleTextView = (TextView) findViewById(R.id.tv_style);
        assert viewPager != null;
        assert styleTextView != null;

        DisplayAdapter adapter = new DisplayAdapter(mAds);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        mIndicator.setCount(adapter.getCount());

        mStyle = CircleScrollStyle.JELLY;
        styleTextView.setText(R.string.jelly);

        styleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mStyle) {
                    case CircleScrollStyle.SELECTED:
                        mStyle = CircleScrollStyle.SWIPE;
                        styleTextView.setText(R.string.swipe);
                        break;
                    case CircleScrollStyle.SWIPE:
                        mStyle = CircleScrollStyle.JELLY;
                        styleTextView.setText(R.string.jelly);
                        break;
                    case CircleScrollStyle.JELLY:
                        mStyle = CircleScrollStyle.SELECTED;
                        styleTextView.setText(R.string.selected);
                        break;
                }
                mIndicator.setScrollStyle(mStyle);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);

    }

    @Override
    public void onPageSelected(int position) {
        mIndicator.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mIndicator.onPageScrollStateChanged(state);
    }
}
