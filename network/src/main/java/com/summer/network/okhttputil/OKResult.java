package com.summer.network.okhttputil;

/**
 * 返回结果对象<br>
 * Created by xiayundong on 2017/10/20.
 */

public class OKResult<T> {

	/**
	 * 返回的code码
	 */
	public String resultCode;

	public String resultMsg;

	public T data;
}
