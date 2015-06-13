package com.yisi.yisiHome.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

public class FileUtils {
private Context context;
	
	public FileUtils(Context context) {
		super();
		this.context = context;
	}
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
	public void playSound(int rawRid){

		SoundPool soundPool= new SoundPool(10,AudioManager.STREAM_SYSTEM,5);

		soundPool.load(context,rawRid,1);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				soundPool.play(1,0.1f, 0.1f, 0, 0, 1);
			}
		});

	}
}
