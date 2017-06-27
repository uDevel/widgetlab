package com.udevel.widgetlab.sequences;

import java.util.Random;

/**
 * Created by yombunker on 6/21/2017.
 */

public class RandomSequenceGenerator extends SequenceGenerator {
    private final Random random = new Random();

    @Override
    protected int nextIndex(int currentIndex, int numberOfElements) {
        return random.nextInt(numberOfElements);
    }
}