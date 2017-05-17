package com.summer.demo.camera.task;

import android.os.AsyncTask;

import com.summer.demo.core.AppConfig;
import com.summer.demo.utils.TimeUtil;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by xiayundong on 2017/5/16.
 */

public class SavePicTask extends AsyncTask<byte[], String, String> {

	public SavePicTask() {

	}

	@Override
	protected String doInBackground(byte[]... params) {
		String currentTime = TimeUtil.getCurrentFormatTime(null);
		String savePath = AppConfig.APP_SD_PATH + "IMG" + File.separator
				+ currentTime + ".jpg";
		File file = new File(AppConfig.APP_SD_PATH + "IMG" + File.separator);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			FileOutputStream outputStream = new FileOutputStream(savePath);
			outputStream.write(params[0]);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return savePath;
	}
}
