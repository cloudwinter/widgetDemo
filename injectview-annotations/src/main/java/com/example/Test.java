package com.example;

/**
 * Created by xiayundong on 2017/11/16.
 */

public class Test {

	public static void main(String[] args) {
		String s2 = "abc,def,ghi,jkl";
		int pos = 0;
		int cnt2 = 0;
		while (pos < s2.length()) {
			pos = s2.indexOf(',', pos);
			if (pos == -1) {
				break;
			} else {
				cnt2++;
				System.out.println(pos);
				pos++;
			}
		}
		System.out.println("time:" + cnt2);
	}
}
