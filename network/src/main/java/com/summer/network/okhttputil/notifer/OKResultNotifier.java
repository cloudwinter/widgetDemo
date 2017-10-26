package com.summer.network.okhttputil.notifer;

/**
 * Created by xiayundong on 2017/10/20.
 */

public interface OKResultNotifier<T> {

	/**
	 * 客户端异常处理
	 * 
	 * @param code
	 * @param msg
	 */
	void notifyFailure(int code, String msg);

	/**
	 * 通信成功后的回调
	 * 
	 * @param data
	 */
	void notifySuccess(T data);

}
