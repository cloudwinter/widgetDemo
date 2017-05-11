package com.summer.demo.crop.widget.croppoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.summer.demo.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * p 1**** 12 ****2 <br>
 * p ************** <br>
 * p 13***********24 <br>
 * p ************** <br>
 * p 3**** 34 ****4 <br>
 * 控制截取图的8个点组成的多边形 Created by xiayundong on 28/03/15.
 */
public class PolygonView extends FrameLayout {

	private int HORIZONTAL = 0;
	private int VERTICAL = 1;

	private PolygonView mPolygonView;
	private Context mContext;

	private ImageView mPoint1;
	private ImageView mPoint2;
	private ImageView mPoint3;
	private ImageView mPoint4;
	private ImageView mPoint12;
	private ImageView mPoint13;
	private ImageView mPoint24;
	private ImageView mPoint34;

	private Paint mMarkPaint;
	private Paint mLinkPaint;

	private float mBitmapWidth;
	private float mBitmapHeight;

	private Map<Integer, PointF> mMapPonits = new HashMap<>();

	public PolygonView(Context context) {
		super(context);
		init(context);
	}

	public PolygonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mPolygonView = this;
		mContext = context;
		mPoint1 = getImageView();
		mPoint1.setOnTouchListener(new CornerTouch(1));
		mPoint2 = getImageView();
		mPoint2.setOnTouchListener(new CornerTouch(2));
		mPoint3 = getImageView();
		mPoint3.setOnTouchListener(new CornerTouch(3));
		mPoint4 = getImageView();
		mPoint4.setOnTouchListener(new CornerTouch(4));
		mPoint12 = getImageView();
		mPoint12.setOnTouchListener(new CenterTouch(1, 2, VERTICAL));
		mPoint13 = getImageView();
		mPoint13.setOnTouchListener(new CenterTouch(1, 3, HORIZONTAL));
		mPoint24 = getImageView();
		mPoint24.setOnTouchListener(new CenterTouch(2, 4, HORIZONTAL));
		mPoint34 = getImageView();
		mPoint34.setOnTouchListener(new CenterTouch(3, 4, VERTICAL));
		addView(mPoint1);
		addView(mPoint2);
		addView(mPoint3);
		addView(mPoint4);
		addView(mPoint12);
		addView(mPoint13);
		addView(mPoint24);
		addView(mPoint34);

		mMarkPaint = new Paint();
		mMarkPaint.setColor(getResources().getColor(R.color.colorMark));
		mMarkPaint.setStyle(Paint.Style.FILL);
		mMarkPaint.setAntiAlias(true);

		mLinkPaint = new Paint();
		mLinkPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));
		mLinkPaint.setStrokeWidth(1);
		mLinkPaint.setAntiAlias(true);
	}

	private ImageView getImageView() {
		ImageView imageView = new ImageView(mContext);
		LayoutParams layoutParams = new LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		imageView.setLayoutParams(layoutParams);
		imageView.setImageResource(R.drawable.polygon_point_drawable);
		return imageView;
	}

	/**
	 * 设置img的位置
	 * 
	 * @param img
	 * @param pointF
	 */
	private void setImgXY(ImageView img, PointF pointF) {
		img.setX(pointF.x);
		img.setY(pointF.y);
	}

	/**
	 * 设置point的位置
	 */
	public void setMapPoints(List<PointF> points, float width, float height) {
		mBitmapHeight = height;
		mBitmapWidth = width;
		// TODO: 2017/4/12
		PointF centerPoint = new PointF();
		int size = points.size();
		for (PointF pointF : points) {
			centerPoint.x += pointF.x / size;
			centerPoint.y += pointF.y / size;
		}
		for (PointF pointF : points) {
			int index = -1;
			if (pointF.x < centerPoint.x && pointF.y < centerPoint.y) {
				index = 1;
			} else if (pointF.x > centerPoint.x && pointF.y < centerPoint.y) {
				index = 2;
			} else if (pointF.x < centerPoint.x && pointF.y > centerPoint.y) {
				index = 3;
			} else if (pointF.x > centerPoint.x && pointF.y > centerPoint.y) {
				index = 4;
			}
			mMapPonits.put(index, pointF);
		}
	}

	/**
	 * 判断是否是可以裁剪的形状
	 * 
	 * @return
	 */
	private boolean isValidShape() {
		return correctPoints();
	}

	/**
	 * 纠正错误的角度
	 */
	private boolean correctPoints() {
		PointF p1 = mMapPonits.get(1);
		PointF p2 = mMapPonits.get(2);
		PointF p3 = mMapPonits.get(3);
		PointF p4 = mMapPonits.get(4);
		// 把各个点纠正到正常的位置
		if (p1.x > p2.x)
			exchange(1, 2);
		if (p1.y > p3.y)
			exchange(1, 3);
		if (p3.x > p4.x)
			exchange(3, 4);
		if (p2.y > p4.y)
			exchange(2, 4);

		// 计算每个角，角度过大的不能纠偏
		boolean flag = true;
		double edge1 = calculateEdge(p1, p2);
		double edge2 = calculateEdge(p4, p2);
		double edge3 = calculateEdge(p3, p4);
		double edge4 = calculateEdge(p3, p1);
		double diagonal1 = calculateEdge(p4, p1);
		double diagonal2 = calculateEdge(p3, p2);

		flag &= calculateAngle(edge1, edge4, diagonal2);
		flag &= calculateAngle(edge1, edge2, diagonal1);
		flag &= calculateAngle(edge2, edge3, diagonal2);
		flag &= calculateAngle(edge3, edge4, diagonal1);

		return flag;
	}

	private void exchange(int p1, int p2) {
		PointF temp = new PointF();
		temp.set(mMapPonits.get(p1));
		PointF point1 = mMapPonits.get(p1);
		point1.set(mMapPonits.get(p2));
		PointF point2 = mMapPonits.get(p2);
		point2.set(temp);
	}

	private double calculateEdge(PointF p1, PointF p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}

	/**
	 * 使用余弦定理 cos(x) = (a^2+b^2-c^2) / 2*a*b 其中p1对应的角度就是x
	 * 
	 * @return
	 */
	private boolean calculateAngle(double a, double b, double c) {
		double cosX = (a * a + b * b - c * c) / (2 * a * b);
		if (Math.abs(cosX) > 0.707)
			return false;
		return true;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mMapPonits.size() <= 0) {
			return;
		}
		PointF pointF1 = mMapPonits.get(1);
		PointF pointF2 = mMapPonits.get(2);
		PointF pointF3 = mMapPonits.get(3);
		PointF pointF4 = mMapPonits.get(4);

		setImgXY(mPoint1, pointF1);
		setImgXY(mPoint2, pointF2);
		setImgXY(mPoint3, pointF3);
		setImgXY(mPoint4, pointF4);
		setImgXY(mPoint12, new PointF((pointF1.x + pointF2.x) / 2,
				(pointF1.y + pointF2.y) / 2));
		setImgXY(mPoint13, new PointF((pointF1.x + pointF3.x) / 2,
				(pointF1.y + pointF3.y) / 2));
		setImgXY(mPoint24, new PointF((pointF2.x + pointF4.x) / 2,
				(pointF2.y + pointF4.y) / 2));
		setImgXY(mPoint34, new PointF((pointF3.x + pointF4.x) / 2,
				(pointF3.y + pointF4.y) / 2));

		canvas.drawLine(mPoint1.getX() + (mPoint1.getWidth() / 2),
				mPoint1.getY() + (mPoint1.getHeight() / 2),
				mPoint3.getX() + (mPoint3.getWidth() / 2),
				mPoint3.getY() + (mPoint3.getHeight() / 2), mLinkPaint);
		canvas.drawLine(mPoint1.getX() + (mPoint1.getWidth() / 2),
				mPoint1.getY() + (mPoint1.getHeight() / 2),
				mPoint2.getX() + (mPoint2.getWidth() / 2),
				mPoint2.getY() + (mPoint2.getHeight() / 2), mLinkPaint);
		canvas.drawLine(mPoint2.getX() + (mPoint2.getWidth() / 2),
				mPoint2.getY() + (mPoint2.getHeight() / 2),
				mPoint4.getX() + (mPoint4.getWidth() / 2),
				mPoint4.getY() + (mPoint4.getHeight() / 2), mLinkPaint);
		canvas.drawLine(mPoint3.getX() + (mPoint3.getWidth() / 2),
				mPoint3.getY() + (mPoint3.getHeight() / 2),
				mPoint4.getX() + (mPoint4.getWidth() / 2),
				mPoint4.getY() + (mPoint4.getHeight() / 2), mLinkPaint);

		Path path12 = new Path();
		path12.moveTo(mPoint1.getWidth() / 2, mPoint1.getHeight() / 2);
		path12.lineTo(getWidth() - mPoint2.getWidth() / 2,
				mPoint2.getHeight() / 2);
		path12.lineTo(mPoint2.getX() + (mPoint2.getWidth() / 2),
				mPoint2.getY() + (mPoint2.getHeight() / 2));
		path12.lineTo(mPoint1.getX() + (mPoint1.getWidth() / 2),
				mPoint1.getY() + (mPoint1.getHeight() / 2));
		path12.close();
		canvas.drawPath(path12, mMarkPaint);

		Path path13 = new Path();
		path13.moveTo(mPoint1.getWidth() / 2, mPoint1.getHeight() / 2);
		path13.lineTo(mPoint1.getX() + (mPoint1.getWidth() / 2),
				mPoint1.getY() + (mPoint1.getHeight() / 2));
		path13.lineTo(mPoint3.getX() + (mPoint3.getWidth() / 2),
				mPoint3.getY() + (mPoint3.getHeight() / 2));
		path13.lineTo(mPoint3.getWidth() / 2,
				getHeight() - mPoint3.getWidth() / 2);
		path13.close();
		canvas.drawPath(path13, mMarkPaint);

		Path path24 = new Path();
		path24.moveTo(getWidth() - mPoint2.getWidth() / 2,
				mPoint2.getHeight() / 2);
		path24.lineTo(getWidth() - mPoint4.getWidth() / 2,
				getHeight() - mPoint4.getWidth() / 2);
		path24.lineTo(mPoint4.getX() + (mPoint3.getWidth() / 2),
				mPoint4.getY() + (mPoint4.getHeight() / 2));
		path24.lineTo(mPoint2.getX() + (mPoint2.getWidth() / 2),
				mPoint2.getY() + (mPoint2.getHeight() / 2));
		path24.close();
		canvas.drawPath(path24, mMarkPaint);

		Path path34 = new Path();
		path34.moveTo(mPoint3.getWidth() / 2,
				getHeight() - mPoint3.getHeight() / 2);
		path34.lineTo(mPoint3.getX() + (mPoint3.getWidth() / 2),
				mPoint3.getY() + (mPoint3.getHeight() / 2));
		path34.lineTo(mPoint4.getX() + (mPoint3.getWidth() / 2),
				mPoint4.getY() + (mPoint4.getHeight() / 2));
		path34.lineTo(getWidth() - (mPoint4.getWidth() / 2),
				getHeight() - (mPoint4.getHeight() / 2));
		path34.close();
		canvas.drawPath(path34, mMarkPaint);
	}

	/**
	 * 四个角的touch事件处理
	 */
	private class CornerTouch implements OnTouchListener {

		private int mKey;
		float mDownX = 0;
		float mDownY = 0;
		PointF startPT = new PointF();

		public CornerTouch(int key) {
			mKey = key;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mDownX = event.getX();
				mDownY = event.getY();
				startPT = new PointF(v.getX(), v.getY());
				Log.e("CornerTouch ACTION_DOWN",
						" downX=" + mDownX + " downY=" + mDownY);
				break;
			case MotionEvent.ACTION_MOVE:
				float minW = (mPolygonView.getWidth() - mBitmapWidth
						- v.getWidth()) / 2;
				float minH = (mPolygonView.getHeight() - mBitmapHeight
						- v.getHeight()) / 2;
				float maxW = minW + mBitmapWidth;
				float maxH = minH + mBitmapHeight;
				float moveX = event.getX() - mDownX;
				float moveY = event.getY() - mDownY;
				Log.e("CornerTouch ACTION_MOVE",
						" event.getX()=" + event.getX() + " event.getY()="
								+ event.getY() + " moveX=" + moveX + " moveY="
								+ moveY);
				if ((startPT.x + moveX) < maxW && (startPT.y + moveY) < maxH
						&& (startPT.x + moveX) > minW
						&& (startPT.y + moveY) > minH) {
					PointF point = mMapPonits.get(mKey);
					if (point != null) {
						float pointMoveX = point.x + moveX;
						float pointMoveY = point.y + moveY;
						point.set(pointMoveX, pointMoveY);
						startPT = new PointF(pointMoveX, pointMoveY);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				// TODO: 2017/4/12
				if (isValidShape()) {
					mLinkPaint.setColor(
							getResources().getColor(R.color.colorPrimaryDark));
				} else {
					mLinkPaint.setColor(Color.RED);
				}
				break;
			}
			mPolygonView.invalidate();
			return true;
		}
	}

	/**
	 * 中间点的touch事件处理
	 */
	private class CenterTouch implements OnTouchListener {

		/**
		 * 中间点的上下或左右两边点的key值
		 */
		private int mKeyS;
		private int mKeyE;
		/**
		 * 方向
		 */
		private int mOrientation;

		private float mDownX = 0;
		private float mDownY = 0;

		PointF startPT = new PointF();

		public CenterTouch(int keyS, int keyE, int orientation) {
			mKeyS = keyS;
			mKeyE = keyE;
			mOrientation = orientation;

		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mDownX = event.getX();
				mDownY = event.getY();
				startPT = new PointF(v.getX(), v.getY());
				break;
			case MotionEvent.ACTION_MOVE:
				float minW = (mPolygonView.getWidth() - mBitmapWidth
						- v.getWidth()) / 2;
				float minH = (mPolygonView.getHeight() - mBitmapHeight
						- v.getHeight()) / 2;
				float maxW = minW + mBitmapWidth;
				float maxH = minH + mBitmapHeight;

				float moveX = event.getX() - mDownX;
				float moveY = event.getY() - mDownY;

				boolean isValid = false;
				PointF pointS = mMapPonits.get(mKeyS);
				PointF pointE = mMapPonits.get(mKeyE);
				float pointSMoveY = pointS.y + moveY;
				float pointEMoveY = pointE.y + moveY;
				float pointSMoveX = pointS.x + moveX;
				float pointEMoveX = pointE.x + moveX;
				if (mOrientation == VERTICAL) {
					if (Math.max(pointSMoveY, pointEMoveY) < maxH
							&& Math.min(pointSMoveY, pointEMoveY) > minH) {
						isValid = true;
					}
				} else {
					if (Math.max(pointSMoveX, pointEMoveX) < maxW
							&& Math.min(pointSMoveX, pointSMoveX) > minW) {
						isValid = true;
					}
				}

				if (isValid) {
					if (pointS != null && pointE != null) {
						if (mOrientation == VERTICAL) {
							pointS.set(pointS.x, pointSMoveY);
							pointE.set(pointE.x, pointEMoveY);
						} else {
							pointS.set(pointSMoveX, pointS.y);
							pointE.set(pointEMoveX, pointE.y);
						}
						startPT = new PointF(startPT.x + moveX,
								startPT.y + moveY);
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if (isValidShape()) {
					mLinkPaint.setColor(
							getResources().getColor(R.color.colorPrimaryDark));
				} else {
					mLinkPaint.setColor(Color.RED);
				}
				break;
			}
			mPolygonView.invalidate();
			return true;
		}
	}
}
