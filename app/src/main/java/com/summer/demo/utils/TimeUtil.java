package com.summer.demo.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xiayundong on 2017/4/6.
 */

public class TimeUtil {

	/**
	 * 获取当前的时间字符串<br>
	 * 
	 * @param pattern
	 * @return
	 */
	public static String getCurrentFormatTime(String pattern) {
		if (TextUtils.isEmpty(pattern)) {
			pattern = "yyyy-MM-dd_HH-mm-ss";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date data = new Date();
		return sdf.format(data);
	}
}
