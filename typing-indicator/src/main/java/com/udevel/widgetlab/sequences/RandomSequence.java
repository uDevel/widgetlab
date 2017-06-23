package com.udevel.widgetlab.sequences;

import java.util.Random;

/**
 * Created by joragu on 6/21/2017.
 */

public class RandomSequence implements SequenceGenerator {
    private Random random = new Random();

    @Override
    public int nextIndex(int numberOfElements) {
        return random.nextInt(numberOfElements);
    }
}