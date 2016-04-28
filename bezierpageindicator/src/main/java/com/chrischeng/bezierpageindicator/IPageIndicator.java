package com.chrischeng.bezierpageindicator;

import android.support.v4.view.ViewPager;

public interface IPageIndicator extends ViewPager.OnPageChangeListener {

    void setCurrentItem(int item);
}
