package com.summer.demo.camera.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.summer.demo.utils.DisplayUtil;
import com.summer.demo.utils.ListUtil;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;

/**
 * 相机配置信息
 * 
 * Created by xiayundong on 2017/5/11.
 */
public class CameraConfiguration {

	private static final String TAG = "CameraConfiguration";

	private Context mContext;

	public CameraConfiguration(Context context) {
		mContext = context;
	}

	public void setInitCameraParams(Camera camera) {
		Camera.Parameters parameters = camera.getParameters();
		setCameraOrientation(parameters);
		setCameraSize(parameters);
		camera.setParameters(parameters);
		camera.setDisplayOrientation(90);

		Camera.Size size = camera.getParameters().getPreviewSize();
		Log.e("CameraConfiguration","preview result: width="+size.width+" height:"+size.height);
	}

	/**
	 * 设置相机方向
	 */
	private void setCameraOrientation(Camera.Parameters parameters) {
		parameters.setRotation(90);
	}

	/**
	 * 设置预览和成图 图片的大小
	 * 
	 * @param parameters
	 */
	private void setCameraSize(Camera.Parameters parameters) {
		Point point = DisplayUtil.getScreenMetrics(mContext);
		Log.e(TAG, "X：" + point.x + " Y：" + point.y);
		List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
		Point pointPreview = getFitPreviewSize(previewSizes, point.x, point.y);
		List<Camera.Size> picsSizes = parameters.getSupportedPictureSizes();
		Point pointPicsView = getFitPicsView(picsSizes, pointPreview.x,
				pointPreview.y);
		Log.e(TAG, " result -->previewSizes:width=" + pointPreview.x
				+ " height=" + pointPreview.y);
		Log.e(TAG, " result -->picsSizes:width=" + pointPicsView.x + " height="
				+ pointPicsView.y);
		parameters.setPreviewSize(pointPreview.x, pointPreview.y);
		parameters.setPictureSize(pointPicsView.x, pointPicsView.y);

	}

	/**
	 * 以屏幕大小为基准，没有合适的取最大的
	 * 
	 * @param previewSizes
	 * @param minW
	 * @param minH
	 * @return
	 */
	private Point getFitPreviewSize(List<Camera.Size> previewSizes, int minW,
			int minH) {
		Point point = new Point();
		if (ListUtil.size(previewSizes) > 0) {
			Collections.sort(previewSizes, new ComparatorSize(1));
			for (Camera.Size size : previewSizes) {
				Log.e(TAG, "previewSizes:width=" + size.width + " height="
						+ size.height);
				// 因为默认是横屏，所以 w和h是反的
				if (size.height == size.width && size.width >= minH) {
					point.x = size.width;
					point.y = size.height;
					return point;
				}
			}
		}
		// 如果没有合适的取最大的
		Camera.Size sizeFirst = previewSizes.get(0);
		point.x = sizeFirst.width;
		point.y = sizeFirst.height;
		return point;
	}

	/**
	 * 以预览图片为基准，没有则升序去之后比例合适的
	 * 
	 * @param picsSizes
	 * @param minW
	 * @param minH
	 * @return
	 */
	private Point getFitPicsView(List<Camera.Size> picsSizes, int minW,
			int minH) {
		Point point = new Point();
		if (ListUtil.size(picsSizes) > 0) {
			Collections.sort(picsSizes, new ComparatorSize(1));
			int i = 0;
			int fitIndex = 0;
			float fitRatio = (float) minW / (float) minH;
			for (Camera.Size size : picsSizes) {
				if (size.width == minW || size.height == minH) {
					point.x = size.width;
					point.y = size.height;
					return point;
				}
				if (size.width > minW || size.height > minH) {
					float currentRatio = (float) size.width
							/ (float) size.height;
					if (Math.abs(currentRatio - fitRatio) < 0.03) {
						fitIndex = i;
					}
				}
				i++;
			}
			Camera.Size size = picsSizes.get(fitIndex);
			point.x = size.width;
			point.y = size.height;
			return point;
		}
		return point;
	}

	/**
	 * 按降序排列
	 */
	private class ComparatorSize implements Comparator<Camera.Size> {

		/**
		 * 0升序<br>
		 * 1降序
		 */
		private int mSort = 0;

		public ComparatorSize(int sort) {
			mSort = sort;
		}

		@Override
		public int compare(Camera.Size o1, Camera.Size o2) {
			if (o1.width > o2.width) {
				return (mSort == 0) ? 1 : -1;
			} else if (o1.width == o2.width) {
				return 0;
			} else {
				return (mSort == 0) ? -1 : 1;
			}
		}
	}
}
