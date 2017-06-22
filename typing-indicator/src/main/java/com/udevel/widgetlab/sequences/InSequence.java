package com.udevel.widgetlab.sequences;

/**
 * Created by joragu on 6/21/2017.
 */

public class InSequence implements SequenceGenerator {
    @Override
    public int nextIndex(int currentIndex, int numberOfElements) {
        currentIndex++;
        return currentIndex % numberOfElements;
    }
}