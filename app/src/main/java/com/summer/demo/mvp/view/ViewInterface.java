package com.summer.demo.mvp.view;

/**
 * Created by xiayundong on 2017/5/31.
 */

public interface ViewInterface {

	/**
	 * 开始下载
	 */
	void startDown();

	/**
	 * 下载进度
	 * 
	 * @param position
	 */
	void showProgress(int position);

	/**
	 * 下载完成
	 */
	void downCompleted();

    /**
     * 下载异常
     * @param error
     */
    void downError(String error);
}
