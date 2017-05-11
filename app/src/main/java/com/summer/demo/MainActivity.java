package com.summer.demo;

import com.summer.demo.core.BaseActivity;
import com.summer.demo.crop.CropActivity;
import com.summer.demo.draw.ui.DrawActivity;
import com.summer.demo.mock.SimpleListMock;
import com.summer.demo.title.BaseTitleView;

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
			default:
				break;
			}
			startActivity(intent);
		}
	};

}
