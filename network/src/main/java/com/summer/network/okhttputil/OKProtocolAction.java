package com.summer.network.okhttputil;

import java.lang.reflect.Type;

/**
 * Created by xiayundong on 2017/10/20.
 */

public class OKProtocolAction {

	/**
	 * 请求地址
	 */
	public String url;

	/**
	 * 返回结果类型
	 */
	public Type resultType;

	public OKProtocolAction(String url, Type resultType) {
		this.url = url;
		this.resultType = resultType;
	}
}
