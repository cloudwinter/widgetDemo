package com.summer.demo.mock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiayundong on 2017/4/7.
 */

public class SimpleListMock {

	/**
	 * 主页数据
	 * 
	 * @return
	 */
	public static List<String> mainSimpleDatas() {
		List<String> array = new ArrayList<>();
		array.add("绘制");
		array.add("裁剪");
		array.add("拍照");
		return array;
	}
}
