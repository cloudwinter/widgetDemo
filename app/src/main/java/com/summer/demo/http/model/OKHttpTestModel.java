package com.summer.demo.http.model;

import com.summer.demo.http.entity.ADListInfo;
import com.summer.demo.http.param.ADParam;
import com.summer.network.okhttputil.OKNetClient;
import com.summer.network.okhttputil.OKNetModel;
import com.summer.network.okhttputil.notifer.OKResultTypeHandler;

/**
 * Created by xiayundong on 2017/10/25.
 */

public class OKHttpTestModel extends OKNetModel {

	static {
		OKNetClient.addProtocol(new OKTestProtocol());
	}

	public void getAdInfo(String city,
			OKResultTypeHandler<ADListInfo> notifier) {
		ADParam param = new ADParam();
		param.city = city;
		mOKNetClient.postAsync(param, notifier);
	}

}
