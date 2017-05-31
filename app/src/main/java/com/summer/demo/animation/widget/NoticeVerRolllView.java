package com.summer.demo.animation.widget;

import java.lang.ref.WeakReference;
import java.util.List;

import com.summer.demo.utils.ListUtil;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 纵向滚动的公告view<br>
 * Created by xiayundong on 2017/5/23.
 */

public class NoticeVerRolllView extends LinearLayout {

	/**
	 * 执行滚动msg
	 */
	private static final int MSG_START_ROLLING = 0x1;
	/**
	 * 删除上一个view
	 */
	private static final int MSG_REMOVE_PREVIEW = 0x2;

	private Context mContext;
	/**
	 * 滚动数据集合
	 */
	private List<String> mDatas;
	/**
	 * 当前显示数据在集合中的位置
	 */
	private int mCurPosition;

	private RollHandler mRollHandler;

	public NoticeVerRolllView(Context context) {
		super(context);
		init(context);
	}

	public NoticeVerRolllView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		setOrientation(LinearLayout.VERTICAL);
		mRollHandler = new RollHandler(this);
	}

	/**
	 * 设置默认的数据
	 * 
	 * @param content
	 */
	public void setDefault(String content) {
		removeAllViews();
		addView(generateItemView(content));
	}

	public void setDatas(List<String> contents) {
		if (ListUtil.isEmpty(contents)) {
			return;
		}
		mDatas = contents;
		removeAllViews();
		mCurPosition = 0;
		addItemView();
	}

	public void startRolling() {
		if (ListUtil.size(mDatas) <= 1) {
			return;
		}
		mRollHandler.removeMessages(MSG_START_ROLLING);
		mRollHandler.sendEmptyMessageDelayed(MSG_START_ROLLING, 3000);
	}

	public void stopRolling() {
		mRollHandler.removeMessages(MSG_START_ROLLING);
	}

	/**
	 * 生产itemView
	 *
	 * @param itemContent
	 * @return
	 */
	private View generateItemView(String itemContent) {
		TextView itemView = new TextView(mContext);
		itemView.setSingleLine();
		LinearLayout.LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		itemView.setLayoutParams(params);
		itemView.setText(itemContent);
		return itemView;
	}

	/**
	 * 添加一个item
	 */
	private void addItemView() {
		if (mCurPosition + 1 > mDatas.size()) {
			mCurPosition = 0;
		}
		View view = generateItemView(mDatas.get(mCurPosition));
		mCurPosition++;
		addView(view);
	}

	/**
	 * 删除上一个view
	 */
	private void removePreView() {
		removeViewAt(0);
	}

	/**
	 * 执行滚动
	 */
	private void executeRolling() {
		addItemView();
		// 获取第一个view
		View itemFirst = getChildAt(0);
		View itemNext = getChildAt(1);
		setAnimation(itemFirst, itemNext);
	}

	/**
	 * 设置动画
	 * 
	 * @param firstView
	 * @param nextView
	 */
	private void setAnimation(final View firstView, View nextView) {

		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(firstView,
				"TranslationY", 0.0f, -getHeight());
		objectAnimator.setDuration(3000);
		objectAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				firstView.clearAnimation();
				mRollHandler.sendEmptyMessage(MSG_REMOVE_PREVIEW);
				startRolling();
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});

		ObjectAnimator nextObjectAnimator = ObjectAnimator.ofFloat(nextView,
				"TranslationY", getHeight(), 0.0f);
		objectAnimator.start();
		nextObjectAnimator.start();

		// TranslateAnimation firstAnimation = new TranslateAnimation(
		// Animation.RELATIVE_TO_PARENT, 0.0f,
		// Animation.RELATIVE_TO_PARENT, 0.0f,
		// Animation.RELATIVE_TO_PARENT, 0.0f,
		// Animation.RELATIVE_TO_PARENT, -1.0f);
		// firstAnimation.setDuration(3000);
		// firstAnimation.setAnimationListener(new Animation.AnimationListener()
		// {
		// @Override
		// public void onAnimationStart(Animation animation) {
		// }
		//
		// @Override
		// public void onAnimationEnd(Animation animation) {
		// firstView.clearAnimation();
		// mRollHandler.sendEmptyMessage(MSG_REMOVE_PREVIEW);
		// startRolling();
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animation animation) {
		//
		// }
		// });
		// TranslateAnimation nextAnimation = new TranslateAnimation(
		// Animation.RELATIVE_TO_PARENT, 0.0f,
		// Animation.RELATIVE_TO_PARENT, 0.0f,
		// Animation.RELATIVE_TO_PARENT, 1.0f,
		// Animation.RELATIVE_TO_PARENT, 0.0f);
		// nextAnimation.setDuration(3000);
		//
		// firstView.startAnimation(firstAnimation);
		// nextView.startAnimation(nextAnimation);
	}

	private class RollHandler extends Handler {

		private WeakReference<NoticeVerRolllView> mWeakRollView;

		public RollHandler(NoticeVerRolllView RollView) {
			mWeakRollView = new WeakReference<NoticeVerRolllView>(RollView);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (mWeakRollView == null || mWeakRollView.get() == null) {
				return;
			}
			switch (msg.what) {
			case MSG_START_ROLLING:
				mWeakRollView.get().executeRolling();
				break;
			case MSG_REMOVE_PREVIEW:
				mWeakRollView.get().removePreView();
				break;
			}
		}
	}
}
