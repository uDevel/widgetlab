package com.udevel.widgetlab.sequences;

import android.support.annotation.IntRange;

/**
 * Created by yombunker on 6/21/2017.
 */

public interface SequenceGenerator {

    @IntRange(from = 0)
    int nextIndex(int numberOfElements);

}