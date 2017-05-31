package com.summer.demo.listview.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.summer.demo.R;
import com.summer.demo.listview.widget.HorizontalListView;

public class ListActivity extends Activity {

	private HorizontalListView mHlvSimpleList;
	private HorizontalListView mHlvCustomList;
	private HorizontalListView mHlvCustomListWithDividerAndFadingEdge;

	private String[] mSimpleListValues = new String[] { "Android", "iPhone",
			"WindowsMobile", "Blackberry", "WebOS", "Ubuntu", "Windows7",
			"Max OS X", "Linux", "OS/2" };

	private CustomData[] mCustomData = new CustomData[] {
			new CustomData(Color.RED, "Red"),
			new CustomData(Color.DKGRAY, "Dark Gray"),
			new CustomData(Color.GREEN, "Green"),
			new CustomData(Color.LTGRAY, "Light Gray"),
			new CustomData(Color.WHITE, "White"),
			new CustomData(Color.RED, "Red"),
			new CustomData(Color.BLACK, "Black"),
			new CustomData(Color.CYAN, "Cyan"),
			new CustomData(Color.DKGRAY, "Dark Gray"),
			new CustomData(Color.GREEN, "Green"),
			new CustomData(Color.RED, "Red"),
			new CustomData(Color.LTGRAY, "Light Gray"),
			new CustomData(Color.WHITE, "White"),
			new CustomData(Color.BLACK, "Black"),
			new CustomData(Color.CYAN, "Cyan"),
			new CustomData(Color.DKGRAY, "Dark Gray"),
			new CustomData(Color.GREEN, "Green"),
			new CustomData(Color.LTGRAY, "Light Gray"),
			new CustomData(Color.RED, "Red"),
			new CustomData(Color.WHITE, "White"),
			new CustomData(Color.DKGRAY, "Dark Gray"),
			new CustomData(Color.GREEN, "Green"),
			new CustomData(Color.LTGRAY, "Light Gray"),
			new CustomData(Color.WHITE, "White"),
			new CustomData(Color.RED, "Red"),
			new CustomData(Color.BLACK, "Black"),
			new CustomData(Color.CYAN, "Cyan"),
			new CustomData(Color.DKGRAY, "Dark Gray"),
			new CustomData(Color.GREEN, "Green"),
			new CustomData(Color.LTGRAY, "Light Gray"),
			new CustomData(Color.RED, "Red"),
			new CustomData(Color.WHITE, "White"),
			new CustomData(Color.BLACK, "Black"),
			new CustomData(Color.CYAN, "Cyan"),
			new CustomData(Color.DKGRAY, "Dark Gray"),
			new CustomData(Color.GREEN, "Green"),
			new CustomData(Color.LTGRAY, "Light Gray") };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		// Get references to UI widgets
		mHlvSimpleList = (HorizontalListView) findViewById(R.id.hlvSimpleList);
		mHlvCustomList = (HorizontalListView) findViewById(R.id.hlvCustomList);
		mHlvCustomListWithDividerAndFadingEdge = (HorizontalListView) findViewById(
				R.id.hlvCustomListWithDividerAndFadingEdge);

		setupSimpleList();
		setupCustomLists();
	}

	private void setupSimpleList() {
		// Make an array adapter using the built in android layout to render a
		// list of strings
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_2, android.R.id.text1,
				mSimpleListValues);

		// Assign adapter to the HorizontalListView
		mHlvSimpleList.setAdapter(adapter);
	}

	private void setupCustomLists() {
		// Make an array adapter using the built in android layout to render a
		// list of strings
		CustomArrayAdapter adapter = new CustomArrayAdapter(this, mCustomData);

		// Assign adapter to HorizontalListView
		mHlvCustomList.setAdapter(adapter);
		mHlvCustomList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Toast.makeText(ListActivity.this,
								mCustomData[position].getText(),
								Toast.LENGTH_SHORT).show();
					}
				});
		mHlvCustomListWithDividerAndFadingEdge.setAdapter(adapter);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.activity_main, menu);
	// return true;
	// }
}
