package com.summer.demo.crop.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.summer.demo.R;
import com.summer.demo.core.BaseFragment;
import com.summer.demo.utils.UriUtil;

import java.io.IOException;

/**
 * Created by xiayundong on 2017/4/11.
 */

public class CropFragment extends BaseFragment {

	public static final int ALBUM_REQUEST_CODE = 1;
	public static final String EXTRA_URI_KEY = "uri_key";

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_crop, container, false);
		Button button = (Button) view.findViewById(R.id.but_album);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startAlbum();
			}
		});
		return view;
	}

	private void startAlbum() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent, ALBUM_REQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == ALBUM_REQUEST_CODE) {
				CropImageFragment fragment = new CropImageFragment();
				Bundle bundle = new Bundle();
				bundle.putParcelable(EXTRA_URI_KEY, data.getData());
				fragment.setArguments(bundle);
				mActivity.startFirstFragment(fragment);
			}
		}
	}

	/**
	 * 压缩三倍
	 * 
	 * @param selectedimg
	 * @return
	 * @throws IOException
	 */
	private Bitmap compressBitmap(Uri selectedimg) throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 3;
		AssetFileDescriptor fileDescriptor = null;
		fileDescriptor = getActivity().getContentResolver()
				.openAssetFileDescriptor(selectedimg, "r");
		Bitmap original = BitmapFactory.decodeFileDescriptor(
				fileDescriptor.getFileDescriptor(), null, options);
		return original;
	}

}
