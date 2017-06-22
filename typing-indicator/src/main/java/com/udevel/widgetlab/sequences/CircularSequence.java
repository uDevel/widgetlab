package com.udevel.widgetlab.sequences;

/**
 * Created by joragu on 6/21/2017.
 */

public class CircularSequence implements SequenceGenerator {
    private boolean clockWise = true;

    @Override
    public int nextIndex(int currentIndex, int numberOfElements) {
        currentIndex += clockWise ? 1 : -1;

        if (currentIndex < 0 || currentIndex >= numberOfElements) {
            currentIndex = clockWise ? numberOfElements - 1 : 0;
            clockWise = !clockWise;
        }

        return currentIndex;
    }
}