package com.summer.demo.http.entity;

import java.io.Serializable;

/**
 * 广告item对象
 *
 * author mowei
 *
 * date 01/12/2016.
 */

public class ADInfo implements Serializable {

    /**
     * 广告名称，可能用于埋点
     */
    public String adName;

    /**
     * 广告图片地址
     */
    public String imageUrl;

    /**
     * 点击跳转地址
     */
    public String linkUrl;

    /**
     * 顺序编号
     */
    public int sort;

}
