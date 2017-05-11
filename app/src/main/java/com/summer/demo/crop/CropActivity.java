package com.summer.demo.crop;

import com.summer.demo.R;
import com.summer.demo.core.BaseActivity;
import com.summer.demo.crop.ui.CropAreaFragment;
import com.summer.demo.crop.ui.CropFragment;
import com.summer.demo.title.BaseTitleView;
import com.summer.demo.utils.ListUtil;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by xiayundong on 2017/4/11.
 */

public class CropActivity extends BaseActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop);
		BaseTitleView titleView = (BaseTitleView) findViewById(R.id.title_view);
		titleView.setTitle("crop");
		initFragment();
	}

	private void initFragment() {
		CropFragment fragment = new CropFragment();
		startFirstFragment(fragment);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 获取当前的fragment
	 * 
	 * @return
	 */
	private Fragment getCurrentFragment() {
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		if (ListUtil.isEmpty(fragments)) {
			return null;
		}
		for (Fragment fragment : fragments) {
			if (fragment != null && fragment.isVisible()) {
				return fragment;
			}
		}
		return null;
	}
}
