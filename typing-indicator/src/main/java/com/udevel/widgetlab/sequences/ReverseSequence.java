package com.udevel.widgetlab.sequences;

/**
 * Created by joragu on 6/21/2017.
 */

public class ReverseSequence implements SequenceGenerator {
    @Override
    public int nextIndex(int currentIndex, int numberOfElements) {
        currentIndex--;
        return (currentIndex < 0) ? numberOfElements - 1 : currentIndex;
    }
}