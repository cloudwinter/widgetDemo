package com.summer.http.okhttputil;

/**
 * Created by xiayundong on 2017/10/20.
 */

public interface OKCallback<T> {

	void notifyStart();

	void notifyCancel();

	void notifyFailure(int code, String msg);

	void notifySuccess(T data);

	void notifyFinish();

}
