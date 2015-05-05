package com.nullcognition.startconcurrent;// Created by ersin on 05/05/15

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ThreadCreationOverhead {

    public static List<Long> getTimes() {
        List<Long> newList = null;
        Collections.copy(newList, times);
        return newList;
    }

    static final List<Long> times = new LinkedList<>();

    static {
        for (int i = 1; i <= 8; i++) {
            int quadratic = i ^ 2;
            times.add(createThreads(quadratic));
        }
    }

    private static long createThreads(int num) {
        long start = System.nanoTime();
        Thread[] t = new Thread[num];
        for (int i = 0; i < num; i++) {
            t[i] = new Thread();
        }
        return System.nanoTime() - start;
    }
}
