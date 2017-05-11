package com.summer.demo.crop.ui;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.summer.demo.R;
import com.summer.demo.core.BaseFragment;
import com.summer.demo.crop.widget.croparea.BottomImageView;
import com.summer.demo.crop.widget.croparea.ChooseArea;

/**
 * Created by xiayundong on 2017/4/11.
 */

public class CropAreaFragment extends BaseFragment {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
//		mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		mActivity.getWindow().setFlags(
//				WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		View view = inflater.inflate(R.layout.fragment_crop_area, container,
				false);

		BottomImageView bottomImageView = (BottomImageView) view
				.findViewById(R.id.layout2_bottomView);
		ImageView zoomArea = (ImageView) view
				.findViewById(R.id.layout2_imageAbove);
		ChooseArea chooseArea = (ChooseArea) view
				.findViewById(R.id.layout2_topView);

		bottomImageView.setZoomView(zoomArea);
		chooseArea.setBottomView(bottomImageView);
		chooseArea.setRegion(new Point(200, 400), new Point(600, 400),
				new Point(600, 900), new Point(200, 900));
		return view;
	}
}
