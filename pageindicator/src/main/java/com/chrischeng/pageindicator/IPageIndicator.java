package com.chrischeng.pageindicator;

import android.support.v4.view.ViewPager;

public interface IPageIndicator extends ViewPager.OnPageChangeListener {
    void setCurrentItem(int item);
}
