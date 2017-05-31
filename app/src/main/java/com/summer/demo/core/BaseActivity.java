package com.summer.demo.core;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.summer.demo.R;
import com.summer.demo.widget.CustomProgress;

/**
 * Created by xiayundong on 2017/4/7.
 */

public class BaseActivity extends FragmentActivity {

	protected CustomProgress mCustomProgress;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCustomProgress = new CustomProgress(this);
	}

	/**
	 * 跳转第一个activity
	 * 
	 * @param fragment
	 */
	public void startFirstFragment(Fragment fragment) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = manager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_content, fragment);
		fragmentTransaction.commitAllowingStateLoss();
	}

	public void showProgress(String tips) {
		if (mCustomProgress != null && !mCustomProgress.isShowing()) {
			mCustomProgress.setProgressTips(tips);
			mCustomProgress.show();
		}
	}

	public void dismissProgress() {
		if (mCustomProgress != null && mCustomProgress.isShowing()) {
			mCustomProgress.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dismissProgress();
		mCustomProgress = null;
	}
}
