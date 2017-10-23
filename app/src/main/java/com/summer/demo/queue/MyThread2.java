package com.summer.demo.queue;

/**
 * Created by xiayundong on 2017/9/22.
 */

public class MyThread2 extends Thread {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"2 is running");
    }
}
