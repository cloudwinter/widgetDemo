package com.summer.demo.touch;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by xiayundong on 2017/8/11.
 */

public class TouchEventView extends TextView {

	private static final String TAG = "TouchEventView";

	public TouchEventView(Context context) {
		super(context);
	}

	public TouchEventView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.e(TAG, "onTouchEvent" + " motion =" + event.getAction());
		return super.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		Log.e(TAG, "dispatchTouchEvent" + " motion =" + event.getAction());
		return super.dispatchTouchEvent(event);
	}
}
