package com.yisi.yisiHome.baseActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.yisi.yisiHome.R;
import com.yisi.yisiHome.baseServer.LoginServer;
import com.yisi.yisiHome.utils.AESHelper;
import com.yisi.yisiHome.utils.Constants;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText userNameET, pwdET;
	private Button loginBtn;
	private CheckBox cbAuto, cbRem;
	private LoginServer loginServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_layout);
		loginServer = new LoginServer(this);
		initView();
	}

	private void initView() {
		userNameET = (EditText) findViewById(R.id.editUserName);
		pwdET = (EditText) findViewById(R.id.editPassword);
		cbAuto = (CheckBox) findViewById(R.id.auto_login_check);
		cbRem = (CheckBox) findViewById(R.id.remember_pwd_check);
		cbAuto.setOnCheckedChangeListener(new Checked());
		cbRem.setOnCheckedChangeListener(new Checked());
		loginBtn = (Button) findViewById(R.id.bt_login);
		findViewById(R.id.regist_btn).setOnClickListener(this);
		findViewById(R.id.reset_pwd_btn).setOnClickListener(this);
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				loginServer.login(userNameET.getText().toString(), pwdET
						.getText().toString().trim(), cbRem.isChecked(),
						cbAuto.isChecked());
			}
		});
		checkBtn();
	}

	// 检查自动登陆
	private void checkBtn() {
		if (Boolean.valueOf(YisiApp.getValue(Constants.LOGIN_REPWD, "false"))) {
			cbRem.setChecked(true);
			userNameET.setText(YisiApp.getValue(Constants.LOGIN_ACCOUNT, ""));
			pwdET.setText(AESHelper.decrypt(YisiApp.getValue(Constants.LOGIN_PWD, "")));
		} else {
			cbRem.setChecked(false);
			cbAuto.setChecked(false);
			userNameET.setText("");
			pwdET.setText("");
		}
		if (Boolean.valueOf(YisiApp.getValue(Constants.LOGIN_AUTO, "false"))) {
			cbAuto.setChecked(true);
			cbRem.setChecked(true);
		} else {
			cbAuto.setChecked(false);
		}
		if (getIntent().getBooleanExtra(Constants.LOGOUT, false)) {
			return;
		}
		if (cbAuto.isChecked()) {
			loginServer.login(userNameET.getText().toString(), pwdET
					.getText().toString().trim(), true,true);
		}
	}

	/** 自动登陆和记住密码想对应 */
	private class Checked implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (buttonView.equals(cbRem) && isChecked == false) {
				cbAuto.setChecked(false);
			} else if (buttonView.equals(cbAuto) && isChecked == true) {
				cbRem.setChecked(true);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.regist_btn:// 注册
			loginServer.register();
			break;
		case R.id.reset_pwd_btn:// 重置
			loginServer.resetPwd();
			break;
		default:
			break;
		}
	}

}
