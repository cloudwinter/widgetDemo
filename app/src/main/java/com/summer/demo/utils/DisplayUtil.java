package com.summer.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by xiayundong on 2017/4/6.
 */
public class DisplayUtil {

	/**
	 * 获取屏幕的宽高<br>
	 * 包括状态栏的高度
	 * 
	 * @param context
	 * @return
	 */
	public static Point getScreenMetrics(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		return new Point(dm.widthPixels, dm.heightPixels);
	}

	/**
	 * 获取状态栏的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		int statusBarHeight = 38;// 通常这个值会是38
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int h = Integer.parseInt(field.get(obj).toString());
			if (h > 0) {
				return h;
			}
			return statusBarHeight;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusBarHeight;
	}

	/**
	 * 该方法依赖WMS的回调，所以在activity初始化的时候值是空的，建议在onWindowFocusChanged中执行 获取除去状态栏之外的尺寸
	 *
	 * @param activity
	 * @return
	 */
	public static Point getWindowContentScreenMetrics(Activity activity) {
		Point p = new Point();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		p.x = frame.width();
		p.y = frame.height();
		return p;
	}


	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

}
