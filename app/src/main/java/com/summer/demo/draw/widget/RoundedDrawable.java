package com.summer.demo.draw.widget;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * 支持：1.圆角处理 2.圆形图片处理 3.边框大小和色值设置
 * 
 * @author zhangxiuliang on 2016-10-11 11:09:41
 */

public class RoundedDrawable extends Drawable {

	/**
	 * 默认边框色值
	 */
	public static final int DEFAULT_BORDER_COLOR = Color.BLACK;

	/**
	 * 图片缩放矩阵
	 */
	private final Matrix mShaderMatrix = new Matrix();

	/**
	 * 控件边界区域
	 */
	private final RectF mBounds = new RectF();
	/**
	 * Drawable区域
	 */
	private final RectF mDrawableRect = new RectF();
	/**
	 * 原始图片区域
	 */
	private final RectF mBitmapRect = new RectF();
	/**
	 * 原始图片
	 */
	private final Bitmap mBitmap;
	/**
	 * 画笔
	 */
	private final Paint mBitmapPaint;
	/**
	 * 图片的宽度
	 */
	private final int mBitmapWidth;
	/**
	 * 图片的高度
	 */
	private final int mBitmapHeight;

	/**
	 * 边框的区域
	 */
	private final RectF mBorderRect = new RectF();
	/**
	 * 绘制边框的画笔
	 */
	private final Paint mBorderPaint;
	/**
	 * 边框的大小
	 */
	private float mBorderWidth = 0;
	/**
	 * 边框的色值
	 */
	private ColorStateList mBorderColor = ColorStateList
			.valueOf(DEFAULT_BORDER_COLOR);
	/**
	 * 图片的缩放类型
	 */
	private ImageView.ScaleType mScaleType = ImageView.ScaleType.FIT_CENTER;

	public RoundedDrawable(Bitmap bitmap) {
		mBitmap = bitmap;
		mBitmapWidth = bitmap.getWidth();
		mBitmapHeight = bitmap.getHeight();
		mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);
		mBitmapPaint = new Paint();
		mBitmapPaint.setStyle(Paint.Style.FILL);
		mBitmapPaint.setAntiAlias(true);
		mBorderPaint = new Paint();
		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setColor(DEFAULT_BORDER_COLOR);
		mBorderPaint.setStrokeWidth(mBorderWidth);
	}

	public static Drawable fromDrawable(Drawable drawable) {
		if (drawable instanceof RoundedDrawable) {
			return drawable;
		}
		Bitmap bitmap = drawableToBitmap(drawable);
		if (bitmap != null) {
			return new RoundedDrawable(bitmap);
		}
		return drawable;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(),
				drawable.getOpacity() != PixelFormat.OPAQUE
						? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;

	}

	@Override
	public boolean isStateful() {
		return mBorderColor.isStateful();
	}

	@Override
	protected boolean onStateChange(int[] state) {
		int newColor = mBorderColor.getColorForState(state, 0);
		if (mBorderPaint.getColor() != newColor) {
			mBorderPaint.setColor(newColor);
			return true;
		} else {
			return super.onStateChange(state);
		}
	}

	/**
	 * 根据scaleType计算Matrix，然后对图片裁剪
	 */
	private void updataShaderMatrixByScale() {
		float scale;
		float dx;
		float dy;
		switch (mScaleType) {
		case CENTER:
			mBorderRect.set(mBounds);
			mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
			mShaderMatrix.reset();
			mShaderMatrix
					.setTranslate(
							(int) ((mBorderRect.width() - mBitmapWidth) * 0.5f
									+ 0.5f),
							(int) ((mBorderRect.height() - mBitmapHeight) * 0.5f
									+ 0.5f));
			break;
		case CENTER_CROP:
			mBorderRect.set(mBounds);
			mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
			mShaderMatrix.reset();
			dx = 0;
			dy = 0;
			// 计算需要缩放比例最大的是宽度还是高度
			if (mBitmapWidth * mBorderRect.height() > mBorderRect.width()
					* mBitmapHeight) {
				scale = mBorderRect.height() / (float) mBitmapHeight;
				dx = (mBorderRect.width() - mBitmapWidth * scale) * 0.5f;
			} else {
				scale = mBorderRect.width() / (float) mBitmapWidth;
				dy = (mBorderRect.height() - mBitmapHeight * scale) * 0.5f;
			}
			mShaderMatrix.setScale(scale, scale);
			mShaderMatrix.setTranslate(dx, dy);
			break;
		case CENTER_INSIDE:
			mShaderMatrix.reset();
			if (mBitmapWidth <= mBounds.width()
					&& mBitmapHeight <= mBounds.height()) {
				scale = 1.0f;
			} else {
				scale = Math.min(mBounds.width() / mBitmapWidth,
						mBounds.height() / mBitmapHeight);
			}
			dx = (int) ((mBounds.width() - mBitmapWidth * scale) * 0.5f + 0.5f);
			dy = (int) ((mBounds.height() - mBitmapHeight * scale) * 0.5f
					+ 0.5f);
			mShaderMatrix.setScale(scale, scale);
			mShaderMatrix.setTranslate(dx, dy);
			mBorderRect.set(mBitmapRect);
			mShaderMatrix.mapRect(mBorderRect);
			mBorderRect.inset(mBorderWidth / 2, mBorderWidth / 2);
			mShaderMatrix.setRectToRect(mBitmapRect, mBorderRect,
					Matrix.ScaleToFit.FILL);
			break;
		case FIT_CENTER:
			break;
		case FIT_END:
			break;
		case FIT_START:
			break;
		case FIT_XY:
			break;

		}
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		mBounds.set(bounds);
		updataShaderMatrixByScale();
	}

	@Override
	public void draw(Canvas canvas) {

	}

	@Override
	public void setAlpha(int alpha) {

	}

	@Override
	public void setColorFilter(ColorFilter colorFilter) {

	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}
}
