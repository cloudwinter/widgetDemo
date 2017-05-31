package com.summer.demo.camera.ui;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.summer.demo.R;
import com.summer.demo.camera.entity.MessageEvent;
import com.summer.demo.camera.util.DecodeFormatManager;
import com.summer.demo.camera.util.DecodeHandler;
import com.summer.demo.camera.widget.CameraShadeView;

import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * Created by xiayundong on 2017/5/17.
 */

public class DecodeActivity extends AbsCameraActivity {

	/**
	 * 阴影view
	 */
	private CameraShadeView mShsdeView;
	/**
	 * 扫描框
	 */
	private RelativeLayout mSanFrame;
	/**
	 * 扫描线
	 */
	private ImageView mScanLine;

	private MultiFormatReader multiFormatReader;

	@Override
	public int contentRes() {
		return R.layout.activity_decode;
	}

	@Override
	protected void init() {
		mShsdeView = (CameraShadeView) findViewById(R.id.view_shade);
		mSanFrame = (RelativeLayout) findViewById(R.id.fram_layout);
		mScanLine = (ImageView) findViewById(R.id.img_scan_line);

		TranslateAnimation translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		translateAnimation.setDuration(1600);
		translateAnimation.setRepeatCount(-1);
		translateAnimation.setRepeatMode(Animation.RESTART);
		mScanLine.startAnimation(translateAnimation);
		mScanLine.setVisibility(View.VISIBLE);

		multiFormatReader = new MultiFormatReader();
		multiFormatReader.setHints(DecodeFormatManager.getHints());

		mShsdeView.post(new Runnable() {
			@Override
			public void run() {
				mManager.requestPreviewFrame();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
	}

	@Subscribe(threadMode = ThreadMode.BACKGROUND)
	public void onMessageEvent(MessageEvent event) {
		Log.e("DecodeActivity", "onMessageEvent()");
		byte[] data = event.data;
		Camera.Size size = mManager.getCameraPreViewSize();
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

		Log.e("DecodeHandler",
				"size widht " + size.width + " height " + size.height);

		Result rawResult = null;
		PlanarYUVLuminanceSource source = buildLuminanceSource(rotatedData,
				size.width, size.height);
		Log.e("DecodeHandler", "source " + source.toString());
		if (source != null) {
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			Log.e("DecodeHandler", "bitmap = " + bitmap.toString() + " width ="
					+ bitmap.getWidth() + " height =" + bitmap.getHeight());
			try {
				Log.e("DecodeHandler",
						"bitmap = " + bitmap.toString() + " width ="
								+ bitmap.getWidth() + " height ="
								+ bitmap.getHeight());
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
			mCameraHandler
					.sendEmptyMessage(CameraHandler.MSG_WHAT_DECODE_FAILED);
			return;
		}
		Message msg = mCameraHandler.obtainMessage();
		msg.what = CameraHandler.MSG_WHAT_DECODE_SUCCESS;
		msg.obj = rawResult;
		msg.sendToTarget();
	}

	public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width,
			int height) {
		Rect mCropRect = mShsdeView.getHollowRect();
		if (mCropRect == null) {
			Log.d("DecodeHandler", "mCropRect is not valid");
			return null;
		}
		Log.e("DecodeActivity",
				"left " + mCropRect.left + " top " + mCropRect.top + " right "
						+ mCropRect.right + " bottom " + mCropRect.bottom);
		// Go ahead and assume it's YUV rather than die.
		return new PlanarYUVLuminanceSource(data, width, height, mCropRect.left,
				mCropRect.top, mCropRect.width(), mCropRect.height(), false);
	}

}
