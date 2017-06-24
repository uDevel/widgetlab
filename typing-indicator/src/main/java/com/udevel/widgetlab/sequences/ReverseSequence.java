package com.udevel.widgetlab.sequences;

/**
 * Created by yombunker on 6/21/2017.
 */

public class ReverseSequence implements SequenceGenerator {
    private int currentIndex = -1;

    @Override
    public int nextIndex(int numberOfElements) {
        currentIndex--;
        currentIndex = (currentIndex < 0) ? numberOfElements - 1 : currentIndex;
        return currentIndex;
    }
}