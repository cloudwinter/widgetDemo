package com.summer.demo.camera.util;

import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.summer.demo.camera.ui.CameraHandler;

import java.util.Map;

/**
 * Created by xiayundong on 2017/5/26.
 */

public class DecodeHandler extends Handler {

	public static final int MSG_WHAT_DECODE = 0x1;

	private final MultiFormatReader multiFormatReader;

	private Handler mActivityHandler;
	private CameraManager mCameraManager;
	private Rect mCropRect;

	public DecodeHandler(Map<DecodeHintType, Object> hints) {
		multiFormatReader = new MultiFormatReader();
		multiFormatReader.setHints(hints);
	}

	public void setActivityHandler(Handler activityHandler) {
		mActivityHandler = activityHandler;
	}

	public void setCameraManager(CameraManager cameraManager) {
		mCameraManager = cameraManager;
	}

	public void setCropRect(Rect rect) {
		mCropRect = rect;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if (msg.what == MSG_WHAT_DECODE) {
			decode((byte[]) msg.obj);
		}
	}

	/**
	 * 解码数据
	 * 
	 * @param data
	 */
	private void decode(byte[] data) {
		if (mActivityHandler == null) {
			Log.e("DecodeHandler", "mActivity is not valid");
			return;
		}
		Camera.Size size = mCameraManager.getCameraPreViewSize();
		// 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
		byte[] rotatedData = new byte[data.length];
		for (int y = 0; y < size.height; y++) {
			for (int x = 0; x < size.width; x++)
				rotatedData[x * size.height + size.height - y - 1] = data[x
						+ y * size.width];
		}

		// 宽高也要调整
		int tmp = size.width;
		size.width = size.height;
		size.height = tmp;

		Result rawResult = null;
		PlanarYUVLuminanceSource source = buildLuminanceSource(rotatedData,
				size.width, size.height);
		Log.e("DecodeHandler", "source " + source.toString());
		if (source != null) {
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			try {
				rawResult = multiFormatReader.decodeWithState(bitmap);
				Log.e("DecodeHandler", "rawResult " + rawResult.toString());
			} catch (Exception re) {
				re.printStackTrace();
				Log.e("DecodeHandler", "exception :" + re.getMessage());
			} finally {
				multiFormatReader.reset();
			}
		}

		if (rawResult == null) {
			mActivityHandler
					.sendEmptyMessage(CameraHandler.MSG_WHAT_DECODE_FAILED);
			return;
		}
		Message msg = mActivityHandler.obtainMessage();
		msg.what = CameraHandler.MSG_WHAT_DECODE_SUCCESS;
		msg.obj = rawResult;
		msg.sendToTarget();
	}

	public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width,
			int height) {
		if (mCropRect == null) {
			Log.d("DecodeHandler", "mCropRect is not valid");
			return null;
		}
		// Go ahead and assume it's YUV rather than die.
		return new PlanarYUVLuminanceSource(data, width, height, mCropRect.left,
				mCropRect.top, mCropRect.width(), mCropRect.height(), false);
	}
}
