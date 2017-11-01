package com.summer.demo.banner.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by xiayundong on 2017/11/1.
 */

public class CustomScroller extends Scroller {

	private int mDuration;

	public CustomScroller(Context context) {
		super(context);
	}

	public CustomScroller(Context context, Interpolator interpolator) {
		super(context, interpolator);
	}

	public CustomScroller(Context context, Interpolator interpolator,
			boolean flywheel) {
		super(context, interpolator, flywheel);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy) {
		super.startScroll(startX, startY, dx, dy);
	}

	@Override
	public void startScroll(int startX, int startY, int dx, int dy,
			int duration) {
		super.startScroll(startX, startY, dx, dy, mDuration);
	}

	public int getmDuration() {
		return mDuration;
	}

	public void setmDuration(int mDuration) {
		this.mDuration = mDuration;
	}
}
