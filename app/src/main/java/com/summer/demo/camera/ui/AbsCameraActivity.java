package com.summer.demo.camera.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.summer.demo.R;
import com.summer.demo.camera.util.CameraManager;
import com.summer.demo.core.BaseActivity;

/**
 * Created by xiayundong on 2017/5/19.
 */

public abstract class AbsCameraActivity extends BaseActivity
		implements SurfaceHolder.Callback {

	protected SurfaceView mSurfaceView;

	protected CameraManager mManager;
	protected SurfaceHolder mSurfaceHolder;
	protected CameraHandler mCameraHandler;
	/**
	 * surfaceView是否加载成功
	 */
	private boolean mHasSurfaceView;

	public abstract int contentRes();

	protected void init() {

	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(contentRes());
		mSurfaceView = (SurfaceView) findViewById(R.id.view_surface);
		mSurfaceHolder = mSurfaceView.getHolder();
		mHasSurfaceView = false;
		mManager = new CameraManager(this);
		mCameraHandler = new CameraHandler(this, mManager);
		init();
		showProgress(null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mHasSurfaceView) {
			initCamera();
		} else {
			mSurfaceHolder.addCallback(this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mManager.stopPreView();
		if (!mHasSurfaceView) {
			mSurfaceHolder.removeCallback(this);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mManager.releaseCamera();
	}

	/**
	 * 初始化camera
	 */
	protected void initCamera() {
		mManager.openDriver(mSurfaceHolder);
		mManager.startPreView();
		mManager.autoFocus(CameraHandler.MSG_WHAT_AUTO_FOCUS);
		dismissProgress();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!mHasSurfaceView) {
			mHasSurfaceView = true;
			initCamera();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mHasSurfaceView = false;
	}
}
