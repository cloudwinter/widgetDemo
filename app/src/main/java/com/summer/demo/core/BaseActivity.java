package com.summer.demo.core;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.summer.demo.R;

/**
 * Created by xiayundong on 2017/4/7.
 */

public class BaseActivity extends FragmentActivity {

	/**
	 * 
	 * @param fragment
	 */
	public void startFirstFragment(Fragment fragment) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = manager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_content, fragment);
		fragmentTransaction.commitAllowingStateLoss();
	}
}
