package com.summer.demo.annotation.entity;

import com.example.annotation.CustomAnnotation;

/**
 * Created by xiayundong on 2017/7/12.
 */

public class Computer {

	@CustomAnnotation(getValue = "华为")
	public String type;

}
