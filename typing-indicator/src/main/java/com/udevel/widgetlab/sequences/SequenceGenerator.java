package com.udevel.widgetlab.sequences;

import android.support.annotation.IntRange;

/**
 * Created by joragu on 6/21/2017.
 */

public interface SequenceGenerator {

    @IntRange(from = 0)
    int nextIndex(@IntRange(from = -1) int currentIndex, int numberOfElements);

}