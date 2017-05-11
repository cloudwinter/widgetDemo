package com.summer.demo.crop.ui;

import com.summer.demo.R;
import com.summer.demo.core.BaseFragment;
import com.summer.demo.crop.widget.cropimage.CropImageView;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xiayundong on 2017/4/17.
 */

public class CropResultFragment extends BaseFragment {

	private CropImageView mCropImageView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_crop_result, container,
				false);
		mCropImageView = (CropImageView) view
				.findViewById(R.id.img_crop_result);
		Uri uri = getArguments().getParcelable(CropFragment.EXTRA_URI_KEY);
		try {
			mCropImageView.setImageUri(uri);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

}
