package com.summer.demo.camera.util;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * camera工具类 Created by xiayundong on 2017/5/11.
 */

public class CameraManager {

	private Context mContext;
	private Camera mCamera;
	private CameraConfiguration mCameraConfiguration;
	private boolean mIsPreViewing;
	private AutoFocusCallBack mAutoFocusCallBack;
	private PreviewCallBack mPreviewCallBack;

	public CameraManager(Context context) {
		mContext = context;
		mCameraConfiguration = new CameraConfiguration(context);
		mPreviewCallBack = new PreviewCallBack();
	}

	/**
	 * 获取preview的size
	 * 
	 * @return
	 */
	public Camera.Size getCameraPreViewSize() {
		if (mCamera == null) {
			return null;
		}
		return mCamera.getParameters().getPreviewSize();
	}

	/**
	 * 打开相机<br>
	 * 初始化相机参数<br>
	 * 设置surfaceHolder
	 *
	 * @param holder
	 */
	public void openDriver(SurfaceHolder holder) {
		if (mCamera == null) {
			mCamera = Camera.open();
		}
		// 设置相机参数
		mCameraConfiguration.setInitCameraParams(mCamera);
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 预览图片
	 */
	public synchronized void startPreView() {
		if (mCamera == null || mIsPreViewing) {
			return;
		}
		mCamera.startPreview();
		mIsPreViewing = true;
	}

	/**
	 * 停止预览
	 */
	public synchronized void stopPreView() {
		if (mCamera == null || !mIsPreViewing) {
			return;
		}
		mCamera.stopPreview();
		mIsPreViewing = false;
	}

	/**
	 * 聚焦的回调监听
	 * 
	 * @param autoFocusCallBack
	 */
	public void setAutoCallBackListener(AutoFocusCallBack autoFocusCallBack) {
		mAutoFocusCallBack = autoFocusCallBack;
	}

	/**
	 * 自动聚焦
	 * 
	 * @param msgWhat
	 *            101自动聚焦 102拍照前聚焦
	 */
	public synchronized void autoFocus(final int msgWhat) {
		if (mCamera == null || !mIsPreViewing) {
			return;
		}
		mCamera.autoFocus(new Camera.AutoFocusCallback() {
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				if (mAutoFocusCallBack != null) {
					mAutoFocusCallBack.onAutoFocus(msgWhat, success, camera);
				}
			}
		});
		// TODO: 2017/5/12
	}

	public synchronized void cancelAutoFocus() {
		if (mCamera == null) {
			return;
		}
		mCamera.cancelAutoFocus();
	}

	/**
	 * 拍照
	 */
	public void takePicture(Camera.PictureCallback pictureCallback) {
		if (mCamera == null) {
			return;
		}
		try {
			mCamera.takePicture(null, null, null, pictureCallback);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 扫码
	 * 
	 */
	public synchronized void requestPreviewFrame() {
		if (mCamera == null) {
			return;
		}
		if (!mIsPreViewing) {
			startPreView();
		}
		mCamera.setOneShotPreviewCallback(mPreviewCallBack);
	}

	/**
	 * 释放cammera
	 */
	public void releaseCamera() {
		if (mCamera == null) {
			return;
		}
		mIsPreViewing = false;
		mCamera.release();
		mCamera = null;
	}

	public interface AutoFocusCallBack {
		void onAutoFocus(int msgWhat, boolean success, Camera camera);
	}

}
