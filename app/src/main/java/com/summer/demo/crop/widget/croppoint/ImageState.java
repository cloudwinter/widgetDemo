package com.summer.demo.crop.widget.croppoint;

/**
 * 存储imageView中图片的四个边的位置 <br>
 * Created by xiayundong on 2017/4/12.
 */
public class ImageState {
	private float mLeft;
	private float mTop;
	private float mRight;
	private float mBottom;

	public void setLeft(float mLeft) {
		this.mLeft = mLeft;
	}

	public float getLeft() {
		return mLeft;
	}

	public void setTop(float top) {
		this.mTop = top;
	}

	public float getTop() {
		return mTop;
	}

	public void setRight(float right) {
		this.mRight = right;
	}

	public float getRight() {
		return mRight;
	}

	public void setBottom(float bottom) {
		this.mBottom = bottom;
	}

	public float getBottom() {
		return mBottom;
	}

}
