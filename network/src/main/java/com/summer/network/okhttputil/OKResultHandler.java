package com.summer.network.okhttputil;

/**
 * 主线程回调结果<br>
 * 
 * Created by xiayundong on 2017/10/20.
 */

public abstract class OKResultHandler<T> implements OKCallback<T> {

	/**
	 * handler处理ui线程中回调 开始
	 *
	 * @see OKCallback#notifyStart()
	 */
	public abstract void onStart();

	/**
	 * handler处理ui线程中回调 失败
	 *
	 * @see OKCallback#notifyFailure(int, String)
	 * @param code
	 * @param msg
	 */
	public abstract void onFailure(int code, String msg);

	/**
	 * handler处理ui线程中回调 成功
	 *
	 * @see OKCallback#notifySuccess(Object)
	 * @param data
	 */
	public abstract void onSuccess(T data);

	/**
	 * handler处理ui线程中回调 结束
	 *
	 * @see OKCallback#notifyFinish()
	 */
	public abstract void onFinish();


	public OKResultHandler() {

	}



	@Override
	public void notifyStart() {

	}

	@Override
	public void notifyCancel() {

	}

	@Override
	public void notifyFailure(int code, String msg) {

	}

	@Override
	public void notifySuccess(T data) {

	}

	@Override
	public void notifyFinish() {

	}
}
