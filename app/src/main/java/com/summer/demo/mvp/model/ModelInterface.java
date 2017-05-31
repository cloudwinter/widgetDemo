package com.summer.demo.mvp.model;

/**
 * Created by xiayundong on 2017/5/31.
 */

public interface ModelInterface {

	/**
	 * 下载文件
	 * 
	 * @param fileUrl
	 */
	void download(String fileUrl, ModelServiceImpl.ModelCallBack callBack);

	/**
	 * 暂停下载
	 */
	void stopDown();
}
