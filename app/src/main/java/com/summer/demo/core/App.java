package com.summer.demo.core;

import android.app.Application;

import com.summer.demo.exception.ExceptionLogUtil;

/**
 * Created by xiayundong on 2017/4/6.
 */

public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		ExceptionLogUtil.initApp(this);
		RunningEnviroment.init(this);
	}
}
