package com.udevel.widgetlab.definitions;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.udevel.widgetlab.definitions.AnimationType.BOUNCING_SLIDING;
import static com.udevel.widgetlab.definitions.AnimationType.DISAPPEAR;
import static com.udevel.widgetlab.definitions.AnimationType.GROW;
import static com.udevel.widgetlab.definitions.AnimationType.SLIDING;
import static com.udevel.widgetlab.definitions.AnimationType.WINK;


/**
 * Created by joragu on 6/21/2017.
 */

@IntDef(value = {GROW, WINK, DISAPPEAR, SLIDING, BOUNCING_SLIDING})
@Retention(RetentionPolicy.SOURCE)
public @interface AnimationType {
    int GROW = 0;
    int WINK = 1;
    int DISAPPEAR = 2;
    int SLIDING = 3;
    int BOUNCING_SLIDING = 4;
}