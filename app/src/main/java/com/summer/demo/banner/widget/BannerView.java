package com.summer.demo.banner.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.utils.ListUtil;
import com.summer.demo.widget.FixRatioRelativeLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 循环滚动的bannerview<br>
 * Created by xiayundong on 2017/10/26.
 */

public class BannerView extends FixRatioRelativeLayout {

	public static final int MSG_AUTO_SCORLL = 107;
	public static final long DURATION_SCORLL = 2000;

	private Context mContext;

	private CircleViewPager mViewPager;
	private LinearLayout mBottomContainer;

	private List<ImageView> mImageViewList = new ArrayList<>();
	private BannerPagerAdapter mPagerAdapter;

	private BannerHandler mBannerHandler;

	private int mCurrentPosition;

	public BannerView(Context context) {
		super(context);
		init(context);
	}

	public BannerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.view_banner, this);
		mViewPager = (CircleViewPager) findViewById(R.id.view_pager_circle);
		mViewPager.addOnPageChangeListener(mPageChangeListener);
		setScrollerDuration();
		mBottomContainer = (LinearLayout) findViewById(
				R.id.layout_bottom_container);
		mPagerAdapter = new BannerPagerAdapter();
		mViewPager.setAdapter(mPagerAdapter);
		mBannerHandler = new BannerHandler();

	}

	private void setScrollerDuration() {
		try {
			Field field = ViewPager.class.getDeclaredField("mScroller");
			field.setAccessible(true);
			CustomScroller scroller = new CustomScroller(mContext);
			scroller.setmDuration(2000);
			field.set(mViewPager, scroller);
		} catch (NoSuchFieldException e) {

		} catch (IllegalAccessException e) {

		}
	}

	/**
	 * 初始化数据
	 *
	 * @param imageViewList
	 */
	public void setData(List<ImageView> imageViewList) {
		if (ListUtil.isEmpty(imageViewList)) {
			return;
		}
		mImageViewList.clear();
		mImageViewList.addAll(imageViewList);
		buildCirclePoint();
		mPagerAdapter.notifyDataSetChanged();
		int position = Integer.MAX_VALUE / ListUtil.size(imageViewList);
		switch (position % ListUtil.size(imageViewList)) {
		case 1:
			position = position - 1;
			break;
		case 2:
			position = position - 2;
			break;
		case 3:
			position = position - 3;
			break;
		default:
			break;
		}
		mCurrentPosition = position;
		mViewPager.setCurrentItem(position);
		mBannerHandler.sendEmptyMessageDelayed(MSG_AUTO_SCORLL,
				DURATION_SCORLL);
	}

	/**
	 * 创建小圆点
	 */
	private void buildCirclePoint() {
		for (int i = 0; i < mImageViewList.size(); i++) {
			ImageView view = new ImageView(mContext);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					40, 40);
			view.setLayoutParams(layoutParams);
			layoutParams.leftMargin = 40;
			view.setBackgroundResource(R.drawable.bg_banner_circle);
			if (i == 0) {
				view.setSelected(true);
			} else {
				view.setSelected(false);
			}
			// // TODO: 2017/11/1 可以做配置
			mBottomContainer.addView(view);
		}
	}

	/**
	 * 设置原点选中效果
	 *
	 * @param position
	 */
	private void setCircleSelected(int position) {
		for (int i = 0; i < mBottomContainer.getChildCount(); i++) {
			if (position == i) {
				mBottomContainer.getChildAt(i).setSelected(true);
			} else {
				mBottomContainer.getChildAt(i).setSelected(false);
			}
		}
	}

	/**
	 * page切换监听
	 */
	private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			mCurrentPosition = position;
			position = Math.abs(position % ListUtil.size(mImageViewList));
			setCircleSelected(position);
			mBannerHandler.removeMessages(MSG_AUTO_SCORLL);
			mBannerHandler.sendEmptyMessageDelayed(MSG_AUTO_SCORLL,
					DURATION_SCORLL);
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	};

	private class BannerPagerAdapter extends PagerAdapter {

		public BannerPagerAdapter() {
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			position = Math.abs(position % ListUtil.size(mImageViewList));
			View view = mImageViewList.get(position);
			// 如果当期的view已经有父类了，先移除再加入
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeView(view);
			}
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position,
				Object object) {
			container.removeView((View) object);
		}
	}

	private class BannerHandler extends Handler {

		public BannerHandler() {

		}

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_AUTO_SCORLL) {
				mViewPager.setCurrentItem(mCurrentPosition + 1);
			}
		}
	}
}
