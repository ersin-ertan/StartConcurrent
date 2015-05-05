package com.nullcognition.startconcurrent;// Created by ersin on 05/05/15

import java.util.ArrayList;

/*
Computes the echo for all linear positions of time in parallel, allowing parameters a and b to recalculate the entire sequence of the input
TODO - cache computation for a and b to eliminate repeats, cache method computation for before echo if input is the same,
TODO - ensure ordering of threads getOutput to match the ordering of the arraylist such that a is for echo and b is for mixing

* */

public class ParallelEcho {

    double a = 1;
    double b = 1;

    int delay = 4;
    double[] in = new double[16];
    double[] out = new double[in.length + delay];
    ArrayList<RunnableEcho> runnableEchos;

    {
        runnableEchos.add(beforeEcho());
        runnableEchos.add(withEcho());
        runnableEchos.add(afterEcho());
    }

    ArrayList<double[]> outputs = new ArrayList<>();

    private class EchoThread extends Thread {
        RunnableEcho r;

        public EchoThread(RunnableEcho runnable) {
            super(runnable);
            r = runnable;
        }

        public double[] getOutput() {
            return r.getOutput();
        }
    }

    private abstract class RunnableEcho implements Runnable {
        public double[] getOutput() {
            return rOut;
        }

        double[] rOut = new double[in.length + delay];
    }


    public void computeOut() {
        int tasks = runnableEchos.size();
        EchoThread[] threads = new EchoThread[tasks];
        for (int i = 0; i < tasks; i++) {
            threads[i] = new EchoThread(runnableEchos.get(i));
        }
        for (int i = 0; i < tasks; i++) {
            threads[i].start();
        }
        for (int i = 0; i < tasks; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < tasks; i++) {
            outputs.add(threads[i].getOutput());
        }
    }

    public RunnableEcho beforeEcho() {
        return new RunnableEcho() {

            @Override
            public void run() {
                for (int i = 0; i < delay; i++) {
                    rOut[i] = in[i];
                }
            }
        };
    }

    public RunnableEcho withEcho() {
        return new RunnableEcho() {
            @Override
            public void run() {
                for (int i = delay; i < in.length; i++) {
                    rOut[i] = a * in[i] + b * in[i - delay];
                }
            }
        };
    }

    private RunnableEcho afterEcho() {
        return new RunnableEcho() {
            @Override
            public void run() {
                for (int i = in.length; i < out.length; i++) {
                    rOut[i] = b * in[i - delay];
                }

            }
        };
    }
}
