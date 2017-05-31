package com.summer.demo.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.summer.demo.R;

/**
 * 自定义加载loadging <br>
 * Created by xiayundong on 2017/5/19.
 */

public class CustomProgress extends Dialog {

	private String mProgressTips;
	private ImageView mProgressIcon;
	private Animation animation;
	private Context mContext;

	public CustomProgress(Context context) {
		super(context, R.style.CustomProgressDialog);

		mContext = context;
	}

	public void setProgressTips(String tips) {
		mProgressTips = tips;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_custom_progress);
		getWindow().getAttributes().gravity = Gravity.CENTER;
		TextView progressText = (TextView) findViewById(R.id.txt_progress);
		if (!TextUtils.isEmpty(mProgressTips)) {
			progressText.setText(mProgressTips);
		}
		mProgressIcon = (ImageView) findViewById(R.id.img_progress);
		animation = AnimationUtils.loadAnimation(mContext,
				R.anim.progress_animation);

	}

	@Override
	public void show() {
		super.show();
		mProgressIcon.clearAnimation();
		mProgressIcon.startAnimation(animation);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		mProgressIcon.clearAnimation();
	}
}
