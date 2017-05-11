package com.summer.demo.crop.ui;

import com.summer.demo.R;
import com.summer.demo.core.AppConfig;
import com.summer.demo.core.BaseFragment;
import com.summer.demo.crop.callback.BitmapCropCallback;
import com.summer.demo.crop.callback.CropBoundsChangeListener;
import com.summer.demo.crop.widget.cropimage.CropImageView;
import com.summer.demo.crop.widget.cropimage.OverlayView;
import com.summer.demo.crop.widget.cropimage.OverlayViewChangeListener;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by xiayundong on 2017/4/14.
 */

public class CropImageFragment extends BaseFragment {

	private CropImageView mCropImageView;
	private OverlayView mOverlayView;
	private TextView mSaveText;

	private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
	public static final int DEFAULT_COMPRESS_QUALITY = 90;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_crop_image, container,
				false);
		mCropImageView = (CropImageView) view.findViewById(R.id.img_crop);
		mOverlayView = (OverlayView) view.findViewById(R.id.view_overlay);
		mSaveText = (TextView) view.findViewById(R.id.save_text);
		mSaveText.setOnClickListener(mSaveClick);
		Uri uri = getArguments().getParcelable(CropFragment.EXTRA_URI_KEY);
		try {
			mCropImageView.setImageUri(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setListeners();
		return view;
	}

	private void setListeners() {
		mOverlayView
				.setOverlayViewChangeListener(new OverlayViewChangeListener() {
					@Override
					public void onCropRectUpdated(RectF cropRect) {
						mCropImageView.setCropRect(cropRect);
					}
				});

		mCropImageView
				.setCropBoundsChangeListener(new CropBoundsChangeListener() {
					@Override
					public void onCropAspectRatioChanged(float cropRatio) {
						mOverlayView.setTargetAspectRatio(cropRatio);
					}
				});
	}

	private String generateOutTempUrl() {
		return AppConfig.APP_CACHE_PATH + "compress_temp" + ".jpg";
	}

	private View.OnClickListener mSaveClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mCropImageView.cropAndSaveImage(generateOutTempUrl(),
					mCompressFormat, DEFAULT_COMPRESS_QUALITY,
					new BitmapCropCallback() {
						@Override
						public void onBitmapCropped(@NonNull Uri resultUri,
								int imageWidth, int imageHeight) {
							CropResultFragment resultFragment = new CropResultFragment();
							Bundle bundle = new Bundle();
							bundle.putParcelable(CropFragment.EXTRA_URI_KEY,
									resultUri);
							resultFragment.setArguments(bundle);
							mActivity.startFirstFragment(resultFragment);
						}

						@Override
						public void onCropFailure(@NonNull Throwable t) {

						}
					});
		}
	};
}
