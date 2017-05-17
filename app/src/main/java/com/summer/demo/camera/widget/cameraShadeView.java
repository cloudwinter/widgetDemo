package com.summer.demo.camera.widget;

import com.summer.demo.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by xiayundong on 2017/5/17.
 */
public class CameraShadeView extends FrameLayout {

	private Context mContext;
	private RectF mCameraRect = new RectF();
	private Paint mShadePaint;

	public CameraShadeView(Context context) {
		super(context);
		mContext = context;
		init(null);
	}

	public CameraShadeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		// 关闭硬件加速
		setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		if (attrs != null) {
			TypedArray array = mContext.obtainStyledAttributes(attrs,
					R.styleable.CameraShadeView);
			array.recycle();
			int rectLeft = array.getDimensionPixelSize(
					R.styleable.CameraShadeView_rect_left, 0);
			int rectTop = array.getDimensionPixelSize(
					R.styleable.CameraShadeView_rect_top, 0);
			int rectRight = array.getDimensionPixelSize(
					R.styleable.CameraShadeView_rect_right, 0);
			int rectBottom = array.getDimensionPixelSize(
					R.styleable.CameraShadeView_rect_bottom, 0);
			mCameraRect.left = rectLeft;
			mCameraRect.top = rectTop;
			mCameraRect.right = rectRight;
			mCameraRect.bottom = rectBottom;
		}
		setWillNotDraw(false);
		mShadePaint = new Paint();
		mShadePaint.setAntiAlias(true);
		mShadePaint.setColor(getResources().getColor(R.color.colorShade));
	}

	/**
	 * 设置拍照区域大小
	 */
	public void setRect(RectF rect) {
		if (rect == null) {
			return;
		}
		mCameraRect.set(rect);
		postInvalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int viewWidth = getWidth();
		int viewHeight = getHeight();

		canvas.save();
		canvas.clipRect(0, 0, viewWidth, viewHeight);
		canvas.clipRect(mCameraRect.left, mCameraRect.top,
				viewWidth - mCameraRect.right, viewHeight - mCameraRect.bottom,
				Region.Op.XOR);
		canvas.drawRect(0, 0, viewWidth, viewHeight, mShadePaint);
		canvas.restore();
	}
}
