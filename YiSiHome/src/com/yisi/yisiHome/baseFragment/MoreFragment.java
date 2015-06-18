package com.yisi.yisiHome.baseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yisi.yisiHome.R;
//更多
public class MoreFragment extends BaseFragment{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_more, container,false);
		return rootView;
	}
	@Override
	public String getTitle() {
		return getResources().getString(R.string.mode_about);
	}
	@Override
	public void doClick(View view){
		switch (view.getId()) {
		case R.id.per_info:
			mainActivity.otherBackableFragment(new PersonInfoFragment());
			break;
		case R.id.sys_set:
			mainActivity.otherBackableFragment(new SysConfFragment());
			break;
		case R.id.feedBack:
			mainActivity.otherBackableFragment(new FeedBackFragment());
			break;
		case R.id.update:
			new UpdateFragment(mainActivity).update();
			break;
		case R.id.about:
			mainActivity.otherBackableFragment(new AboutFragment());
			break;

		}
	}
}
