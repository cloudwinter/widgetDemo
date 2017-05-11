package com.summer.demo.utils;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by xiayundong on 2017/4/6.
 */

public class FileUtil {

	/**
	 * 保存字符串到指定的文件
	 * 
	 * @param filePathName
	 * @param content
	 */
	public static void writeStringToFile(String filePathName, String content) {
		if (TextUtils.isEmpty(filePathName) || TextUtils.isEmpty(content)) {
			return;
		}
		try {
			File file = new File(filePathName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream outputStream = new FileOutputStream(file, true);
			outputStream.write(content.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取文件
	 * 
	 * @param filePathName
	 * @return
	 */
	public static String readFileToString(String filePathName) {
		if (TextUtils.isEmpty(filePathName)) {
			return null;
		}
		File file = new File(filePathName);
		if (!file.exists()) {
			return null;
		}
		FileInputStream inputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			inputStream = new FileInputStream(file);
			byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] bytes = new byte[1024];
			int length = -1;
			while ((length = inputStream.read(bytes)) != -1) {
				byteArrayOutputStream.write(bytes, 0, length);
			}
			inputStream.close();
			byteArrayOutputStream.close();
			return byteArrayOutputStream.toString();
		} catch (Exception e) {
			try {
				inputStream.close();
				byteArrayOutputStream.close();
			} catch (Exception ex) {

			}
		}
		return null;
	}
}
