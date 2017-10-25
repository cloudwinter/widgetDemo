package com.summer.network.okhttputil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.summer.network.code.NetCode;

import android.text.TextUtils;

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
	public void postAsync(final OKBaseRequestParam param,
			final OKCallback callback) {
		Request request = buildRequest(param);
		Call call = mOKHttpClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException t) {
				callback.notifyFailure(NetCode.RESULT_CODE_NET_ERROR,
						NetCode.RESULT_CODE_NET_ERROR_MSG);
			}

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {
				// 请求成功
				OKProtocolAction protocolAction = OKProtocolGroup
						.getProtocolAction(param.getClass());
				callbackResult(callback, protocolAction.resultType, response);
			}
		});
	}

	/**
	 * 同步post请求
	 * 
	 * @param param
	 */
	public OKResult postSync(OKBaseRequestParam param) {
		Request request = buildRequest(param);
		Call call = mOKHttpClient.newCall(request);
		OKResult result = null;
		try {
			Response response = call.execute();
			if (response.code() >= 200 && response.code() < 300) {
				OKProtocolAction protocolAction = OKProtocolGroup
						.getProtocolAction(param.getClass());
				result = parseTypeJson(response.body().toString(),
						protocolAction.resultType);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * get异步请求
	 * 
	 * @param url
	 * @param callback
	 */
	public void getAsync(String url, final Type dataType,
			final OKCallback callback) {
		Request request = new Request.Builder().url(url).build();
		Call call = mOKHttpClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				callback.notifyFailure(NetCode.RESULT_CODE_NET_ERROR,
						NetCode.RESULT_CODE_NET_ERROR_MSG);
			}

			@Override
			public void onResponse(Call call, Response response)
					throws IOException {
				callbackResult(callback, dataType, response);
			}
		});
	}

	/**
	 * 同步的get请求
	 * 
	 * @param url
	 * @param resultType
	 * @return
	 */
	public OKResult getSync(String url, Type resultType) {
		Request request = new Request.Builder().url(url).build();
		Call call = mOKHttpClient.newCall(request);
		OKResult result = null;
		try {
			Response response = call.execute();
			if (response.code() >= 200 && response.code() < 300) {
				result = parseTypeJson(response.body().toString(), resultType);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 创建请求Request
	 * 
	 * @param param
	 * @return
	 */
	private Request buildRequest(OKBaseRequestParam param) {
		String requestParamJson = new Gson().toJson(param);
		RequestBody requestBody = RequestBody.create(JSON, requestParamJson);
		OKProtocolAction protocolAction = OKProtocolGroup
				.getProtocolAction(param.getClass());
		Request request = new Request.Builder().url(protocolAction.url)
				.post(requestBody).build();
		return request;
	}

	/**
	 * 请求结果回调
	 * 
	 * @param callback
	 * @param response
	 */
	private void callbackResult(OKCallback callback, Type dataType,
			Response response) {
		if (response == null) {
			callback.notifyFailure(NetCode.RESULT_CODE_RESPONSE_NULL,
					NetCode.RESULT_CODE_RESPONSE_NULL_MSG);
		} else {
			if (response.code() >= 200 && response.code() < 300) {
				try {
					OKResult result = parseTypeJson(response.body().toString(),
							dataType);
					if (result.resultCode == NetCode.RESULT_CODE_SUCCESS) {
						callback.notifySuccess(result.data);
					} else {
						callback.notifyFailure(result.resultCode,
								result.resultMsg);
					}
				} catch (JSONException e) {
					callback.notifyFailure(NetCode.RESULT_CODE_JSON_ERROR,
							NetCode.RESULT_CODE_JSON_ERROR_MSG);
				}
			} else {
				callback.notifyFailure(NetCode.RESULT_CODE_NET_ERROR,
						NetCode.RESULT_CODE_NET_ERROR_MSG);
			}
		}
	}

	/**
	 * 解析返回结果
	 * 
	 * @param result
	 * @param resultType
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	private <T> OKResult<T> parseTypeJson(String result, Type resultType)
			throws JSONException {
		OKResult<T> okResult = null;
		if (TextUtils.isEmpty(result)) {
			okResult = new OKResult<T>();
			okResult.resultCode = NetCode.RESULT_CODE_RESPONSE_NULL;
			okResult.resultMsg = NetCode.RESULT_CODE_RESPONSE_NULL_MSG;
			return okResult;
		}
		JSONObject jsonObject = new JSONObject(result);
		okResult.resultCode = jsonObject.getInt(OKResult.JSON_CODE);
		if (jsonObject.has(OKResult.JSON_MSG)) {
			okResult.resultMsg = jsonObject.getString(OKResult.JSON_MSG);
		}
		if (resultType != Void.class) {
			String content = jsonObject.has(OKResult.JSON_DATA)
					? jsonObject.getString(OKResult.JSON_DATA) : null;
			if (!TextUtils.isEmpty(content)) {
				okResult.data = new Gson().fromJson(content, resultType);
			}
		}
		return okResult;
	}

}
