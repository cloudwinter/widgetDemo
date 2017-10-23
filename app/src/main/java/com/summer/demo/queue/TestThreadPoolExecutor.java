package com.summer.demo.queue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xiayundong on 2017/9/22.
 */

public class TestThreadPoolExecutor {

	public static void main(String[] args) {
		// 创建一个可重用固定线程数的线程池
		ExecutorService pool = Executors.newFixedThreadPool(2);
		MyThread1 thread1 = new MyThread1();
		MyThread2 thread2 = new MyThread2();
		MyThread3 thread3 = new MyThread3();
		MyThread4 thread4 = new MyThread4();
		pool.execute(thread1);
		pool.execute(thread2);
		pool.execute(thread3);
		pool.execute(thread4);
		pool.shutdown();
	}

}
