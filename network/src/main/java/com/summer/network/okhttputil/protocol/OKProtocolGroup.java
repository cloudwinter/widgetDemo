package com.summer.network.okhttputil.protocol;

import com.summer.network.okhttputil.entity.OKBaseRequestParam;

import java.util.HashMap;

/**
 * 
 * Created by xiayundong on 2017/10/20.
 */

public class OKProtocolGroup {

	/**
	 * 存储请求和返回结果
	 */
	private static HashMap<Class<? extends OKBaseRequestParam>, OKProtocolAction> mProtocolMap = new HashMap<>();

	/**
	 * 添加请求参数类型，请求url，返回类型
	 * 
	 * @param param
	 * @param protocolAction
	 */
	public static void addProtocolAction(
			Class<? extends OKBaseRequestParam> param,
			OKProtocolAction protocolAction) {
		mProtocolMap.put(param, protocolAction);
	}

	/**
	 * 根据请求参数获取对应的url和回调结果
	 * 
	 * @param param
	 * @return
	 */
	public static OKProtocolAction getProtocolAction(
			Class<? extends OKBaseRequestParam> param) {
		if (!mProtocolMap.containsKey(param)) {
			throw new IllegalArgumentException("protocolAction is null");
		}
		return mProtocolMap.get(param);
	}

}
