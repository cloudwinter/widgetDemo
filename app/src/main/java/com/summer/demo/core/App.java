package com.summer.demo.core;

import android.app.Application;

/**
 * Created by xiayundong on 2017/4/6.
 */

public class App extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		RunningEnviroment.init(this);
		// CrashHandler.getInstance().init();
	}
}
