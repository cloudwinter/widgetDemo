package com.summer.network.code;

/**
 * Created by xiayundong on 2017/10/23.
 */

public class NetCode {

	/**
	 * 0 接口通信成功&&业务处理均成功，返回业务对象resultData
	 */
	public static final int RESULT_CODE_SUCCESS = 0;

	/**
	 * -2 与服务端连接成功，通信不成功，response为空
	 */
	public static final int RESULT_CODE_RESPONSE_NULL = -1;

	/**
	 * -1 与服务端通信成功，但状态码非2xx，错误码统一为-1
	 */
	public static final int RESULT_CODE_NET_ERROR = -2;

	/**
	 * -1 与服务端通信成功，状态码2xx，但是格式不对
	 */
	public static final int RESULT_CODE_JSON_ERROR = -3;

	/**
	 * 网络状态有问题的错误信息
	 */
	public static final String RESULT_CODE_NET_ERROR_MSG = "网络异常，请检查网络";

	/**
	 * -2 与服务端通信成功，response为空
	 */
	public static final String RESULT_CODE_RESPONSE_NULL_MSG = "服务异常，请稍后再试";
	/**
	 * -2 与服务端通信成功，状态码2xx，但是格式不对
	 */
	public static final String RESULT_CODE_JSON_ERROR_MSG = "数据格式错误";

}
