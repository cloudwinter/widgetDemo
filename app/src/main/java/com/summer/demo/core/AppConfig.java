package com.summer.demo.core;

import android.os.Environment;

import java.io.File;

/**
 * Created by xiayundong on 2017/4/6.
 */

public class AppConfig {

	/**
	 * sd卡的根目录(/mnt/sdcard/)
	 */
	public static final String SD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + File.separator;

	/**
	 * 获取APP的sd卡中cache目录（/mnt/sdcard/Android/data/<application package>/cache/）
	 */
	public static final String APP_SD_CACHE_PATH = RunningEnviroment.sContext
			.getExternalCacheDir().getAbsolutePath() + File.separator;

	/**
	 * 获取APP的cache目录（/data/data/<application package>/cache/）
	 */
	public static final String APP_CACHE_PATH = RunningEnviroment.sContext
			.getCacheDir().getAbsolutePath() + File.separator;

	/**
	 * 获取app的file目录
	 */
	public static final String APP_FILE_PATH = RunningEnviroment.sContext
			.getFilesDir().getAbsolutePath() + File.separator;

	/**
	 * 当前应用在sd下的根目录
	 */
	public static final String APP_SD_PATH = SD_PATH + "widgetdemo"
			+ File.separator;

	/**
	 * crash的log日志目录
	 */
	public static final String APP_CRASH_LOG_PATH = APP_SD_PATH + "log"
			+ File.separator;
}
