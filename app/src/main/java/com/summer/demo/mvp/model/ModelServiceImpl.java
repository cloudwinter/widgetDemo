package com.summer.demo.mvp.model;

import android.os.Handler;

/**
 * Created by xiayundong on 2017/5/31.
 */

public class ModelServiceImpl implements ModelInterface {

	private Handler mHandler;

	private int downloadPosition = 0;

	/**
	 * 是否下载
	 */
	private boolean mDownload = true;

	public ModelServiceImpl() {
		mHandler = new Handler();
	}

	@Override
	public void download(String fileUrl, final ModelCallBack callBack) {
		callBack.start();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (downloadPosition == 100) {
					callBack.downSuccess();
					return;
				}
				if (mDownload) {
					mHandler.postDelayed(this, 100);
					downloadPosition++;
					callBack.download(downloadPosition);
				} else {
					callBack.downError("暂停下载");
				}
			}
		}, 100);
	}

	@Override
	public void stopDown() {
		mDownload = false;
	}

	public interface ModelCallBack {

		void start();

		void download(int position);

		void downSuccess();

		void downError(String msg);
	}
}
