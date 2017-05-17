package com.summer.demo.camera.ui;

import com.summer.demo.R;
import com.summer.demo.camera.util.CameraManager;
import com.summer.demo.core.BaseActivity;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

/**
 * 自定义相机 <br>
 * Created by xiayundong on 2017/5/11.
 */

public class CustomCameraActivity extends BaseActivity
		implements SurfaceHolder.Callback {

	private SurfaceView mSurfaceView;
	private Button mCaptureBtn;

	private CameraHandler mCameraHandler;
	private CameraManager mManager;
	private SurfaceHolder mSurfaceHolder;
	/**
	 * surfaceView是否加载成功
	 */
	private boolean mHasSurfaceView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_camera);
		mSurfaceView = (SurfaceView) findViewById(R.id.view_surface);
		mCaptureBtn = (Button) findViewById(R.id.but_capture);
		mCaptureBtn.setOnClickListener(mShootClick);
		mSurfaceHolder = mSurfaceView.getHolder();
		mHasSurfaceView = false;
		mManager = new CameraManager(this);
		mManager.setAutoCallBackListener(mAutoFocusCallBack);
		mCameraHandler = new CameraHandler(this, mManager);
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
	 * 初始化相机
	 */
	private void initCamera() {
		mManager.openDriver(mSurfaceHolder);
		mManager.startPreView();
		mManager.autoFocus(CameraHandler.MSG_WHAT_AUTO_FOCUS);
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

	/**
	 * 相机聚焦回调监听
	 */
	private CameraManager.AutoFocusCallBack mAutoFocusCallBack = new CameraManager.AutoFocusCallBack() {

		@Override
		public void onAutoFocus(int msgWhat, boolean success, Camera camera) {
			switch (msgWhat) {
			case CameraHandler.MSG_WHAT_AUTO_FOCUS:
				// 每个3s后自动聚焦
				mCameraHandler.sendEmptyMessageDelayed(msgWhat, 3000L);
				break;
			case CameraHandler.MSG_WHAT_TAKE_PICS:
				// 聚焦后马上开始拍照
				mCameraHandler.sendEmptyMessageDelayed(msgWhat, 100L);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 拍照按钮监听
	 */
	private View.OnClickListener mShootClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mManager.autoFocus(CameraHandler.MSG_WHAT_TAKE_PICS);
		}
	};
}
