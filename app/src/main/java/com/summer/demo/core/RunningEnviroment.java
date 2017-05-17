package com.summer.demo.core;

import android.app.Application;
import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xiayundong on 2017/4/6.
 */

public class RunningEnviroment {

	/**
	 * 全局上下文
	 */
	public static Context sContext;

	public static ExecutorService sExecutor = Executors
			.newFixedThreadPool(10);

	public static void init(Application app) {
		if (app != null) {
			sContext = app.getApplicationContext();
		}
	}
}
