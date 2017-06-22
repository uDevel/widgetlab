package com.udevel.widgetlab.sequences;

/**
 * Created by joragu on 6/21/2017.
 */

public class RandomNoRepetitionSequence extends RandomSequence {
    @Override
    public int nextIndex(int currentIndex, int numberOfElements) {
        int nextValue;
        do {
            nextValue = super.nextIndex(currentIndex, numberOfElements);
        } while (nextValue == currentIndex);
        return nextValue;
    }
}