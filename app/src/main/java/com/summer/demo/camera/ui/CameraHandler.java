package com.summer.demo.camera.ui;

import java.lang.ref.WeakReference;

import com.summer.demo.camera.task.SavePicTask;
import com.summer.demo.camera.util.CameraManager;

import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

/**
 * Created by xiayundong on 2017/5/12.
 */

public class CameraHandler extends Handler {

	public static final int MSG_WHAT_AUTO_FOCUS = 101;
	public static final int MSG_WHAT_TAKE_PICS = 102;

	private WeakReference<CustomCameraActivity> mActivity;
	private CameraManager mManager;

	public CameraHandler(CustomCameraActivity activity, CameraManager manager) {
		mActivity = new WeakReference<>(activity);
		mManager = manager;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if (!isValidActivity()) {
			return;
		}
		switch (msg.what) {
		case MSG_WHAT_AUTO_FOCUS:
			mManager.autoFocus(MSG_WHAT_AUTO_FOCUS);
			break;
		case MSG_WHAT_TAKE_PICS:
			mManager.takePicture(mPicCallBack);
			break;
		default:
		}
	}

	private Camera.PictureCallback mPicCallBack = new Camera.PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			new SavePicTask().execute(data);
		}
	};

	private boolean isValidActivity() {
		if (mActivity == null) {
			return false;
		}
		CustomCameraActivity cameraActivity = mActivity.get();
		if (Build.VERSION.SDK_INT > 16) {
			if (cameraActivity.isDestroyed()) {
				return false;
			}
		} else {
			if (cameraActivity.isFinishing()) {
				return false;
			}
		}
		return true;
	}
}
