package com.summer.network.okhttputil.notifer;

import android.content.Context;

import com.summer.network.okhttputil.entity.OKMessage;

/**
 * 设置messageType为OKMessage<br>
 * Created by xiayundong on 2017/10/26.
 */

public abstract class OKResultTypeHandler<DataType>
		extends OKBaseResultHandler<OKMessage, DataType> {

	public OKResultTypeHandler(Context context) {
		super(context);
	}

	@Override
	public OKMessage getErrorMsg(String msg) {
		OKMessage okMessage = new OKMessage();
		okMessage.msg = msg;
		return okMessage;
	}
}
