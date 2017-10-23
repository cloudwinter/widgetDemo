package com.summer.demo.touch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by xiayundong on 2017/8/11.
 */

public class TouchEventFrameLayout extends FrameLayout {

	private static final String TAG = "TouchEventFrameLayout";

	public TouchEventFrameLayout(@NonNull Context context) {
		super(context);
	}

	public TouchEventFrameLayout(@NonNull Context context,
			@Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.e(TAG, "onTouchEvent" + " motion =" + event.getAction());
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.e(TAG, "onInterceptTouchEvent" + " motion =" + ev.getAction());
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		Log.e(TAG, "dispatchTouchEvent" + " motion =" + ev.getAction());
		return super.dispatchTouchEvent(ev);
	}
}
