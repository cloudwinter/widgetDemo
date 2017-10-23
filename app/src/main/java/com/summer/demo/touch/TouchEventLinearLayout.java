package com.summer.demo.touch;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * 测试viewGroup的事件分发<br>
 * Created by xiayundong on 2017/8/10.
 */

public class TouchEventLinearLayout extends LinearLayout {

	private static final String TAG = "TouchEventLinearLayout";

	public TouchEventLinearLayout(Context context) {
		super(context);
	}

	public TouchEventLinearLayout(Context context,
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
