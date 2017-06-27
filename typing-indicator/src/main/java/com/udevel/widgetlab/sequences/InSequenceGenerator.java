package com.udevel.widgetlab.sequences;

/**
 * Created by yombunker on 6/21/2017.
 */

public class InSequenceGenerator extends SequenceGenerator {
    @Override
    protected int nextIndex(int currentIndex, int numberOfElements) {
        currentIndex++;
        currentIndex = currentIndex < numberOfElements ? currentIndex : 0;
        return currentIndex;
    }
}