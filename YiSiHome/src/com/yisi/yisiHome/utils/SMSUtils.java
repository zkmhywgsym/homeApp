package com.yisi.yisiHome.utils;

import android.app.Activity;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.widget.TextView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.SMSReceiver;

public class SMSUtils {
	// 填写从短信SDK应用后台注册得到的APPKEY
	private static String APPKEY = "6f994a4ba6c4";

	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "4b6c1ab99ad94a09ebad28b1f79c0487";

	// 获取验证码
	public static void getCheckCode(Activity activity, String nickName,
			String avatar, String country, String phone, EventHandler callBack) {
		SMSSDK.initSDK(activity, APPKEY, APPSECRET);
		// SMSSDK.submitUserInfo(new Random().nextInt(100)+"", nickName, "abc",
		// TextUtils.isEmpty(country)?"+86":country, phone);
		country = TextUtils.isEmpty(country) ? "86"
				: country.startsWith("+") ? country.substring(1) : country;
		SMSSDK.getVerificationCode(country, phone);
		// SMSSDK.getSupportedCountries();
		SMSSDK.registerEventHandler(callBack);

	}

	private static SMSReceiver smsReceiver;
	// 监听收到的短信，自动填写验证码
	// <uses-permission android:name="android.permission.RECEIVE_SMS"/>
	public static void getSMSmsg(final Activity activity,
			final TextView textView) {
		if (smsReceiver!=null) {
			return;
		}
		smsReceiver = new SMSReceiver(
				new SMSSDK.VerifyCodeReadListener() {
					@Override
					public void onReadVerifyCode(final String verifyCode) {
						textView.setText(verifyCode);
						if (smsReceiver!=null) {
							activity.unregisterReceiver(smsReceiver);
							smsReceiver=null;
						}
					}
				});
		try {
			activity.registerReceiver(smsReceiver, new IntentFilter(
					"android.provider.Telephony.SMS_RECEIVED"));
		} catch (Exception e) {
		}
	}

	// 提交验证码，查看验证有效性
	public static void submitVerifyCode(Activity activity, String phone,
			String verifyCode, final EventHandler callBack) {
		SMSSDK.initSDK(activity, APPKEY, APPSECRET);
		SMSSDK.registerEventHandler(new EventHandler(){
			@Override
			public void afterEvent(int arg0, int arg1, Object arg2) {
				callBack.afterEvent(arg0, arg1, arg2);
				
				super.afterEvent(arg0, arg1, arg2);
			}
		});
		SMSSDK.submitVerificationCode("86", phone, verifyCode);
	}


}
