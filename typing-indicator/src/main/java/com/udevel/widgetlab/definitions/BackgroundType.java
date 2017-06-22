package com.udevel.widgetlab.definitions;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.udevel.widgetlab.definitions.BackgroundType.ROUNDED;
import static com.udevel.widgetlab.definitions.BackgroundType.SQUARE;

/**
 * Created by joragu on 6/21/2017.
 */

@IntDef(value = {ROUNDED, SQUARE})
@Retention(RetentionPolicy.SOURCE)
public @interface BackgroundType {
    int ROUNDED = 1;
    int SQUARE = 0;
}