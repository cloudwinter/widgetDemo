package com.summer.demo.banner.ui;

import com.summer.demo.R;
import com.summer.demo.banner.widget.BannerView;
import com.summer.demo.core.BaseActivity;

import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮播广告的activity<br>
 * Created by xiayundong on 2017/10/26.
 */

public class BannerActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_banner);
		BannerView bannerView = (BannerView) findViewById(R.id.banner_view);
		bannerView.setData(buildImageList());
	}

	private List<ImageView> buildImageList() {
		List<ImageView> imgViewList = new ArrayList<>();
		ImageView imageView1 = new ImageView(BannerActivity.this);
		ImageView imageView2 = new ImageView(BannerActivity.this);
		ImageView imageView3 = new ImageView(BannerActivity.this);
		ImageView imageView4 = new ImageView(BannerActivity.this);
		imageView1.setImageResource(R.mipmap.banner_1);
		imageView2.setImageResource(R.mipmap.banner_2);
		imageView3.setImageResource(R.mipmap.banner_3);
		imageView4.setImageResource(R.mipmap.banner_4);
		imgViewList.add(imageView1);
		imgViewList.add(imageView2);
		imgViewList.add(imageView3);
		imgViewList.add(imageView4);
		return imgViewList;
	}
}
