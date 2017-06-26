package com.udevel.widgetlab.sequences;

import android.util.Log;

/**
 * Created by yombunker on 6/21/2017.
 */

public class RandomNoRepetitionSequenceGenerator extends RandomSequenceGenerator {
    private static final String TAG = RandomNoRepetitionSequenceGenerator.class.getSimpleName();
    private int currentIndex = -1;

    @Override
    public int nextIndex(int numberOfElements) {
        if (numberOfElements < 2) {
            Log.w(TAG, "This sequence generator needs to have at least 2 elements to work properly");
            return 0;
        }

        int nextValue = super.nextIndex(numberOfElements - 1);
        currentIndex = nextValue < currentIndex ? nextValue : nextValue + 1;
        return currentIndex;
    }
}