package com.yisi.yisiHome.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
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
		public void init(Window win,Dialog dialog);
	}
	/**w和h都是0－1的数值*/
	public Dialog showCustomizedDialog(Activity context,double d,double e,int rid,InitView callBack){
		DisplayMetrics outMetrics=new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		return showDialog(context, (int)(outMetrics.widthPixels*d), (int)(outMetrics.heightPixels*e), rid, callBack);
	}
	public Dialog showDialog(Context context,int w,int h,int rid,InitView callBack){
		
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		Window win = dialog.getWindow();
		LayoutParams lp=win.getAttributes();
		lp.width=w;
		lp.height=h;
		win.setAttributes(lp);
		win.setContentView(rid);
		callBack.init(win,dialog);
		return dialog;
	}
	public void dismissDialog(Dialog dialog){
		if (dialog!=null&&dialog.isShowing()) {
			dialog.dismiss();
		}
	}
}
