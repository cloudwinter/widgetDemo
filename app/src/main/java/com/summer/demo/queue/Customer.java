package com.summer.demo.queue;

import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiayundong on 2017/9/18.
 */

public class Customer implements Runnable {

	private LinkedBlockingDeque<String> mQueue;

	private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;

	public Customer(LinkedBlockingDeque<String> queue) {
		mQueue = queue;
	}

	@Override
	public void run() {
		System.out.println("启动消费者线程！");
		Random random = new Random();
		boolean isRunning = true;
		try {
			while (isRunning) {
				System.out.println("正从队列获取数据...");
				String data = mQueue.poll(2, TimeUnit.SECONDS);
				if (null != data) {
					System.out.println("拿到数据：" + data);
					System.out.println("正在消费数据：" + data);
					Thread.sleep(random.nextInt(DEFAULT_RANGE_FOR_SLEEP));
				} else {
					// 超过2s还没数据，认为所有生产线程都已经退出，自动退出消费线程。
					isRunning = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} finally {
			System.out.println("退出消费者线程！");
		}
	}
}
