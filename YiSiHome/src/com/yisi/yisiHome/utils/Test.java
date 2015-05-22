package com.yisi.yisiHome.utils;

import java.nio.charset.Charset;

public class Test {
	static byte[] bytes1,byte2;
	public static void main(String[] args) {
		bytes1=new byte[]{0, 0, 0, 0, 0, 0, -1, 7, -128, 0, 32, 16, 3, 34, 0, 0};
		byte2=new byte[]{-73, -27, -47, -46, -68, -81, -51, -59, -74, -2, -60, -74, -71, -75, -61, -70};
		testStr(bytes1);
		testStr(byte2);
	}
	private static void testStr(byte[] bytes){
		try {
			
			System.out.println("GB2312"+new String(bytes,"GB2312"));
			System.out.println("utf-8"+new String(bytes,"utf-8"));
			System.out.println("gbk"+new String(bytes,"gbk"));
		} catch (Exception e) {
		}
	}
}
