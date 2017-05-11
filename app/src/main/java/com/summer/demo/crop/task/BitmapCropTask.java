package com.summer.demo.crop.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.summer.demo.core.RunningEnviroment;
import com.summer.demo.crop.callback.BitmapCropCallback;
import com.summer.demo.crop.model.CropParameters;
import com.summer.demo.crop.model.ExifInfo;
import com.summer.demo.crop.model.CropImageState;
import com.summer.demo.crop.util.CropBitmapUtils;
import com.summer.demo.crop.util.ImageHeaderParser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Crops part of image that fills the crop bounds.
 * <p/>
 * First image is downscaled if max size was set and if resulting image is
 * larger that max size. Then image is rotated accordingly. Finally new Bitmap
 * object is created and saved to file.
 */
public class BitmapCropTask extends AsyncTask<Void, Void, Throwable> {

	private static final int DEFAULT_RESULT_SIZE = 1028;

	private static final String TAG = "BitmapCropTask";

	private Bitmap mViewBitmap;

	private final RectF mCropRect;
	private final RectF mCurrentImageRect;

	private float mCurrentScale;

	private static int mMaxResultImageSizeX, mMaxResultImageSizeY;
	private final Bitmap.CompressFormat mCompressFormat;
	private final int mCompressQuality;
	private final String mImageInputPath, mImageOutputPath;
	private final ExifInfo mExifInfo;
	private final BitmapCropCallback mCropCallback;

	private int mCroppedImageWidth, mCroppedImageHeight;

	public BitmapCropTask(@Nullable Bitmap viewBitmap,
			@NonNull CropImageState imageState,
			@NonNull CropParameters cropParameters,
			@Nullable BitmapCropCallback cropCallback) {

		mViewBitmap = viewBitmap;
		mCropRect = imageState.getCropRect();
		mCurrentImageRect = imageState.getCurrentImageRect();

		mCurrentScale = imageState.getCurrentScale();
		mMaxResultImageSizeX = cropParameters.getMaxResultImageSizeX();
		if (mMaxResultImageSizeX <= 0) {
			mMaxResultImageSizeX = DEFAULT_RESULT_SIZE;
		}
		mMaxResultImageSizeY = cropParameters.getMaxResultImageSizeY();
		if (mMaxResultImageSizeY <= 0) {
			mMaxResultImageSizeY = DEFAULT_RESULT_SIZE;
		}
		mCompressFormat = cropParameters.getCompressFormat();
		mCompressQuality = cropParameters.getCompressQuality();

		mImageInputPath = cropParameters.getImageInputPath();
		mImageOutputPath = cropParameters.getImageOutputPath();
		mExifInfo = cropParameters.getExifInfo();

		mCropCallback = cropCallback;
	}

	@Override
	@Nullable
	protected Throwable doInBackground(Void... params) {
		if (mViewBitmap == null) {
			return new NullPointerException("ViewBitmap is null");
		} else if (mViewBitmap.isRecycled()) {
			return new NullPointerException("ViewBitmap is recycled");
		} else if (mCurrentImageRect.isEmpty()) {
			return new NullPointerException("CurrentImageRect is empty");
		}
		resize();
		try {
			crop();
			mViewBitmap = null;
		} catch (Throwable throwable) {
			return throwable;
		}

		return null;
	}

	private float resize() {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mImageInputPath, options);

		boolean swapSides = mExifInfo.getExifDegrees() == 90
				|| mExifInfo.getExifDegrees() == 270;
		float scaleX = (swapSides ? options.outHeight : options.outWidth)
				/ (float) mViewBitmap.getWidth();
		float scaleY = (swapSides ? options.outWidth : options.outHeight)
				/ (float) mViewBitmap.getHeight();

		float resizeScale = Math.min(scaleX, scaleY);

		mCurrentScale /= resizeScale;

		return resizeScale;
	}

	private boolean crop() throws IOException {
		ExifInterface originalExif = new ExifInterface(mImageInputPath);

		int top = Math
				.round((mCropRect.top - mCurrentImageRect.top) / mCurrentScale);
		int left = Math.round(
				(mCropRect.left - mCurrentImageRect.left) / mCurrentScale);
		mCroppedImageWidth = Math.round(mCropRect.width() / mCurrentScale);
		mCroppedImageHeight = Math.round(mCropRect.height() / mCurrentScale);

		boolean shouldCrop = shouldCrop(mCroppedImageWidth,
				mCroppedImageHeight);
		Log.i(TAG, "Should crop: " + shouldCrop);

		if (shouldCrop) {
			boolean cropped = cropCImg(mImageInputPath, mImageOutputPath, left,
					top, mCroppedImageWidth, mCroppedImageHeight,
					mCompressQuality, mExifInfo.getExifDegrees(),
					mExifInfo.getExifTranslation());
			if (cropped && mCompressFormat.equals(Bitmap.CompressFormat.JPEG)) {
				ImageHeaderParser.copyExif(originalExif, mCroppedImageWidth,
						mCroppedImageHeight, mImageOutputPath);
			}
			return cropped;
		} else {
			return false;
		}
	}

	/**
	 * Check whether an image should be cropped at all or just file can be
	 * copied to the destination path. For each 1000 pixels there is one pixel
	 * of error due to matrix calculations etc.
	 *
	 * @param width
	 *            - crop area width
	 * @param height
	 *            - crop area height
	 * @return - true if image must be cropped, false - if original image fits
	 *         requirements
	 */
	private boolean shouldCrop(int width, int height) {
		int pixelError = 1;
		pixelError += Math.round(Math.max(width, height) / 1000f);
		return Math.abs(mCropRect.left - mCurrentImageRect.left) > pixelError
				|| Math.abs(mCropRect.top - mCurrentImageRect.top) > pixelError
				|| Math.abs(mCropRect.bottom
						- mCurrentImageRect.bottom) > pixelError
				|| Math.abs(
						mCropRect.right - mCurrentImageRect.right) > pixelError;
	}

	// @SuppressWarnings("JniMissingFunction")
	// native public static boolean cropCImg(String inputPath, String
	// outputPath,
	// int left, int top, int width, int height, float angle,
	// float resizeScale, int format, int quality, int exifDegrees,
	// int exifTranslation) throws IOException, OutOfMemoryError;

	public static boolean cropCImg(String inputPath, String outputPath,
			int left, int top, int width, int height, int quality,
			int exifDegrees, int exifTranslation) {
		try {
			Bitmap bitmap = null;
			InputStream is = null;
			is = RunningEnviroment.sContext.getContentResolver()
					.openInputStream(Uri.fromFile(new File(inputPath)));
			BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is,
					false);
			Matrix matrixCopy = new Matrix();
			RectF rectF = new RectF(left, top, left + width, top + height);
			RectF adjusted = new RectF();
			matrixCopy.mapRect(adjusted, rectF);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = CropBitmapUtils.calculateInSampleSize(
					(int) rectF.width(), (int) rectF.height(),
					mMaxResultImageSizeX, mMaxResultImageSizeY);
			// 判断在rgb8888配置下内存是否够用
			if (isMemEnough(options.inSampleSize, (int) rectF.width(),
					(int) rectF.height(), 4)) {
				options = CropBitmapUtils.get8888Option(options);
			} else {
				// 可用内存不足，使用RGB565色值设置，减少内存占用
				options = CropBitmapUtils.get565Option(options);
				// 判断在rgb565配置下内存是否够用
				if (!isMemEnough(options.inSampleSize, (int) rectF.width(),
						(int) rectF.height(), 2)) {
					// rgb565模式下内存还是不够，将图片压缩
					if (options.inSampleSize < 2) {
						// 如果图片质量会影响审核，这步骤要去掉
						options.inSampleSize = 2;
					}
				}
			}
			bitmap = decoder.decodeRegion(
					new Rect((int) adjusted.left, (int) adjusted.top,
							(int) adjusted.right, (int) adjusted.bottom),
					options);
			Matrix matrixTransform = new Matrix();
			if (exifDegrees != 0) {
				matrixTransform.preRotate(exifDegrees);
			}
			if (exifTranslation != 1) {
				matrixTransform.postScale(exifTranslation, 1);
			}
			if (!matrixTransform.isIdentity()) {
				bitmap = CropBitmapUtils.transformBitmap(bitmap,
						matrixTransform);
			}
			cropPic(bitmap, outputPath, quality);
			return true;
		} catch (Exception e) {
			e.printStackTrace();

		} catch (OutOfMemoryError error) {
			error.printStackTrace();
		}
		return false;
	}

	private static void cropPic(Bitmap bitmap, String outputPath, int quality) {
		FileOutputStream fos = null;
		try {
			if (bitmap != null) {
				// 3、 质量压缩到80
				fos = new FileOutputStream(new File(outputPath));
				bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bitmap != null) {
				bitmap.recycle();
			}
		}
	}

	/**
	 * 判断内存是否足够使用，3倍于图片bitmap所占内存判定为够用
	 *
	 * @param width
	 * @param length
	 * @param bitLength
	 *            每像素所占byte数
	 * @return
	 */
	private static boolean isMemEnough(int inSampleSize, int width, int length,
			int bitLength) {
		long ams = getAvailableMemSize();
		long imageS = width * length * bitLength * 3 / inSampleSize
				* inSampleSize;
		return ams > imageS ? true : false;
	}

	/**
	 * 获取可用内存
	 */
	public static long getAvailableMemSize() {
		// 应用程序最大可用内存
		long maxMemory = Runtime.getRuntime().maxMemory();
		// 应用程序已获得内存
		long totalMemory = Runtime.getRuntime().totalMemory();
		// 应用程序已获得内存中未使用内存
		long freeMemory = Runtime.getRuntime().freeMemory();
		long memSize = maxMemory - (totalMemory - freeMemory);
		return memSize;
	}

	@Override
	protected void onPostExecute(@Nullable Throwable t) {
		if (mCropCallback != null) {
			if (t == null) {
				mCropCallback.onBitmapCropped(
						Uri.fromFile(new File(mImageOutputPath)),
						mCroppedImageWidth, mCroppedImageHeight);
			} else {
				mCropCallback.onCropFailure(t);
			}
		}
	}

}
