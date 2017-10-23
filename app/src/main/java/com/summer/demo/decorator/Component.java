package com.summer.demo.decorator;

import android.util.Log;

/**
 * Created by xiayundong on 2017/10/10.
 */

public class Component {

	public static final String TAG = "Component";

	public void operation() {
		Log.e(TAG, "operation: super operate");
	}
}
