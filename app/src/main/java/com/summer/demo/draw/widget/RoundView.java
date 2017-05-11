package com.summer.demo.draw.widget;

import java.io.InputStream;

import com.summer.demo.R;
import com.summer.demo.core.RunningEnviroment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

/**
 * 绘制圆角图片<br>
 * Created by xiayundong on 2017/4/7.
 */

public class RoundView extends ImageView {

	private Context mContext;

	private Bitmap mBitmap;
	/**
	 * 半径
	 */
	private int mRadius;
	/**
	 * 图片的缩放比例
	 */
	private float mScale;

	public RoundView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public RoundView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		try {

			DisplayMetrics dm = new DisplayMetrics();
			((Activity) mContext).getWindowManager().getDefaultDisplay()
					.getMetrics(dm);
			Log.e("init","dm.density:" + dm.density);
			Log.e("init","dm.densityDpi:" + dm.densityDpi);
			// 在as中使用openRaw加载本地资源需要前缀+，否则会Expected resource of type raw
			InputStream inputStream = getResources()
					.openRawResource(+R.mipmap.tt);
			mBitmap = BitmapFactory.decodeStream(inputStream);
			Log.e("init", "原始 ：mBitmap.width=" + mBitmap.getWidth()
					+ " mBitmap.height=" + mBitmap.getHeight());

			Drawable drawable = getResources().getDrawable(R.mipmap.tt);
			Bitmap drawbleBitmap = drawableToBitmap(drawable);
			Log.e("init", "转化后：drawbleBitmap.width=" + drawbleBitmap.getWidth()
					+ " drawbleBitmap.height=" + drawbleBitmap.getHeight());

			mBitmap = drawbleBitmap;
		} catch (Exception e) {

		}
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {

		Log.e("init", "转化前：drawable.width=" + drawable.getIntrinsicWidth()
				+ " drawable.height=" + drawable.getIntrinsicHeight());
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(),
				drawable.getOpacity() != PixelFormat.OPAQUE
						? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
		mRadius = size / 2;
		setMeasuredDimension(size, size);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setScaleType(ScaleType.FIT_XY);
		BitmapShader bitmapShader = new BitmapShader(mBitmap,
				BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
		mScale = (mRadius * 2.0f)
				/ Math.min(mBitmap.getWidth(), mBitmap.getHeight());
		Matrix matrix = new Matrix();
		matrix.setScale(mScale, mScale);
		bitmapShader.setLocalMatrix(matrix);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setShader(bitmapShader);
		canvas.drawCircle(mRadius, mRadius, mRadius, paint);
	}
}
