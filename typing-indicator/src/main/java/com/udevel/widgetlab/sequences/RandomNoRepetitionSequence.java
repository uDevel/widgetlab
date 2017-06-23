package com.udevel.widgetlab.sequences;

/**
 * Created by joragu on 6/21/2017.
 */

public class RandomNoRepetitionSequence extends RandomSequence {
    private static final int MAX_NUMBER_OF_TRIES = 5;

    private int currentIndex = -1;

    @Override
    public int nextIndex(int numberOfElements) {
        int nextValue, numberOfTries = 0;
        do {
            nextValue = super.nextIndex(numberOfElements);
            numberOfTries++;
        } while (nextValue == currentIndex && numberOfTries < MAX_NUMBER_OF_TRIES);

        currentIndex = nextValue;
        return nextValue;
    }
}