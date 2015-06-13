package com.yisi.yisiHome.baseFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.alibaba.fastjson.JSON;
import com.yisi.yisiHome.R;
import com.yisi.yisiHome.baseActivity.YisiApp;
import com.yisi.yisiHome.entity.EntityICCard;
import com.yisi.yisiHome.utils.Constants;
import com.yisi.yisiHome.utils.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class TakPicFragment extends BaseFragment implements OnClickListener{
	private static final int TAKE_PIC = 2589;
	private EntityICCard ic;
	private ImageView pic;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView=inflater.inflate(R.layout.activity_tak_pic_layout, container,false);
		rootView.findViewById(R.id.tak_pic).setOnClickListener(this);
		rootView.findViewById(R.id.send).setOnClickListener(this);
		rootView.findViewById(R.id.next).setOnClickListener(this);
		pic=(ImageView) rootView.findViewById(R.id.display);
		String icJson=getValue(mainActivity.getIntent(), "ic");
		if (!TextUtils.isEmpty(icJson)) {
			ic=JSON.parseObject(icJson,EntityICCard.class);
			if (ic!=null&&!TextUtils.isEmpty(ic.getImageName())) {
				pic.setImageBitmap(new FileUtils(mainActivity).getBitmap(ic.getImageName()));
			}
		}
		return rootView;
	}
	@Override
	public void onResult(Intent intent) {
		int requestCode=intent.getIntExtra(Constants.REQUEST_CODE, -1);
		int resultCode=intent.getIntExtra(Constants.RESULT_CODE, -1);
		if (requestCode==TAKE_PIC&&resultCode==Activity.RESULT_OK) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0-100)压缩文件
				//此处可以把Bitmap保存到sd卡中，具体请看：http://www.cnblogs.com/linjiqin/archive/2011/12/28/2304940.html
				pic.setVisibility(View.VISIBLE);
				pic.setImageBitmap(photo); //把图片显示在ImageView控件上//TODO save
			}
		}
		
		super.onResult(intent);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tak_pic:
			mainActivity.startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), TAKE_PIC);
			break;
		case R.id.next:
//			mainActivity.startActivity(new Intent(mainActivity, LocationActivity.class));
			if (TextUtils.isEmpty(ic.getImageName())) {
				YisiApp.tell(mainActivity, "请先拍照");
				return;
			}
			setValue(mainActivity.getIntent(), "ic", JSON.toJSONString(ic));
			
//			mainActivity.otherBackableFragment(new LocationFragment());
			break;
		case R.id.send:
			savePic();
			break;
		default:
			break;
		}
	}
	private void savePic() {
		ImageView iv=(ImageView) rootView.findViewById(R.id.display);
		Drawable drawable=iv.getDrawable();
		if (drawable==null) {
			Toast.makeText(mainActivity, "没找到相片", Toast.LENGTH_SHORT).show();
			return;
		}
		Bitmap image=((BitmapDrawable) drawable).getBitmap();
		File file=new File(mainActivity.getFilesDir()+File.separator+"image.jpeg");
		try {
			System.out.println("path:"+file.getPath()+"file:"+file.exists());
			if (!file.exists()) {
				File.createTempFile("yisi", System.currentTimeMillis()+"");
				ic.setImageName(file.getAbsolutePath());
//				file.createNewFile();
			}
			FileOutputStream fos=new FileOutputStream(file);
			image.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			//TODO 上传图片
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
