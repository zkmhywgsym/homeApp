package com.yisi.yisiHome.utils;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.widget.TextView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.SMSReceiver;

public class AndroidUtils {

	// READ_PHONE_STATE 获取手机号(有的手机获取不到)
	public static String getPhoneNum(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String num = tm.getLine1Number();
		num = (TextUtils.isEmpty(num) ? "" : num);
		return num;
	}


	public static Point getDisplay(Activity activity) {
		Point size = new Point();
		Display display = activity.getWindowManager().getDefaultDisplay();
		display.getSize(size);
		return size;
	}
	/** 取得版本号*/
    public static String getVersion(Context context) {
		try {
			PackageInfo manager = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return manager.versionName;
		} catch (NameNotFoundException e) {
			return "Unknown";
		}
	}
    /**the IMEI for GSM and the MEID or ESN for CDMA phones*/
    public static String getDeviceId(Context context){
    	TelephonyManager tm=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    	return tm.getDeviceId();
    }

}
