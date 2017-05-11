package com.summer.demo.crop.model;

import java.io.Serializable;

/**
 * Created by Oleksii Shliama [https://github.com/shliama] on 6/21/16.
 */
public class ExifInfo implements Serializable{

	private int mExifOrientation;
	private int mExifDegrees;
	private int mExifTranslation;

	public String test;

	public ExifInfo(int exifOrientation, int exifDegrees, int exifTranslation) {
		mExifOrientation = exifOrientation;
		mExifDegrees = exifDegrees;
		mExifTranslation = exifTranslation;
	}

	public int getExifDegrees() {
		return mExifDegrees;
	}

	public int getExifTranslation() {
		return mExifTranslation;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ExifInfo exifInfo = (ExifInfo) o;

		if (mExifOrientation != exifInfo.mExifOrientation)
			return false;
		if (mExifDegrees != exifInfo.mExifDegrees)
			return false;
		return mExifTranslation == exifInfo.mExifTranslation;

	}

	@Override
	public int hashCode() {
		int result = mExifOrientation;
		result = 31 * result + mExifDegrees;
		result = 31 * result + mExifTranslation;
		return result;
	}

}
