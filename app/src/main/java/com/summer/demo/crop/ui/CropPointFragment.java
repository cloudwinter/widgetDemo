package com.summer.demo.crop.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.summer.demo.R;
import com.summer.demo.core.BaseFragment;
import com.summer.demo.crop.widget.croppoint.ImageState;
import com.summer.demo.crop.widget.croppoint.PolygonView;
import com.summer.demo.utils.UriUtil;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by xiayundong on 2017/4/11.
 */
public class CropPointFragment extends BaseFragment {

	private TextView mNextView;
	private FrameLayout mContentLayout;
	private ImageView mImgView;
	private PolygonView mPolugonView;
	private float mOriginalBitWidth;
	private float mOriginalBitHeight;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_crop_point, container,
				false);
		mContentLayout = (FrameLayout) view.findViewById(R.id.container_layout);
		mImgView = (ImageView) view.findViewById(R.id.photo_img);
		mPolugonView = (PolygonView) view.findViewById(R.id.polygon_view);
		mNextView = (TextView) view.findViewById(R.id.view_next);
		mContentLayout.post(new Runnable() {
			@Override
			public void run() {
				Bitmap bitmap = getBitmap();
				if (bitmap != null) {
					setImgBitmap(bitmap);
				}
			}
		});
		return view;
	}

	private Bitmap getBitmap() {
		Uri uri = getArguments().getParcelable(CropFragment.EXTRA_URI_KEY);
		try {
			Bitmap bitmap = UriUtil.getBitmap(getActivity(), uri);
			getActivity().getContentResolver().delete(uri, null, null);
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 * @param original
	 */
	private void setImgBitmap(Bitmap original) {
		Bitmap scaledBitmap = scaledBitmap(original, mContentLayout.getWidth(),
				mContentLayout.getHeight());
		mImgView.setImageBitmap(scaledBitmap);
		mPolugonView.setMapPoints(generatePoints(mImgView), mOriginalBitWidth,
				mOriginalBitHeight);
		mPolugonView.setVisibility(View.VISIBLE);

	}

	private Bitmap scaledBitmap(Bitmap bitmap, int width, int height) {
		Matrix m = new Matrix();
		m.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()),
				new RectF(0, 0, width, height), Matrix.ScaleToFit.CENTER);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), m, true);
	}

	private List<PointF> generatePoints(ImageView img) {
		ImageState imageState = generateImageState(img);
		List<PointF> pointFs = new ArrayList<>();
		PointF pointF1 = new PointF(imageState.getLeft(), imageState.getTop());
		PointF pointF2 = new PointF(imageState.getRight(), imageState.getTop());
		PointF pointF3 = new PointF(imageState.getLeft(),
				imageState.getBottom());
		PointF pointF4 = new PointF(imageState.getRight(),
				imageState.getBottom());
		pointFs.add(pointF1);
		pointFs.add(pointF2);
		pointFs.add(pointF3);
		pointFs.add(pointF4);
		return pointFs;
	}

	private ImageState generateImageState(ImageView img) {
		Matrix matrix = img.getImageMatrix();
		Rect rect = img.getDrawable().getBounds();
		float[] values = new float[9];
		matrix.getValues(values);
		ImageState mapState = new ImageState();
		mapState.setLeft(values[2]);
		mapState.setTop(values[5]);
		mapState.setRight(mapState.getLeft() + rect.width() * values[0]);
		mapState.setBottom(mapState.getTop() + rect.height() * values[0]);
		mOriginalBitWidth = mapState.getRight() - mapState.getLeft();
		mOriginalBitHeight = mapState.getBottom() - mapState.getTop();
		return mapState;
	}

}
