package com.yisi.yisiHome.baseFragment;

import com.yisi.yisiHome.R;
import com.yisi.yisiHome.baseActivity.MainActivity;
import com.yisi.yisiHome.utils.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

//基类 使用注意点：1.返回回调doBack(),点击回调doClick()2.用getResources()前记得看是否isAdded()3.切换横竖用mainActivity.switchScreenOrientation();，切换全屏用mainActivity.fullScreen();

public class BaseFragment extends Fragment {
	protected View rootView;
	protected MainActivity mainActivity;
	protected String flag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentActivity acitvity = getActivity();
		if (acitvity instanceof MainActivity) {
			mainActivity = (MainActivity) acitvity;
		}
	}

	public void setValue(Intent activityIntent, String name, String value) {
		if (activityIntent == null) {
			Log.e(getClass().getSimpleName(), "setValue() activityIntent==null");
			return;
		}
		activityIntent.removeExtra(name);
		activityIntent.putExtra(name, value);
	}

	public String getValue(Intent activityIntent, String name) {
		if (activityIntent == null) {
			Log.e(getClass().getSimpleName(), "getValue() activityIntent==null");
			return "get Intent Value False";
		}
		return activityIntent.getStringExtra(name);
	}


	@Override
	public void onResume() {
		Intent intent=mainActivity.getIntent();
		int reqCode=intent.getIntExtra(Constants.REQUEST_CODE, -1);
		if (reqCode!=-1) {
			onResult(intent);
		}
//		getActivity().findViewById(R.id.btn_titleLeftButton).setVisibility(
//				View.GONE);
		getActivity().findViewById(R.id.btn_titleRightButton)
		.setVisibility(View.GONE);
		((TextView) getActivity().findViewById(R.id.titleText))
				.setText(getTitle());
		super.onResume();
	}

	public String getTitle() {
		return getResources().getString(R.string.app_name);
	}

	@Override
	public void onStart() {
		mainActivity.setCurFragment(this);
		super.onStart();
	}
	// type="start"/"end";
		public final void showTimeDialog(String type,int requestCode) {
			FragmentTransaction ft = mainActivity.getSupportFragmentManager()
					.beginTransaction();
			Intent intent = mainActivity.getIntent();
			DateDialogFragment dialog = DateDialogFragment.newInstance();
			if (dialog.isAdded()) {
				return;
			}
			dialog.setType(type);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			dialog.show(ft, "df");
			intent.removeExtra(Constants.REQUEST_CODE);
			intent.putExtra(Constants.REQUEST_CODE, requestCode);
		}
	//结果回调
	public void onResult(Intent intent) {}
	
	public final void onSwitch(){
		if (mainActivity==null||!mainActivity.isSwitched()) {
			return;
		}
		mainActivity.switchScreenOrientation();
		mainActivity.switchFullScreen();
	}
	@Override
	public View getView() {
		if (rootView != null) {
			return rootView;
		}
		return super.getView();
	}

	public void doSaveInstanceState(Bundle outState){
		
	}
	public void doRestoreInstanceState(Bundle savedInstanceState){
		
	}
	// 返回回调
	public void doBack() {
	}

	// 点击回调
	public void doClick(View view) {
	}

	public void menuChangeTo(int checkedId) {
		
	}

	public void doNewIntent(Intent intent) {}
}
