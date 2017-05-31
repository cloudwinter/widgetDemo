package com.summer.demo.animation.ui;

import com.summer.demo.R;
import com.summer.demo.core.BaseActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

/**
 *
 * 帧动画<br>
 * Created by xiayundong on 2017/5/22.
 */

public class FrameAnimationActivity extends BaseActivity {

	private ImageView mAnimationImg;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_frame_animation);
		mAnimationImg = (ImageView) findViewById(R.id.img_animation);
		mAnimationImg.setImageResource(R.drawable.frame_animation_bg);
		AnimationDrawable animationDrawable = (AnimationDrawable) mAnimationImg
				.getDrawable();
		animationDrawable.start();
	}
}
