package com.summer.network.okhttputil;

/**
 * Created by xiayundong on 2017/10/20.
 */

public interface OKCallback<T> {

	void notifyFailure(int code, String msg);

	void notifySuccess(T data);


}
