package com.udevel.widgetlab.sequences;

/**
 * Created by yombunker on 6/21/2017.
 */

public class CircularSequenceGenerator extends SequenceGenerator {
    private boolean clockWise = true;

    @Override
    protected int nextIndex(int currentIndex, int numberOfElements) {
        if (clockWise) {
            return calculateClockWiseNumber(currentIndex, numberOfElements);
        } else {
            return calculateAntiClockWiseNumber(currentIndex, numberOfElements);
        }
    }

    private int calculateClockWiseNumber(int currentIndex, int numberOfElements) {
        currentIndex++;

        if (currentIndex >= numberOfElements) {
            clockWise = false;
            currentIndex = numberOfElements > 1 ? numberOfElements - 2 : 0;
        }

        return currentIndex;
    }

    private int calculateAntiClockWiseNumber(int currentIndex, int numberOfElements) {
        currentIndex--;

        if (currentIndex < 0) {
            clockWise = true;
            currentIndex = numberOfElements > 1 ? 1 : 0;
        }

        return currentIndex;
    }
}