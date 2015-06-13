package com.yisi.yisiHome.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtils {
	// 旋转图片
		public static Bitmap rotaingImageView(Bitmap bitmap,int angle) {
			Matrix matrix = new Matrix();
			// 旋转图片 动作
			matrix.postRotate(angle);
			// 创建新的图片
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			return resizedBitmap;
		}
}
