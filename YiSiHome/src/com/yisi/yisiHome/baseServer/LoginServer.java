package com.yisi.yisiHome.baseServer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.yisi.yisiHome.R;
import com.yisi.yisiHome.baseActivity.LoginActivity;
import com.yisi.yisiHome.baseActivity.MainActivity;
import com.yisi.yisiHome.baseActivity.YisiApp;
import com.yisi.yisiHome.baseEntity.EntityUser;
import com.yisi.yisiHome.utils.AESHelper;
import com.yisi.yisiHome.utils.AndroidUtils;
import com.yisi.yisiHome.utils.Constants;
import com.yisi.yisiHome.utils.DialogUtils;
import com.yisi.yisiHome.utils.MD5Utils;
import com.yisi.yisiHome.utils.SMSUtils;
import com.yisi.yisiHome.utils.WebHelper;
import com.yisi.yisiHome.utils.DialogUtils.InitView;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class LoginServer {
	private Activity activity;
	private EditText checkCode;
	private EditText contacts;
	private Dialog dialog;
	private int flag = -1;
	private EditText pwd;
	private EditText pwdConfirm;

	public LoginServer(Activity activity) {
		this.activity = activity;
	}

	public void login(String name, String pwd, boolean remember, boolean auto) {
		if ((TextUtils.isEmpty(name)) || (TextUtils.isEmpty(pwd))) {
			return;
		}
		String pwdMd5 = MD5Utils.Md5(pwd);
		if (loginLocal(name, pwdMd5)) {
			loginSuccess(name,pwd,remember,auto);
			return;
		}
		loginWeb(name.trim(), pwd, remember, auto);
	}

	public void register() {
		if ((dialog != null) && (dialog.isShowing())) {
			return;
		}
		flag = 0;
		dialog = DialogUtils.getInstance().showDialog(activity,
				DialogUtils.WRAP_CONTENT, DialogUtils.WRAP_CONTENT,
				R.layout.frame_regist_layout, new DialogUtils.InitView() {
					public void init(Window win) {
						((TextView) win.findViewById(R.id.titleText))
								.setText("用户注册");
						checkCode = ((EditText) win
								.findViewById(R.id.check_code_value));
						pwd = ((EditText) win.findViewById(R.id.user_pwd_value));
						pwdConfirm = ((EditText) win
								.findViewById(R.id.user_pwd_value2));
						contacts = ((EditText) win
								.findViewById(R.id.contacts_value));
						Button confirmBtn = (Button) win
								.findViewById(R.id.confirm_button);
						win.findViewById(R.id.check_code_button)
								.setOnClickListener(new MyClick());
						confirmBtn.setOnClickListener(new MyClick());
						confirmBtn.setText("注册用户");
//						SMSUtils.getSMSmsg(activity, checkCode);
						contacts.setText(AndroidUtils.getPhoneNum(activity));
						contacts.setEnabled(true);
						pwd.setEnabled(true);
//						pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//						pwdConfirm
//								.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					}
				});
	}

	public void resetPwd() {
		if ((dialog != null) && (dialog.isShowing())){
			return;
		}
		flag = 1;
		dialog = DialogUtils.getInstance().showDialog(activity,
				DialogUtils.WRAP_CONTENT, DialogUtils.WRAP_CONTENT,
				R.layout.frame_reset_pwd_layout, new DialogUtils.InitView() {
					public void init(Window win) {
						((TextView) win.findViewById(R.id.titleText))
								.setText("重置密码");
						checkCode = ((EditText) win
								.findViewById(R.id.check_code_value));
						pwd = ((EditText) win.findViewById(R.id.user_pwd_value));
						pwdConfirm = ((EditText) win
								.findViewById(R.id.user_pwd_value2));
						contacts = ((EditText) win
								.findViewById(R.id.login_name_value));
						Button confirmBtn = (Button) win
								.findViewById(R.id.confirm_button);
						win.findViewById(R.id.check_code_button)
								.setOnClickListener(new MyClick());
						confirmBtn.setOnClickListener(new MyClick());
//						SMSUtils.getSMSmsg(activity, checkCode);
						confirmBtn.setText("确认重置");
						pwd.setEnabled(true);
//						pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
						contacts.setEnabled(true);
						contacts.setText(AndroidUtils.getPhoneNum(activity));
					}
				});
	}

	// /*************************************************************
	private void toRegist() {
		final String deviceId=AndroidUtils
				.getDeviceId(activity);
		new AsyncTask<Void, Void, String>() {
			protected void onPreExecute() {
				YisiApp.showProgressDialog(activity, "加载中……", "正在加载中……");
				super.onPreExecute();
			}

			protected String doInBackground(Void... ps) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("loginName", contacts
						.getText().toString()));
				params.add(new BasicNameValuePair("loginPwd", MD5Utils
						.Md5(pwd.getText().toString())));
//				params.add(new BasicNameValuePair("device", deviceId));
				return new WebHelper().getResult(Constants.URL_REGISTER_USER,
						params);
			}

			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				YisiApp.disMissProgressDialog();
				if (result!=null&&result.contains("true")) {
					YisiApp.tell(activity, "注册成功");
					flag = -1;
					if (dialog != null) {
						dialog.dismiss();
						dialog = null;
					}
				} else if (result!=null&&result.contains("exists")) {
					YisiApp.tell(activity, "注册名已存在");
					contacts.requestFocus();
				} else {
					YisiApp.tell(activity, "注册失败");
				}
			}

		}.execute();
	}
	private void toResetPwd() {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				WebHelper webHelper = new WebHelper(String.class);
				List<NameValuePair> ps = new ArrayList<NameValuePair>();
				ps.add(new BasicNameValuePair("loginPwd", MD5Utils.Md5(pwd
						.getText().toString())));
				ps.add(new BasicNameValuePair("loginName", contacts.getText()
						.toString()));
				return webHelper.getResult(Constants.URL_RESET, ps);
			}

			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				YisiApp.disMissProgressDialog();
				if (result != null && result.contains("true")) {
					flag = -1;
					YisiApp.tell(activity, "重置成功");
					if (dialog != null) {
						dialog.dismiss();
						dialog = null;
					}
				} else {

					YisiApp.tell(activity, "重置失败");
				}
			}

			protected void onPreExecute() {
				YisiApp.showProgressDialog(activity, "加载中……", "正在加载中……");
				super.onPreExecute();
			}

		}.execute();
	}

	private void getCheckCode() {
		if ((!checkInfo(contacts))
				|| (!contacts.getText().toString()
						.matches("^1[3|4|5|8][0-9]\\d{8}$"))) {
			YisiApp.tell(activity, "请输入手机号");
			contacts.requestFocus();
		} else {
			YisiApp.showProgressDialog(activity, "请稍候", "加载中……");
			SMSUtils.getCheckCode(activity, "dear", "", "", contacts.getText()
					.toString(), new EventHandler() {
				public void afterEvent(int event, final int result,
						final Object data) {
					getSMSCodeSuccess(result, data);
					SMSSDK.unregisterAllEventHandler();
					super.afterEvent(event, result, data);
				}
			});
		}
	}

	private void getSMSCodeSuccess(int result, Object data) {
		YisiApp.disMissProgressDialog();
		if (result == SMSSDK.RESULT_COMPLETE) {
			YisiApp.i(this, "已请求验证码");
			YisiApp.tell(activity, "已请求验证码");
			SMSUtils.getSMSmsg(activity, checkCode);
		} else {
			YisiApp.e(this, "获取验证码失败：" + data.toString());
			YisiApp.tell(activity, "获取验证码失败");
		}
	}

	private boolean loginLocal(String user, String pwd) {
		try {
			return new UserServer(activity).checkUser(new EntityUser(0, user,
					pwd, 0, 0L));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void loginSuccess(String name, String pwd, boolean remember,
			boolean auto) {
		saveValue(name, AESHelper.encrypt(pwd), remember, auto);
		Intent intent = new Intent(activity, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(intent);
		activity.finish();
	}

	private void loginWeb(final String user, final String pwd,
			final boolean remember, final boolean auto) {
		final String pwdMd5 = MD5Utils.Md5(pwd);
		new AsyncTask<Void, Void, String>() {
			protected void onPreExecute() {
				YisiApp.showProgressDialog(activity, "登陆中……", "正在登陆中，请稍候……");
				super.onPreExecute();
			}

			protected String doInBackground(Void... ps) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("loginName", user));
				params.add(new BasicNameValuePair("loginPwd", pwdMd5));
				return new WebHelper().getResult(Constants.URL_LOGIN, params);
			}

			protected void onPostExecute(String result) {
				YisiApp.disMissProgressDialog();
				super.onPostExecute(result);
				if ((result == null) || ("".equals(result.trim()))) {
					YisiApp.tell(activity, "连接失败");
				} else {

					if (result.contains("success")){
						loginSuccess(user, pwd, remember, auto);
					}
					else if (result.contains("other")) {
						YisiApp.tell(activity, "未知错误");
					} else {
						YisiApp.tell(activity, "用户名/密码错误");
					}
				}
			}

		}.execute();
	}

	private void saveValue(String name, String pwd, boolean remember,
			boolean auto) {
		YisiApp.setValue("rememberPwd", remember+"");
		YisiApp.setValue("autoLogin", auto + "");
		if (auto) {
			YisiApp.setValue("loginAccount", name.trim());
			YisiApp.setValue("loginPwd", pwd.trim());
		} else {
			YisiApp.setValue("loginAccount", "");
			YisiApp.setValue("loginPwd", "");
		}
	}

	private void submitVerifyCode(final Runnable callBack) {
		if (checkInfo(checkCode, pwd, pwdConfirm, contacts)) {
			if (pwd.getText().toString()
					.equals(pwdConfirm.getText().toString())) {
				YisiApp.showProgressDialog(activity, "正在校验", "校验中……");
				SMSUtils.submitVerifyCode(activity, contacts.getText()
						.toString(), checkCode.getText().toString(),
						new EventHandler() {
							public void afterEvent(int event, int result,
									Object data) {
								super.afterEvent(event, result, data);
								YisiApp.disMissProgressDialog();
								if (result == SMSSDK.RESULT_COMPLETE) {
									activity.runOnUiThread(callBack);
								} else {
									YisiApp.tell(activity, "验证码无效");
								}
							}
						});
			}
		} else {
			YisiApp.tell(activity, "密码不一致");
			pwd.requestFocus();
			return;
		}
	}


	// 检测非空
	private boolean checkInfo(EditText... evs) {
		for (EditText ev : evs) {
			if (TextUtils.isEmpty(ev.getText().toString())) {
				ev.requestFocus();
				YisiApp.tell(activity, "输入不合法");
				return false;
			}
		}
		return true;
	}

	private class MyClick implements View.OnClickListener {
		private MyClick() {
		}

		public void onClick(View view) {
			switch (view.getId()) {

			case R.id.check_code_button:
				getCheckCode();
				break;
			case R.id.confirm_button:
				Runnable callBack = null;
				if (flag == -1) {
					return;
				}
				if (flag == 0) {
					callBack = new Runnable() {
						public void run() {
							toRegist();
						}
					};
				} else if (flag == 1) {
					callBack = new Runnable() {
						public void run() {
							toResetPwd();
						}
					};
				}
				submitVerifyCode(callBack);
				break;
			default:
				break;
			}
		}
	}
}

/*
 * Location:
 * C:\Users\Administrator.J8GC8MIFGGCYPIM\Desktop\反编译工具包\classes3_dex2jar.jar
 * Qualified Name: com.yisi.yisiHome.baseServer.LoginServer JD-Core
 * Version: 0.6.2
 */