package com.summer.network.okhttputil;

import okhttp3.OkHttpClient;

/**
 * 子类继承使用<br>
 * Created by xiayundong on 2017/10/20.
 */

public class OKNetModel {

	protected OkHttpClient mOKHttpClient = null;

	public OKNetModel() {
		if (mOKHttpClient == null) {
			mOKHttpClient = new OkHttpClient();
		}
	}

}
