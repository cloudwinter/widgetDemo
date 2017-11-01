package com.summer.demo.http.param;

import com.summer.network.okhttputil.entity.OKBaseRequestParam;

/**
 * author mowei date 13/12/2016.
 */

public class ADParam extends OKBaseRequestParam {

	/**
	 * 版本号
	 */
	public String version = "2.0";
	/**
	 * 设备ID
	 */
	public String deviceId = "865863026059928";
	/**
	 * 客户端类型
	 */
	public String clientName = "android";

	/**
	 * 客户端版本号
	 */
	public String clientVersion = "4.2.0";

	/**
	 * token，jdjr=金融token，wallet=钱包auth
	 */
	public String token = "111";

	/**
	 * 账号，jdjr=jdPin，wallet=customerId
	 */
	public String account = "piaoxue16";

	/**
	 * 渠道（钱包wallet、金融jdjr）
	 */
	public String platformChannel = "jdjr";

	/**
	 * 城市编号
	 */
	public String city;

}
