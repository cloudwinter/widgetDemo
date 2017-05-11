package com.summer.demo.crop.widget.cropimage;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import com.summer.demo.crop.callback.BitmapCropCallback;
import com.summer.demo.crop.callback.CropBoundsChangeListener;
import com.summer.demo.crop.model.CropParameters;
import com.summer.demo.crop.model.CropImageState;
import com.summer.demo.crop.task.BitmapCropTask;
import com.summer.demo.crop.util.CropRectUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by Oleksii Shliama (https://github.com/shliama).
 * <p/>
 * This class adds crop feature, methods to draw crop guidelines, and keep image
 * in correct state. Also it extends parent class methods to add checks for
 * scale; animating zoom in/out.
 */
public class CropImageView extends TransformImageView {

	public static final int DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION = 500;
	public static final float DEFAULT_MAX_SCALE_MULTIPLIER = 10.0f;
	public static final float SOURCE_IMAGE_ASPECT_RATIO = 0f;

	private final RectF mCropRect = new RectF();

	private final Matrix mTempMatrix = new Matrix();

	private float mTargetAspectRatio;
	private float mMaxScaleMultiplier = DEFAULT_MAX_SCALE_MULTIPLIER;

	private CropBoundsChangeListener mCropBoundsChangeListener;

	private Runnable mWrapCropBoundsRunnable = null;

	private float mMaxScale, mMinScale;
	private int mMaxResultImageSizeX = 0, mMaxResultImageSizeY = 0;
	private long mImageToWrapCropBoundsAnimDuration = DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION;

	public CropImageView(Context context) {
		this(context, null);
	}

	public CropImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CropImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Cancels all current animations and sets image to fill crop area (without
	 * animation). Then creates and executes {@link BitmapCropTask} with proper
	 * parameters.
	 */
	public void cropAndSaveImage(String imageOutPath,
			@NonNull Bitmap.CompressFormat compressFormat, int compressQuality,
			@Nullable BitmapCropCallback cropCallback) {
		cancelAllAnimations();
		setImageToWrapCropBounds(false);

		final CropImageState imageState = new CropImageState(mCropRect,
				CropRectUtils.trapToRect(mCurrentImageCorners),
				getCurrentScale(), getCurrentAngle());

		final CropParameters cropParameters = new CropParameters(
				mMaxResultImageSizeX, mMaxResultImageSizeY, compressFormat,
				compressQuality, getImageInputPath(), imageOutPath,
				getExifInfo());

		// FIXME 待修改
		new BitmapCropTask(getViewBitmap(), imageState, cropParameters,
				cropCallback).execute();
	}

	/**
	 * @return - maximum scale value for current image and crop ratio
	 */
	public float getMaxScale() {
		return mMaxScale;
	}

	/**
	 * @return - minimum scale value for current image and crop ratio
	 */
	public float getMinScale() {
		return mMinScale;
	}

	/**
	 * Updates current crop rectangle with given. Also recalculates image
	 * properties and position to fit new crop rectangle.
	 *
	 * @param cropRect
	 *            - new crop rectangle
	 */
	public void setCropRect(RectF cropRect) {
		mTargetAspectRatio = cropRect.width() / cropRect.height();
		mCropRect.set(cropRect.left - getPaddingLeft(),
				cropRect.top - getPaddingTop(),
				cropRect.right - getPaddingRight(),
				cropRect.bottom - getPaddingBottom());
		calculateImageScaleBounds();
		setImageToWrapCropBounds();
	}

	public void setCropBoundsChangeListener(
			@Nullable CropBoundsChangeListener cropBoundsChangeListener) {
		mCropBoundsChangeListener = cropBoundsChangeListener;
	}

	/**
	 * This method scales image up for given value related to given coords (x,
	 * y).
	 */
	public void zoomInImage(float scale, float centerX, float centerY) {
		if (scale <= getMaxScale()) {
			postScale(scale / getCurrentScale(), centerX, centerY);
		}
	}

	/**
	 * This method changes image scale for given value related to point (px, py)
	 * but only if resulting scale is in min/max bounds.
	 *
	 * @param deltaScale
	 *            - scale value
	 * @param px
	 *            - scale center X
	 * @param py
	 *            - scale center Y
	 */
	public void postScale(float deltaScale, float px, float py) {
		if (deltaScale > 1 && getCurrentScale() * deltaScale <= getMaxScale()) {
			super.postScale(deltaScale, px, py);
		} else if (deltaScale < 1
				&& getCurrentScale() * deltaScale >= getMinScale()) {
			super.postScale(deltaScale, px, py);
		}
	}

	/**
	 * This method cancels all current Runnable objects that represent
	 * animations.
	 */
	public void cancelAllAnimations() {
		removeCallbacks(mWrapCropBoundsRunnable);
	}

	public void setImageToWrapCropBounds() {
		setImageToWrapCropBounds(true);
	}

	/**
	 * If image doesn't fill the crop bounds it must be translated and scaled
	 * properly to fill those.
	 * <p/>
	 * Therefore this method calculates delta X, Y and scale values and passes
	 * them to the {@link WrapCropBoundsRunnable} which animates image. Scale
	 * value must be calculated only if image won't fill the crop bounds after
	 * it's translated to the crop bounds rectangle center. Using temporary
	 * variables this method checks this case.
	 */
	public void setImageToWrapCropBounds(boolean animate) {
		if (mBitmapLaidOut && !isImageWrapCropBounds()) {

			float currentX = mCurrentImageCenter[0];
			float currentY = mCurrentImageCenter[1];
			float currentScale = getCurrentScale();

			float deltaX = mCropRect.centerX() - currentX;
			float deltaY = mCropRect.centerY() - currentY;
			float deltaScale = 0;

			mTempMatrix.reset();
			mTempMatrix.setTranslate(deltaX, deltaY);

			final float[] tempCurrentImageCorners = Arrays
					.copyOf(mCurrentImageCorners, mCurrentImageCorners.length);
			mTempMatrix.mapPoints(tempCurrentImageCorners);

			boolean willImageWrapCropBoundsAfterTranslate = isImageWrapCropBounds(
					tempCurrentImageCorners);

			if (willImageWrapCropBoundsAfterTranslate) {
				final float[] imageIndents = calculateImageIndents();
				deltaX = -(imageIndents[0] + imageIndents[2]);
				deltaY = -(imageIndents[1] + imageIndents[3]);
			} else {
				RectF tempCropRect = new RectF(mCropRect);
				mTempMatrix.reset();
				mTempMatrix.setRotate(getCurrentAngle());
				mTempMatrix.mapRect(tempCropRect);

				final float[] currentImageSides = CropRectUtils
						.getRectSidesFromCorners(mCurrentImageCorners);

				deltaScale = Math.max(
						tempCropRect.width() / currentImageSides[0],
						tempCropRect.height() / currentImageSides[1]);
				deltaScale = deltaScale * currentScale - currentScale;
			}

			if (animate) {
				post(mWrapCropBoundsRunnable = new WrapCropBoundsRunnable(
						CropImageView.this, mImageToWrapCropBoundsAnimDuration,
						currentX, currentY, deltaX, deltaY, currentScale,
						deltaScale, willImageWrapCropBoundsAfterTranslate));
			} else {
				postTranslate(deltaX, deltaY);
				if (!willImageWrapCropBoundsAfterTranslate) {
					zoomInImage(currentScale + deltaScale, mCropRect.centerX(),
							mCropRect.centerY());
				}
			}
		}
	}

	/**
	 * First, un-rotate image and crop rectangles (make image rectangle
	 * axis-aligned). Second, calculate deltas between those rectangles sides.
	 * Third, depending on delta (its sign) put them or zero inside an array.
	 * Fourth, using Matrix, rotate back those points (indents).
	 *
	 * @return - the float array of image indents (4 floats) - in this order
	 *         [left, top, right, bottom]
	 */
	private float[] calculateImageIndents() {
		mTempMatrix.reset();
		mTempMatrix.setRotate(-getCurrentAngle());

		float[] unrotatedImageCorners = Arrays.copyOf(mCurrentImageCorners,
				mCurrentImageCorners.length);
		float[] unrotatedCropBoundsCorners = CropRectUtils
				.getCornersFromRect(mCropRect);

		mTempMatrix.mapPoints(unrotatedImageCorners);
		mTempMatrix.mapPoints(unrotatedCropBoundsCorners);

		RectF unrotatedImageRect = CropRectUtils
				.trapToRect(unrotatedImageCorners);
		RectF unrotatedCropRect = CropRectUtils
				.trapToRect(unrotatedCropBoundsCorners);

		float deltaLeft = unrotatedImageRect.left - unrotatedCropRect.left;
		float deltaTop = unrotatedImageRect.top - unrotatedCropRect.top;
		float deltaRight = unrotatedImageRect.right - unrotatedCropRect.right;
		float deltaBottom = unrotatedImageRect.bottom
				- unrotatedCropRect.bottom;

		float indents[] = new float[4];
		indents[0] = (deltaLeft > 0) ? deltaLeft : 0;
		indents[1] = (deltaTop > 0) ? deltaTop : 0;
		indents[2] = (deltaRight < 0) ? deltaRight : 0;
		indents[3] = (deltaBottom < 0) ? deltaBottom : 0;

		mTempMatrix.reset();
		mTempMatrix.setRotate(getCurrentAngle());
		mTempMatrix.mapPoints(indents);

		return indents;
	}

	/**
	 * When image is laid out it must be centered properly to fit current crop
	 * bounds.
	 */
	@Override
	protected void onImageLaidOut() {
		super.onImageLaidOut();
		final Drawable drawable = getDrawable();
		if (drawable == null) {
			return;
		}

		float drawableWidth = drawable.getIntrinsicWidth();
		float drawableHeight = drawable.getIntrinsicHeight();

		if (mTargetAspectRatio == SOURCE_IMAGE_ASPECT_RATIO) {
			mTargetAspectRatio = drawableWidth / drawableHeight;
		}

		int height = (int) (mThisWidth / mTargetAspectRatio);
		if (height > mThisHeight) {
			int width = (int) (mThisHeight * mTargetAspectRatio);
			int halfDiff = (mThisWidth - width) / 2;
			mCropRect.set(halfDiff, 0, width + halfDiff, mThisHeight);
		} else {
			int halfDiff = (mThisHeight - height) / 2;
			mCropRect.set(0, halfDiff, mThisWidth, height + halfDiff);
		}

		calculateImageScaleBounds(drawableWidth, drawableHeight);
		setupInitialImagePosition(drawableWidth, drawableHeight);

		if (mCropBoundsChangeListener != null) {
			mCropBoundsChangeListener
					.onCropAspectRatioChanged(mTargetAspectRatio);
		}
		if (mTransformImageListener != null) {
			mTransformImageListener.onScale(getCurrentScale());
			mTransformImageListener.onRotate(getCurrentAngle());
		}
	}

	/**
	 * This method checks whether current image fills the crop bounds.
	 */
	protected boolean isImageWrapCropBounds() {
		return isImageWrapCropBounds(mCurrentImageCorners);
	}

	/**
	 * This methods checks whether a rectangle that is represented as 4 corner
	 * points (8 floats) fills the crop bounds rectangle.
	 *
	 * @param imageCorners
	 *            - corners of a rectangle
	 * @return - true if it wraps crop bounds, false - otherwise
	 */
	protected boolean isImageWrapCropBounds(float[] imageCorners) {
		mTempMatrix.reset();
		mTempMatrix.setRotate(-getCurrentAngle());

		float[] unrotatedImageCorners = Arrays.copyOf(imageCorners,
				imageCorners.length);
		mTempMatrix.mapPoints(unrotatedImageCorners);

		float[] unrotatedCropBoundsCorners = CropRectUtils
				.getCornersFromRect(mCropRect);
		mTempMatrix.mapPoints(unrotatedCropBoundsCorners);

		return CropRectUtils.trapToRect(unrotatedImageCorners)
				.contains(CropRectUtils.trapToRect(unrotatedCropBoundsCorners));
	}

	private void calculateImageScaleBounds() {
		final Drawable drawable = getDrawable();
		if (drawable == null) {
			return;
		}
		calculateImageScaleBounds(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
	}

	/**
	 * This method calculates image minimum and maximum scale values for current
	 * {@link #mCropRect}.
	 *
	 * @param drawableWidth
	 *            - image width
	 * @param drawableHeight
	 *            - image height
	 */
	private void calculateImageScaleBounds(float drawableWidth,
			float drawableHeight) {
		float widthScale = Math.min(mCropRect.width() / drawableWidth,
				mCropRect.width() / drawableHeight);
		float heightScale = Math.min(mCropRect.height() / drawableHeight,
				mCropRect.height() / drawableWidth);

		mMinScale = Math.min(widthScale, heightScale);
		mMaxScale = mMinScale * mMaxScaleMultiplier;
	}

	/**
	 * This method calculates initial image position so it is positioned
	 * properly. Then it sets those values to the current image matrix.
	 *
	 * @param drawableWidth
	 *            - image width
	 * @param drawableHeight
	 *            - image height
	 */
	private void setupInitialImagePosition(float drawableWidth,
			float drawableHeight) {
		float cropRectWidth = mCropRect.width();
		float cropRectHeight = mCropRect.height();

		float widthScale = mCropRect.width() / drawableWidth;
		float heightScale = mCropRect.height() / drawableHeight;

		float initialMinScale = Math.max(widthScale, heightScale);

		float tw = (cropRectWidth - drawableWidth * initialMinScale) / 2.0f
				+ mCropRect.left;
		float th = (cropRectHeight - drawableHeight * initialMinScale) / 2.0f
				+ mCropRect.top;

		mCurrentImageMatrix.reset();
		mCurrentImageMatrix.postScale(initialMinScale, initialMinScale);
		mCurrentImageMatrix.postTranslate(tw, th);
		setImageMatrix(mCurrentImageMatrix);
	}

	/**
	 * This Runnable is used to animate an image so it fills the crop bounds
	 * entirely. Given values are interpolated during the animation time.
	 * Runnable can be terminated either vie {@link #cancelAllAnimations()}
	 * method or when certain conditions inside
	 * {@link WrapCropBoundsRunnable#run()} method are triggered.
	 */
	private static class WrapCropBoundsRunnable implements Runnable {

		private final WeakReference<CropImageView> mCropImageView;

		private final long mDurationMs, mStartTime;
		private final float mOldX, mOldY;
		private final float mCenterDiffX, mCenterDiffY;
		private final float mOldScale;
		private final float mDeltaScale;
		private final boolean mWillBeImageInBoundsAfterTranslate;

		public WrapCropBoundsRunnable(CropImageView cropImageView,
				long durationMs, float oldX, float oldY, float centerDiffX,
				float centerDiffY, float oldScale, float deltaScale,
				boolean willBeImageInBoundsAfterTranslate) {

			mCropImageView = new WeakReference<>(cropImageView);

			mDurationMs = durationMs;
			mStartTime = System.currentTimeMillis();
			mOldX = oldX;
			mOldY = oldY;
			mCenterDiffX = centerDiffX;
			mCenterDiffY = centerDiffY;
			mOldScale = oldScale;
			mDeltaScale = deltaScale;
			mWillBeImageInBoundsAfterTranslate = willBeImageInBoundsAfterTranslate;
		}

		@Override
		public void run() {
			CropImageView cropImageView = mCropImageView.get();
			if (cropImageView == null) {
				return;
			}

			long now = System.currentTimeMillis();
			float currentMs = Math.min(mDurationMs, now - mStartTime);

			float newX = easeOut(currentMs, 0, mCenterDiffX, mDurationMs);
			float newY = easeOut(currentMs, 0, mCenterDiffY, mDurationMs);
			float newScale = easeInOut(currentMs, 0, mDeltaScale, mDurationMs);

			if (currentMs < mDurationMs) {
				cropImageView.postTranslate(
						newX - (cropImageView.mCurrentImageCenter[0] - mOldX),
						newY - (cropImageView.mCurrentImageCenter[1] - mOldY));
				if (!mWillBeImageInBoundsAfterTranslate) {
					cropImageView.zoomInImage(mOldScale + newScale,
							cropImageView.mCropRect.centerX(),
							cropImageView.mCropRect.centerY());
				}
				if (!cropImageView.isImageWrapCropBounds()) {
					cropImageView.post(this);
				}
			}
		}

		private float easeOut(float time, float start, float end,
				float duration) {
			return end * ((time = time / duration - 1.0f) * time * time + 1.0f)
					+ start;
		}

		private float easeInOut(float time, float start, float end,
				float duration) {
			return (time /= duration / 2.0f) < 1.0f
					? end / 2.0f * time * time * time + start
					: end / 2.0f * ((time -= 2.0f) * time * time + 2.0f)
							+ start;
		}
	}

}
