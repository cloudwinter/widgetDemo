package com.summer.network.okhttputil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OKHttpclient请求<br>
 * Created by xiayundong on 2017/10/20.
 */

public class OKNetClient {

	/**
     * 使用post上传
	 */
	private static final MediaType JSON = MediaType
			.parse("application/json; charset=utf-8");

	private static OkHttpClient mOKHttpClient = null;

	public OKNetClient() {
		if (mOKHttpClient == null) {
			mOKHttpClient = new OkHttpClient.Builder()
					.connectTimeout(30, TimeUnit.SECONDS).build();
		}
	}

	/**
     * 异步post请求
	 * 
	 * @param param
     * @param callback
	 */
	public void asyncPost(OKBaseRequestParam param, OKCallback callback) {
		Request request = generateRequest(param);
		mOKHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {

			}
		});
	}

	/**
     * 生产请求参数
	 * 
	 * @param param
     * @return
     */
	private Request generateRequest(OKBaseRequestParam param) {
		String requestParamJson = new Gson().toJson(param);
		RequestBody requestBody = RequestBody.create(JSON, requestParamJson);
		OKProtocolAction protocolAction = OKProtocolGroup
				.getProtocolAction(param.getClass());
		Request request = new Request.Builder().url(protocolAction.url)
				.post(requestBody).build();
		return request;
	}

}
