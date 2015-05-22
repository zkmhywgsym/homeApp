package com.yisi.yisiHome.baseFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.ToggleButton;

import com.yisi.yisiHome.R;
import com.yisi.yisiHome.baseActivity.LoginActivity;
import com.yisi.yisiHome.baseActivity.YisiApp;
import com.yisi.yisiHome.utils.Constants;

//系统设置
@SuppressLint("ValidFragment")
public class SysConfFragment extends BaseFragment implements OnClickListener {
	private TableRow recMsg;
	private ToggleButton msgBtn;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_sys_config_layout,container,false);
		initView();
		return rootView;
	}
	@Override
	public View getView() {
		return rootView;
	}

	private void initView() {
		recMsg=(TableRow) rootView.findViewById(R.id.recever_msg);
		msgBtn=(ToggleButton) rootView.findViewById(R.id.setReceiveMessage);
//		boolean receveMsg=Boolean.valueOf(YisiApp.getValue(Constants.RECEVER_MSG, "true"));
//		switchRecMsg(receveMsg);
		recMsg.setOnClickListener(this);
		rootView.findViewById(R.id.logout).setOnClickListener(this);
	}
	private void switchRecMsg(boolean receve){
//		if (receve) {
//			msgBtn.setChecked(true);
//			JPushInterface.resumePush(mainActivity);
//		}else{
//			JPushInterface.stopPush(mainActivity);
//			msgBtn.setChecked(false);
//		}
//		YisiApp.setValue(Constants.RECEVER_MSG, receve+"");
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.recever_msg://接收消息
			switchRecMsg(!msgBtn.isChecked());
			break;
		case R.id.logout://注销用户
			Intent intent=new Intent(mainActivity, LoginActivity.class);
			intent.putExtra(Constants.LOGOUT, true);
			mainActivity.startActivity(intent);
			mainActivity.finish();
			break;

		default:
			break;
		}
	}





}
