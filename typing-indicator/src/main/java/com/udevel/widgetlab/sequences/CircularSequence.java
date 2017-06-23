package com.udevel.widgetlab.sequences;

/**
 * Created by yombunker on 6/21/2017.
 */

public class CircularSequence implements SequenceGenerator {
    private int currentIndex = -1;
    private boolean clockWise = true;

    @Override
    public int nextIndex(int numberOfElements) {
        if (clockWise) {
            return calculateClockWiseNumber(numberOfElements);
        } else {
            return calculateAntiClockWiseNumber(numberOfElements);
        }
//        currentIndex += clockWise ? 1 : -1;
//
//        if (currentIndex < 0 || currentIndex >= numberOfElements) {
//            currentIndex = clockWise ? numberOfElements - 1 : 0;
//            clockWise = !clockWise;
//        }
//
//        return currentIndex;
    }

    private int calculateClockWiseNumber(int numberOfElements) {
        currentIndex++;

        if (currentIndex >= numberOfElements) {
            clockWise = false;
            currentIndex = numberOfElements > 1 ? numberOfElements - 2 : 0;
        }

        return currentIndex;
    }

    private int calculateAntiClockWiseNumber(int numberOfElements) {
        currentIndex--;

        if (currentIndex < 0) {
            clockWise = true;
            currentIndex = numberOfElements > 1 ? 1 : 0;
        }

        return currentIndex;
    }
}