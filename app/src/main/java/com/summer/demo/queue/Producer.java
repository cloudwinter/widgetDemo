package com.summer.demo.queue;

import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xiayundong on 2017/9/18.
 */

public class Producer implements Runnable {

	private static final int DEFAULT_RANGE_FOR_SLEEP = 1000;

	private LinkedBlockingDeque<String> mQueue;

	private boolean mIsRunning = true;

	private static AtomicInteger mCount = new AtomicInteger();

	public Producer(LinkedBlockingDeque<String> queue) {
		mQueue = queue;
	}

	@Override
	public void run() {
		String date;
		Random random = new Random();
		try {
			while (mIsRunning) {
				System.out.println("正在生产数据......");
				Thread.sleep(random.nextInt(DEFAULT_RANGE_FOR_SLEEP));
				date = "date:" + mCount.incrementAndGet();
				if (!mQueue.offer(date, 2, TimeUnit.SECONDS)) {
					System.out.println("放入数据失败：" + date);
				} else {
					System.out.println("将数据：" + date + "放入队列....");
				}
			}
		} catch (Exception e) {
			System.out.println("生产者异常：" + e.getMessage());
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} finally {
			System.out.println("退出生产者消费！");
		}
	}

	public void stop() {
		mIsRunning = false;
	}
}
