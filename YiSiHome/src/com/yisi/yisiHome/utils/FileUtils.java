package com.yisi.yisiHome.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FileUtils {
	public Bitmap getBitmap(String path){
		Bitmap bitmap=null;
		try {
			return BitmapFactory.decodeFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	public String readTextFile(File file) {
		try {
			FileReader fr=new FileReader(file);
			BufferedReader br=new BufferedReader(fr);
			StringBuffer sb=new StringBuffer();
			String line;
			while ((line=br.readLine())!=null) {
				sb.append(line);
			}
			br.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		
	}
}
