package com.summer.demo.camera.ui;

import java.lang.ref.WeakReference;

import com.google.zxing.Result;
import com.summer.demo.camera.task.SavePicTask;
import com.summer.demo.camera.util.CameraManager;
import com.summer.demo.camera.util.DecodeHandler;

import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by xiayundong on 2017/5/12.
 */

public class CameraHandler extends Handler {

	public static final int MSG_WHAT_AUTO_FOCUS = 101;
	public static final int MSG_WHAT_TAKE_PICS = 102;
	public static final int MSG_WHAT_DECODE_FAILED = 107;
	public static final int MSG_WHAT_DECODE_SUCCESS = 108;

	private WeakReference<AbsCameraActivity> mActivity;
	private CameraManager mManager;

	public CameraHandler(AbsCameraActivity activity, CameraManager manager) {
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
		case MSG_WHAT_DECODE_FAILED:
			Log.e("CameraHandler", "decode failed");
			mManager.requestPreviewFrame();
			break;
		case MSG_WHAT_DECODE_SUCCESS:
			Result rawResult = (Result) msg.obj;
			Log.e("CameraHandler", "decode success: " + rawResult.getText());
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
		AbsCameraActivity cameraActivity = mActivity.get();
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
