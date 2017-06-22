package com.udevel.widgetlab.definitions;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.udevel.widgetlab.definitions.Order.CIRCULAR;
import static com.udevel.widgetlab.definitions.Order.LAST_FIRST;
import static com.udevel.widgetlab.definitions.Order.RANDOM;
import static com.udevel.widgetlab.definitions.Order.RANDOM_NO_REPETITION;
import static com.udevel.widgetlab.definitions.Order.SEQUENCE;

/**
 * Created by joragu on 6/21/2017.
 */

@IntDef(value = {RANDOM, RANDOM_NO_REPETITION, SEQUENCE, CIRCULAR, LAST_FIRST})
@Retention(RetentionPolicy.SOURCE)
public @interface Order {
    int RANDOM = 0;
    int SEQUENCE = 1;
    int CIRCULAR = 2;
    int LAST_FIRST = 3;
    int RANDOM_NO_REPETITION = 4;
}