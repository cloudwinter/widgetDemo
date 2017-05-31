package com.summer.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.summer.demo.R;

/**
 * Created by xiayundong on 2017/5/24.
 */

public class FixRatioRelativeLayout extends RelativeLayout {

	private static final int DEFAULT_FIXED = 0;

	/**
	 * 设置w/h比例
	 */
	private float mRatio = 1.0f;
	/**
	 * 固定的方向
	 */
	private int mFixed = DEFAULT_FIXED;

	public FixRatioRelativeLayout(Context context) {
		super(context);
	}

	public FixRatioRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		if (attrs != null) {
			TypedArray array = context.obtainStyledAttributes(attrs,
					R.styleable.FixRatioRelativeLayout);
			mRatio = array.getFloat(R.styleable.FixRatioRelativeLayout_ratio,
					1.0f);
			mFixed = array.getInt(R.styleable.FixRatioRelativeLayout_fixed,
					DEFAULT_FIXED);
			array.recycle();
		}
	}

	/**
	 * 设置w/h比例
	 * 
	 * @param rotio
	 */
	public void setRatio(float rotio) {
		mRatio = rotio;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
				getDefaultSize(0, heightMeasureSpec));
		int childWidthSize = getMeasuredWidth();
		int childHeightSize = getMeasuredHeight();
		if (DEFAULT_FIXED == mFixed) {
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,
					MeasureSpec.EXACTLY);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(
					(int) (childWidthSize / mRatio + 0.5), MeasureSpec.EXACTLY);

		} else {
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize,
					MeasureSpec.EXACTLY);
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(
					(int) (childHeightSize * mRatio + 0.5),
					MeasureSpec.EXACTLY);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
