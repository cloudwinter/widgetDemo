package com.summer.demo.title;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;

/**
 * Created by xiayundong on 2017/4/11.
 */
public class BaseTitleView extends RelativeLayout {

	private Context mContext;

	private ImageView mLeftImg;
	private TextView mTitleTxt;
	private RelativeLayout mDefaultContainer;
	private FrameLayout mCustomContainer;

	public BaseTitleView(Context context) {
		super(context);
		init(context);
	}

	public BaseTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		LayoutInflater.from(getContext()).inflate(R.layout.view_base_title,
				this);
		mLeftImg = (ImageView) findViewById(R.id.left_img);
		mTitleTxt = (TextView) findViewById(R.id.title_txt);
		mDefaultContainer = (RelativeLayout) findViewById(
				R.id.default_container);
		mCustomContainer = (FrameLayout) findViewById(R.id.custom_container);
		mLeftImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Activity activity = (Activity) mContext;
				activity.finish();
			}
		});
	}

	/**
	 * 
	 * @param title
	 */
	public void setSimpleTitle(String title) {
		mLeftImg.setVisibility(View.GONE);
		mTitleTxt.setText(title);
	}

	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		mTitleTxt.setText(title);
	}

	public void setCustomTitle(View view) {
		mDefaultContainer.setVisibility(View.GONE);
		mCustomContainer.addView(view);
	}
}
