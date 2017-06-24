package com.udevel.widgetlab.sequences;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by yombunker on 6/23/2017.
 */
public class CircularSequenceTest {
    private SequenceGenerator sequenceGenerator;
    private int numberOfElements;
    private int numberOfSamples;

    @Before
    public void setUp() throws Exception {
        sequenceGenerator = new CircularSequence();
        numberOfElements = 5;
        numberOfSamples = (numberOfElements * 3) - 2;
    }

    @After
    public void tearDown() throws Exception {
        sequenceGenerator = null;
    }

    @Test
    public void nextIndex() throws Exception {
        int[] result = new int[numberOfSamples];
        for (int i = 0; i < numberOfSamples; i++) {
            result[i] = sequenceGenerator.nextIndex(numberOfElements);
        }

        assertThat(result).containsSequence(0, 1, 2, 3, 4, 3, 2, 1, 0, 1, 2, 3, 4);
    }
}