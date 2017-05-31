package com.summer.demo.animation.widget;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * Created by xiayundong on 2017/5/23.
 */

public class MarginAnimation extends Animation {

	private View mView;
	private int mStart;
	private int mEnd;

	public MarginAnimation(View view) {
		mView = view;
	}

	/**
	 * 设置起始和结束值
	 * 
	 * @param start
	 * @param end
	 */
	public void setSides(int start, int end) {
		mStart = start;
		mEnd = end;
	}

	@Override
	protected void applyTransformation(float interpolatedTime,
			Transformation t) {
		super.applyTransformation(interpolatedTime, t);
		ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mView
				.getLayoutParams();
		layoutParams.topMargin = (int) ((mEnd - mStart) * interpolatedTime);
		mView.requestLayout();
	}

	@Override
	public boolean willChangeBounds() {
		return true;
	}
}
