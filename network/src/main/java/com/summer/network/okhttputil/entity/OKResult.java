package com.summer.network.okhttputil.entity;

/**
 * 返回结果对象<br>
 * Created by xiayundong on 2017/10/20.
 */

public class OKResult<MessageType, DataType> {

	/**
	 * 解析的json节点名称
	 */
	public transient static final String JSON_CODE = "resultCode";
	public transient static final String JSON_MSG = "resultMsg";
	public transient static final String JSON_DATA = "data";

	/**
	 * 返回的code码
	 */
	public int resultCode;

	public MessageType resultMsg;

	public DataType data;
}
