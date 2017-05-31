package com.summer.demo.mvp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.summer.demo.R;
import com.summer.demo.core.BaseActivity;
import com.summer.demo.mvp.presenter.PresenterIml;

/**
 * Created by xiayundong on 2017/5/31.
 */

public class MVPActivity extends BaseActivity implements ViewInterface {

	private Button mStartBut;
	private Button mStopBut;
	private SeekBar mSeekBar;
	private PresenterIml mPresenterIml;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mvp);
		mStartBut = (Button) findViewById(R.id.but_start);
		mStartBut.setOnClickListener(mClick);
		mStopBut = (Button) findViewById(R.id.but_stop);
		mStopBut.setOnClickListener(mClick);
		mSeekBar = (SeekBar) findViewById(R.id.bar_seek);
		mPresenterIml = new PresenterIml(this);
	}

	private View.OnClickListener mClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.but_start:
				mPresenterIml.startDown("fileUrl");
				break;
			case R.id.but_stop:
				mPresenterIml.stopDown();
				break;
			}
		}
	};

	@Override
	public void startDown() {
		Toast.makeText(this, "开始下载", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showProgress(int position) {
		mSeekBar.setProgress(position);
	}

	@Override
	public void downCompleted() {
		Toast.makeText(this, "下载成功", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void downError(String error) {
		Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
	}
}
