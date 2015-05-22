package com.yisi.yisiHome.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class MD5Utils {
	//md5加密
	public static String Md5(String plainText) {
		String res="";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			res=buf.toString().substring(8,24).toUpperCase(Locale.CHINA);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return res;
	}
}
