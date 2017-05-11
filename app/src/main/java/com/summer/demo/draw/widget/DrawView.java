package com.summer.demo.draw.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by xiayundong on 2017/4/7.
 */

public class DrawView extends FrameLayout {

	public DrawView(Context context) {
		super(context);
		init();
	}

	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setWillNotDraw(false);
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 画线
		Paint paintLine = new Paint();
		paintLine.setColor(Color.BLUE);
		canvas.drawLine(40, 80, 500, 80, paintLine);
		float[] pts = { 40, 100, 100, 100, 100, 100, 100, 300 };
		canvas.drawLines(pts, paintLine);

		// 画圆

		// 画圆弧

		// 画正方形
	}
}
