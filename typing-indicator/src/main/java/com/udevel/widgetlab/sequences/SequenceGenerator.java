package com.udevel.widgetlab.sequences;

import android.support.annotation.IntRange;
import android.util.Log;

/**
 * Created by yombunker on 6/21/2017.
 */

public abstract class SequenceGenerator {
    private static final String TAG = SequenceGenerator.class.getSimpleName();
    private int currentIndex = -1;

    @IntRange(from = 0)
    public int nextIndex(int numberOfElements) {
        if (numberOfElements < 1) {
            Log.w(TAG, "This sequence generator needs to have at least 1 element to work properly");
            return 0;
        }

        currentIndex = nextIndex(currentIndex, numberOfElements);
        return currentIndex;
    }

    protected abstract int nextIndex(int currentIndex, int numberOfElements);
}