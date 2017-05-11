package com.summer.demo.core;

import android.os.Looper;
import android.text.TextUtils;

import com.summer.demo.utils.FileUtil;
import com.summer.demo.utils.TimeUtil;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 处理未捕获的异常<br>
 * Created by xiayundong on 2017/4/6.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

	private static CrashHandler sInstance;
	private Thread.UncaughtExceptionHandler mOlderHandler;

	private CrashHandler() {

	}

	public static CrashHandler getInstance() {
		if (sInstance == null) {
			sInstance = new CrashHandler();
		}
		return sInstance;
	}

	public void init() {
		mOlderHandler = Thread.currentThread().getUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread t, final Throwable e) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				handleException(e);
				Looper.loop();
			}
		}).start();
	}

	/**
	 * 处理异常
	 * 
	 * @param e
	 */
	public void handleException(Throwable ex) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		if (ex != null) {
			ex.printStackTrace(pw);
		}
		String error = sw.toString();
		if (TextUtils.isEmpty(error)) {
			return;
		}
		File file = new File(AppConfig.APP_CRASH_LOG_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		String fileName = AppConfig.APP_CRASH_LOG_PATH + "exception-"
				+ TimeUtil.getCurrentFormatTime(null);
		FileUtil.writeStringToFile(fileName, error);
	}
}
