package com.summer.demo.http.entity;

import java.io.Serializable;
import java.util.List;

/**
 * banner广告列表对象
 *
 * author mowei
 *
 * date 01/12/2016.
 */

public class ADListInfo implements Serializable {

	/**
	 * 自动轮播顺序
	 */
	public int playingOrder;

	/**
	 * 轮播间隔，单位为s
	 */
	public int timing;

	/**
	 * 广告列表
	 */
	public List<ADInfo> list;

	/**
	 * 获取滑动间隔
	 * 
	 * @return
	 */
	public long getScrollInterval() {
		return timing * 1000L;
	}

}
