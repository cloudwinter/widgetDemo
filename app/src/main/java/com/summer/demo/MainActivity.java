package com.summer.demo;

import com.summer.demo.animation.ui.TweenAnimationActivity;
import com.summer.demo.annotation.ui.AnnotationActivity;
import com.summer.demo.banner.ui.BannerActivity;
import com.summer.demo.camera.ui.DecodeActivity;
import com.summer.demo.core.BaseActivity;
import com.summer.demo.crop.CropActivity;
import com.summer.demo.draw.ui.DrawActivity;
import com.summer.demo.http.OKHttpTestActivity;
import com.summer.demo.listview.ui.ListActivity;
import com.summer.demo.mock.SimpleListMock;
import com.summer.demo.mvp.view.MVPActivity;
import com.summer.demo.title.BaseTitleView;
import com.summer.demo.touch.TouchEventActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends BaseActivity {

	private BaseTitleView mTitleView;
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTitleView = (BaseTitleView) findViewById(R.id.title_view);
		mTitleView.setSimpleTitle("Practice");
		mListView = (ListView) findViewById(R.id.listview);
		ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.item_simple,
				SimpleListMock.mainSimpleDatas());
		mListView.setAdapter(arrayAdapter);
		mListView.setOnItemClickListener(mOnItemClick);
	}

	private AdapterView.OnItemClickListener mOnItemClick = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent();
			switch (position) {
			case 0:
				intent.setClass(MainActivity.this, DrawActivity.class);
				break;
			case 1:
				intent.setClass(MainActivity.this, CropActivity.class);
				break;
			case 2:
				intent.setClass(MainActivity.this, DecodeActivity.class);
				break;
			case 3:
				intent.setClass(MainActivity.this,
						TweenAnimationActivity.class);
				break;
			case 4:
				intent.setClass(MainActivity.this, ListActivity.class);
				break;
			case 5:
				intent.setClass(MainActivity.this, MVPActivity.class);
				break;
			case 6:
				intent.setClass(MainActivity.this, AnnotationActivity.class);
				break;
			case 7:
				intent.setClass(MainActivity.this, TouchEventActivity.class);
				break;
			case 8:
				intent.setClass(MainActivity.this, OKHttpTestActivity.class);
				break;
			case 9:
				intent.setClass(MainActivity.this, BannerActivity.class);
				break;
			default:
				break;
			}
			startActivity(intent);
		}
	};

}
