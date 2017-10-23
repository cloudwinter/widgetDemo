package com.summer.demo.queue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 用生产者和消费者实例来描述BlockingQueue的阻塞功能<br>
 * Created by xiayundong on 2017/9/5.
 */

public class TestBlockingQueue {

	private static final String TAG = "TestBlockingQueue";

	public static void main(String[] args) throws InterruptedException {

		LinkedBlockingDeque<String> blockingDeque = new LinkedBlockingDeque<>(
				10);
		Producer producer1 = new Producer(blockingDeque);
		Producer producer2 = new Producer(blockingDeque);
		Producer producer3 = new Producer(blockingDeque);
		Customer consumer = new Customer(blockingDeque);

		// 借助Executors
		ExecutorService service = Executors.newCachedThreadPool();
		// 启动线程
		service.execute(producer1);
		service.execute(producer2);
		service.execute(producer3);
		service.execute(consumer);

		// 执行10s
		Thread.sleep(10 * 1000);
		producer1.stop();
		producer2.stop();
		producer3.stop();

		Thread.sleep(2000);
		// 退出Executor
		service.shutdown();
	}
}
