package com.summer.demo.touch;

import com.summer.demo.R;
import com.summer.demo.core.BaseActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 事件分发<br>
 * Created by xiayundong on 2017/8/10.
 */

public class TouchEventActivity extends BaseActivity {

	private static final String TAG = "touchEventActivity";

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_touchevent);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		Log.e(TAG, "dispatchTouchEvent()" + " motion =" + ev.getAction());
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.e(TAG, "onTouchEvent()" + " motion =" + event.getAction());
		return super.onTouchEvent(event);
	}

}
