package com.udevel.widgetlab.sequences;

/**
 * Created by yombunker on 6/21/2017.
 */

public class ReverseSequenceGenerator extends SequenceGenerator {
    @Override
    protected int nextIndex(int currentIndex, int numberOfElements) {
        currentIndex--;
        currentIndex = (currentIndex < 0) ? numberOfElements - 1 : currentIndex;
        return currentIndex;
    }
}