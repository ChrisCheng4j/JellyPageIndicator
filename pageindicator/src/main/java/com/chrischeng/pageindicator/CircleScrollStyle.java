package com.chrischeng.pageindicator;

import android.support.annotation.IntDef;

@IntDef({CircleScrollStyle.SELECTED, CircleScrollStyle.SWIPE, CircleScrollStyle.JELLY})
public @interface CircleScrollStyle {
    int SELECTED = 0;
    int SWIPE = 1;
    int JELLY = 2;
}
