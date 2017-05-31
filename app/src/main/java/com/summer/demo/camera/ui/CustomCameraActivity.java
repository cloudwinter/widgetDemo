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

public class CustomCameraActivity extends AbsCameraActivity {

	private Button mCaptureBtn;

	@Override
	public int contentRes() {
		return R.layout.activity_custom_camera;
	}

	@Override
	protected void init() {
		mCaptureBtn = (Button) findViewById(R.id.but_capture);
		mCaptureBtn.setOnClickListener(mShootClick);
		mManager.setAutoCallBackListener(mAutoFocusCallBack);
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
