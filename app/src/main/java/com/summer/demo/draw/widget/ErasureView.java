package com.summer.demo.draw.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.summer.demo.R;

/**
 * Created by xiayundong on 2017/4/12.
 */

public class ErasureView extends View implements View.OnTouchListener {

	private int x = 0;
	private int y = 0;

	Bitmap bitmap;
	Canvas bitmapCanvas;
	private Path mPath = new Path();

	private Paint eraserPaint = null;

	public ErasureView(Context context) {
		super(context);
		init(context);
	}

	public ErasureView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void init(Context context) {
		this.setOnTouchListener(this);
		setFocusable(true);
		setFocusableInTouchMode(true);
		// Set background
		this.setBackgroundColor(Color.GREEN);

		// Set bitmap
		bitmap = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
		bitmapCanvas = new Canvas();
		bitmapCanvas.setBitmap(bitmap);
		bitmapCanvas.drawColor(Color.BLUE);

		// Paint paint = new Paint();
		// paint.setAntiAlias(true);
		// paint.setStyle(Paint.Style.FILL);
		// paint.setColor(Color.BLUE);
		// paint.setStrokeWidth(20);
		// bitmapCanvas.drawRect(0, 0, 320, 480, paint);

		// Set eraser paint properties
		eraserPaint = getEraserPaint();
		mPath.moveTo(0, 0);

	}

	private Paint getEraserPaint() {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(Color.RED);
		paint.setStrokeWidth(10);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.SQUARE);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		return paint;
	}

	@Override
	public void onDraw(Canvas canvas) {
		mPath.lineTo(x, y);
		bitmapCanvas.drawPath(mPath, eraserPaint);
		canvas.drawBitmap(bitmap, 0, 0, null);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		x = (int) event.getX();
		y = (int) event.getY();

		invalidate();
		return true;
	}

}
