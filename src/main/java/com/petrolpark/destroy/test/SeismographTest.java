package com.petrolpark.destroy.test;

import java.util.Arrays;

import com.petrolpark.destroy.content.oil.seismology.SeismographItem.Seismograph;

public class SeismographTest {
    
    public static void main(String[] args) {
        Seismograph seismograph = new Seismograph();
        byte sequence = 0;
        System.out.println(sequence);
        seismograph.getRows()[0] = sequence;
        System.out.println(Arrays.toString(seismograph.getRowDisplayed(0)));
    };
};
