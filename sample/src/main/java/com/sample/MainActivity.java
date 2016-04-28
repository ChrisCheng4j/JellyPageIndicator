package com.sample;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.chrischeng.bezierpageindicator.CirclePageIndicator;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final int[] ads = {R.drawable.ad1, R.drawable.ad2, R.drawable.ad3};

    private CirclePageIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.vp);
        indicator = (CirclePageIndicator) findViewById(R.id.pi);

        if (viewPager != null) {
            DisplayAdapter adapter = new DisplayAdapter(ads);
            viewPager.setAdapter(adapter);
            indicator.setCount(adapter.getCount());
            viewPager.addOnPageChangeListener(this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        indicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        indicator.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        indicator.onPageScrollStateChanged(state);
    }
}
