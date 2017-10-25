package com.summer.network.okhttputil;

/**
 * 子类继承使用<br>
 * Created by xiayundong on 2017/10/20.
 */

public class OKNetModel {

	protected OKNetClient mOKNetClient = null;

	public OKNetModel() {
		if (mOKNetClient == null) {
			mOKNetClient = new OKNetClient();
		}
	}

}
