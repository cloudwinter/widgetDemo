package com.summer.demo.exception;

import android.content.Context;

/**
 * 说明
 * 1.提供行为日志记录功能
 * 2.崩溃日志提醒功能
 * 使用前必须初始化App,传入的Context最好是Application
 * @author zhangxiuliang
 *
 */
public class ExceptionLogUtil {
	
	public static Context sApp;
	
	public static void initApp(Context app){
		sApp = app;
		Thread.setDefaultUncaughtExceptionHandler(
	            AppException.getInstance(sApp));
	}

}
