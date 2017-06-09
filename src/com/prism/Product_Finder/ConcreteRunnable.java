package com.prism.Product_Finder;

import java.util.Random;

public class ConcreteRunnable implements Runnable {

    int seq;
    public ConcreteRunnable(int seq) {
        this.seq = seq;
    }
    public void run() {
        System.out.println(this.seq+" thread start.");
        try {
            Thread.sleep(1000);
        }catch(Exception e) {
        }
        System.out.println(this.seq+" thread end.");
    }
}