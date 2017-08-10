package com.summer.demo.annotation.ui;

import com.summer.demo.R;
import com.summer.demo.annotation.entity.Computer;
import com.summer.demo.core.BaseActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 注解的测试activity Created by xiayundong on 2017/7/12.
 */
public class AnnotationActivity extends BaseActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_annotation);
		Computer computer = new Computer();
	}
}
