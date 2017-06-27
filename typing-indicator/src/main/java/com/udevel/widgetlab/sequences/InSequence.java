package com.udevel.widgetlab.sequences;

/**
 * Created by yombunker on 6/21/2017.
 */

public class InSequence implements SequenceGenerator {
    private int currentIndex = -1;

    @Override
    public int nextIndex(int numberOfElements) {
        currentIndex++;
        currentIndex = currentIndex < numberOfElements ? currentIndex : 0;
        return currentIndex;
    }
}