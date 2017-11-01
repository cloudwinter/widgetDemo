package com.summer.demo.http;

import com.google.gson.Gson;
import com.summer.demo.R;
import com.summer.demo.core.BaseActivity;
import com.summer.demo.http.entity.ADListInfo;
import com.summer.demo.http.model.OKHttpTestModel;
import com.summer.network.okhttputil.entity.OKMessage;
import com.summer.network.okhttputil.notifer.OKResultTypeHandler;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * okhttpClient+handler 页面测试<br>
 * Created by xiayundong on 2017/10/25.
 */

public class OKHttpTestActivity extends BaseActivity {

	public static final String TAG = "OKHttpTestActivity";

	private Button mPostAsyncBut;
	private TextView mResultJsonText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_okhttptest);
		mPostAsyncBut = (Button) findViewById(R.id.but_post_async);
		mResultJsonText = (TextView) findViewById(R.id.text_result_json);
		mPostAsyncBut.setOnClickListener(mButClick);
	}

	private View.OnClickListener mButClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			new OKHttpTestModel().getAdInfo("",
					new OKResultTypeHandler<ADListInfo>(
							OKHttpTestActivity.this) {
						@Override
						public void onFailure(int code, OKMessage msg) {
							Log.e(TAG, "onFailure: msg=" + msg.msg);
						}

						@Override
						public void onSuccess(ADListInfo data) {
							if (data != null) {
								String resultJson = new Gson().toJson(data);
								mResultJsonText.setText(resultJson);
							}
						}
					});
		}
	};
}
