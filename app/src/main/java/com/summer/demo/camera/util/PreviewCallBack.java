package com.summer.demo.camera.util;

import org.greenrobot.eventbus.EventBus;

import com.summer.demo.camera.entity.MessageEvent;

import android.hardware.Camera;
import android.util.Log;

/**
 * 预览回调<br>
 * Created by xiayundong on 2017/5/25.
 */

public class PreviewCallBack implements Camera.PreviewCallback {

	public PreviewCallBack() {
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		Log.e("PreviewCallBack", "onPreviewFrame");
		MessageEvent event = new MessageEvent();
		event.data = data;
		EventBus.getDefault().post(event);
	}
}
