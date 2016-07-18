package com.chrischeng.pageindicator;

import android.support.annotation.IntDef;

@IntDef({CircleScrollStyle.NONE, CircleScrollStyle.SLIDE, CircleScrollStyle.JELLY})
public @interface CircleScrollStyle {
    int NONE = 0;
    int SLIDE = 1;
    int JELLY = 2;
}
