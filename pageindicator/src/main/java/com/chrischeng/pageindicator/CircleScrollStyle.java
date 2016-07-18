package com.chrischeng.pageindicator;

import android.support.annotation.IntDef;

@IntDef({CircleScrollStyle.NONE, CircleScrollStyle.SWIPE, CircleScrollStyle.JELLY})
public @interface CircleScrollStyle {
    int NONE = 0;
    int SWIPE = 1;
    int JELLY = 2;
}
