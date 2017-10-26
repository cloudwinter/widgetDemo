package com.summer.network.okhttputil.protocol;

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
	 * 消息类型
	 */
	public Type msgType;

	/**
	 * 返回结果类型
	 */
	public Type resultType;

	public OKProtocolAction(String url, Type resultType) {
		this(url, String.class, resultType);
	}

	public OKProtocolAction(String url, Type msgType, Type resultType) {
		this.url = url;
		this.msgType = msgType;
		this.resultType = resultType;
	}
}
