package com.yisi.yisiHome.baseActivity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.yisi.yisiHome.BuildConfig;
import com.yisi.yisiHome.utils.Constants;
import com.yisi.yisiHome.utils.CrashHandler;

public class YisiApp extends Application {
	private static YisiApp app;
	private static SharedPreferences sp;
	private static ProgressDialog progress;
	public static Map<String, Object> temp = new HashMap<String, Object>();

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		SDKInitializer.initialize(this);
		app = this;
		sp = app.getSharedPreferences(app.getPackageName(),
				Context.MODE_PRIVATE);
		initCrash();
	}
	//开启错误日志
	private void initCrash() {
		CrashHandler.getInstance().init(this).initSend(Constants.URL_CRASH_SEND	, "crashFile").sendPreviousReportsToServer();
	}

	public static YisiApp getInstance() {
		return app;
	}

	public static void setValue(String name, String value) {
		Editor edit = sp.edit();
		edit.putString(name, value);
		edit.commit();
	}

	public static void e(Object obj, String msg) {
		if (BuildConfig.DEBUG) {
			Log.e(obj.getClass().getSimpleName(), msg);
		}
	}

	public static void i(Object obj, String msg) {
		if (BuildConfig.DEBUG) {
			Log.i(obj.getClass().getSimpleName(), msg);
		}
	}

	public static void w(Object obj, String msg) {
		if (BuildConfig.DEBUG) {
			Log.w(obj.getClass().getSimpleName(), msg);
		}
	}

	public static String getValue(String name, String defaultValue) {
		return sp.getString(name, defaultValue);
	}

	public static void tell(final Context context, final String msg) {
		if (context instanceof Activity) {
			((Activity) context).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				}
			});
		} else {
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		}
	}

	public static boolean isProgressDialogShowing(){
		return progress!=null&&progress.isShowing();
	}
	public static void showProgressDialog(Context context, String title,
			String msg) {
		try {
			disMissProgressDialog();
			progress = new ProgressDialog(context);
			progress.setTitle(title);
			progress.setMessage(msg);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setCancelable(false);
			progress.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void disMissProgressDialog() {
		try {
			if (progress != null && progress.isShowing()) {
				progress.dismiss();
				progress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
