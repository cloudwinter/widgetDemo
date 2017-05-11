package com.summer.demo.draw.ui;

import com.summer.demo.R;
import com.summer.demo.core.BaseActivity;
import com.summer.demo.draw.widget.DrawView;
import com.summer.demo.title.BaseTitleView;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by xiayundong on 2017/4/7.
 */

public class DrawActivity extends BaseActivity {

	private DrawView mDrawView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw);
		BaseTitleView titleView = (BaseTitleView) findViewById(R.id.title_view);
		titleView.setTitle("draw");
		mDrawView = (DrawView) findViewById(R.id.draw_view);
	}
}
