package com.summer.demo.animation.ui;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.animation.widget.MarginAnimation;
import com.summer.demo.animation.widget.NoticeVerRolllView;
import com.summer.demo.core.BaseActivity;
import com.summer.demo.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 补间动画<br>
 * Created by xiayundong on 2017/5/22.
 */

public class TweenAnimationActivity extends BaseActivity {

	private NoticeVerRolllView mNoticeVerRollView;
	private LinearLayout mLinearLayout;
	private TextView mFirstView;
	private TextView mSecondView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tween_animation);
		mNoticeVerRollView = (NoticeVerRolllView) findViewById(
				R.id.view_notice);
		mLinearLayout = (LinearLayout) findViewById(R.id.layout_linear);
		mFirstView = (TextView) findViewById(R.id.text_first);
		mSecondView = (TextView) findViewById(R.id.text_second);
		mNoticeVerRollView.setDefault("滚动公告默认数据");
		mNoticeVerRollView.postDelayed(new Runnable() {
			@Override
			public void run() {
				List<String> datas = new ArrayList<String>();
				datas.add("滚动公告测试数据1");
				datas.add("滚动公告测试数据2");
				datas.add("滚动公告测试数据3");
				datas.add("滚动公告测试数据4 最长就是我顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶 长");
				mNoticeVerRollView.setDatas(datas);
				mNoticeVerRollView.startRolling();
			}
		}, 3000);

		int height = DisplayUtil.dip2px(this, 50);
		LinearLayout.LayoutParams firstLayoutParams = new LinearLayout.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT, height);
		LinearLayout.LayoutParams secondLayoutParams = new LinearLayout.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT, height);
		mFirstView.setLayoutParams(firstLayoutParams);
		mSecondView.setLayoutParams(secondLayoutParams);
		MarginAnimation animation = new MarginAnimation(mFirstView);
		animation.setSides(0, -height);
		animation.setDuration(3000);
		mFirstView.startAnimation(animation);

//		 int height = DisplayUtil.dip2px(this,50);
//		 ObjectAnimator firstAnimator =
//		 ObjectAnimator.ofFloat(mFirstView,"TranslationY",0,-height);
//		 firstAnimator.setDuration(3000);
//		 firstAnimator.start();
//
//		 ObjectAnimator secondAnimator =
//		 ObjectAnimator.ofFloat(mSecondView,"TranslationY",height,0);
//		 secondAnimator.setDuration(3000);
//		 secondAnimator.start();

		// TranslateAnimation firstAnimation = new TranslateAnimation(
		// Animation.RELATIVE_TO_PARENT, 0.0f,
		// Animation.RELATIVE_TO_PARENT, 0.0f,
		// Animation.RELATIVE_TO_PARENT, 0.0f,
		// Animation.RELATIVE_TO_PARENT, -1.0f);
		// firstAnimation.setDuration(3000);
		// mFirstView.startAnimation(firstAnimation);
		// firstAnimation.setAnimationListener(new Animation.AnimationListener()
		// {
		// @Override
		// public void onAnimationStart(Animation animation) {
		//
		// }
		//
		// @Override
		// public void onAnimationEnd(Animation animation) {
		// mFirstView.setVisibility(View.GONE);
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animation animation) {
		//
		// }
		// });
		//
		// TranslateAnimation secondAnimation = new TranslateAnimation(
		// Animation.RELATIVE_TO_PARENT, 0.0f,
		// Animation.RELATIVE_TO_PARENT, 0.0f,
		// Animation.RELATIVE_TO_PARENT, 1.0f,
		// Animation.RELATIVE_TO_PARENT, 0.0f);
		// secondAnimation.setDuration(3000);
		// mSecondView.startAnimation(secondAnimation);
	}

}
