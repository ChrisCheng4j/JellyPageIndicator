package com.sample;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.chrischeng.pageindicator.CirclePageIndicator;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final int[] ads = {R.drawable.ad1, R.drawable.ad2, R.drawable.ad3};

    private CirclePageIndicator selectedIndicator;
    private CirclePageIndicator swipeIndicator;
    private CirclePageIndicator jellyIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager selectedViewPager = (ViewPager) findViewById(R.id.vp_selected);
        ViewPager swipeViewPager = (ViewPager) findViewById(R.id.vp_swipe);
        ViewPager jellyViewPager = (ViewPager) findViewById(R.id.vp_jelly);
        selectedIndicator = (CirclePageIndicator) findViewById(R.id.cpi_selected);
        swipeIndicator = (CirclePageIndicator) findViewById(R.id.cpi_swipe);
        jellyIndicator = (CirclePageIndicator) findViewById(R.id.cpi_jelly);

        assert selectedViewPager != null;
        assert swipeViewPager != null;
        assert jellyViewPager != null;

        DisplayAdapter adapter = new DisplayAdapter(ads);

        selectedViewPager.setAdapter(adapter);
        swipeViewPager.setAdapter(adapter);
        jellyViewPager.setAdapter(adapter);

        selectedViewPager.addOnPageChangeListener(this);
        swipeViewPager.addOnPageChangeListener(this);
        jellyViewPager.addOnPageChangeListener(this);

        selectedIndicator.setCount(adapter.getCount());
        swipeIndicator.setCount(adapter.getCount());
        jellyIndicator.setCount(adapter.getCount());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        selectedIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
        swipeIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
        jellyIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);

    }

    @Override
    public void onPageSelected(int position) {
        selectedIndicator.onPageSelected(position);
        swipeIndicator.onPageSelected(position);
        jellyIndicator.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        selectedIndicator.onPageScrollStateChanged(state);
        swipeIndicator.onPageScrollStateChanged(state);
        jellyIndicator.onPageScrollStateChanged(state);
    }
}
