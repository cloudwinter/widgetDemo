package com.summer.network.okhttputil.notifer;

import android.content.Context;

/**
 * 默认设置messageType为String<br>
 * Created by xiayundong on 2017/10/26.
 */

public abstract class OKResultHandler<DataType>
		extends OKBaseResultHandler<String, DataType> {

	public OKResultHandler(Context context) {
		super(context);
	}

	@Override
	public String getErrorMsg(String msg) {
		return msg;
	}
}
