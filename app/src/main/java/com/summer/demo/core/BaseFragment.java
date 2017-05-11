package com.summer.demo.core;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by xiayundong on 2017/4/7.
 */

public class BaseFragment extends Fragment {

	protected BaseActivity mActivity;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mActivity = (BaseActivity) getActivity();
	}

}
