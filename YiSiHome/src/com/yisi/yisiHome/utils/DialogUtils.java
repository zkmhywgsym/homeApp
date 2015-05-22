package com.yisi.yisiHome.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class DialogUtils {
	public static final int WRAP_CONTENT=LayoutParams.WRAP_CONTENT;
	public static final int MATCH_PARENT=LayoutParams.MATCH_PARENT;
	private static DialogUtils dialogUtil;
	private DialogUtils(){}
	public static DialogUtils getInstance(){
		if (dialogUtil==null) {
			dialogUtil=new DialogUtils();
		}
		return dialogUtil;
	}
	public interface InitView{
		public void init(Window win);
	}
	public Dialog showDialog(Context context,int w,int h,int rid,InitView callBack){
		
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.show();
		Window win = dialog.getWindow();
		LayoutParams lp=win.getAttributes();
		lp.width=w;
		lp.height=h;
		win.setAttributes(lp);
		win.setContentView(rid);
		callBack.init(win);
		return dialog;
	}
	public void dismissDialog(Dialog dialog){
		if (dialog!=null&&dialog.isShowing()) {
			dialog.dismiss();
		}
	}
}
