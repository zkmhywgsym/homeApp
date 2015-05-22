package com.yisi.yisiHome.baseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.alibaba.fastjson.JSONObject;
import com.yisi.yisiHome.R;
import com.yisi.yisiHome.baseActivity.YisiApp;
import com.yisi.yisiHome.utils.Constants;
import com.yisi.yisiHome.utils.WebHelper;


@SuppressLint("ValidFragment")
public class FeedBackFragment extends BaseFragment {
//	private AlertDialog selectMonitor;
	private EditText opinion;
	private EditText contact_way;//联系方式
	TelephonyManager tm;
	private ImageView submitDance;
	private Button submitButton;
	private LinearLayout ll_ani;
	private Spinner tv_type;//意见类型
	private AnimationDrawable animDance;
	List<Map<String, Object>> typeLists = new ArrayList<Map<String, Object>>();;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_idea_layout,container,false);
		initView();
		return rootView;
	}
	@Override
	public View getView() {
		return rootView;
	}

	private void initView() {
		tm = (TelephonyManager) mainActivity
				.getSystemService(Context.TELEPHONY_SERVICE);
		tv_type = (Spinner) rootView.findViewById(R.id.ideaSpinner);
//		tv_type.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				showType();
//			}
//		});
		tv_type.setAdapter(getAdapter());
		ll_ani = (LinearLayout) rootView.findViewById(R.id.ani);
		opinion = (EditText) rootView.findViewById(R.id.problem);
		contact_way = (EditText) rootView.findViewById(R.id.contacts);
		submitDance = (ImageView) rootView.findViewById(R.id.ani_img);
		submitButton = (Button) rootView.findViewById(R.id.confirm);
		animDance = (AnimationDrawable) submitDance.getBackground();
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				submit(v);
			}
		});
	}
//	protected void showType() {
//		if (null == selectMonitor) {
//			selectMonitor = new AlertDialog.Builder(mainActivity).create();
//			selectMonitor.show();
//			View view = View.inflate(mainActivity,R.layout.frame_idea_choose_layout,null);
//			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
//					LayoutParams.MATCH_PARENT));
//			Window window = selectMonitor.getWindow();
//			window.setContentView(view);
//			ListView monitor_list = (ListView) view
//					.findViewById(R.id.type_list);
//
//			monitor_list.setAdapter(getAdapter());
//			monitor_list.setOnItemClickListener(new OnItemClickListener() {
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id) {
////					tv_type.setText(typeLists.get(position).get("text")
////							.toString());
//					selectMonitor.dismiss();
//				}
//			});
//			RelativeLayout btn_cancel = (RelativeLayout) view
//					.findViewById(R.id.cancel);
//			btn_cancel.setOnClickListener(new View.OnClickListener() {
//				public void onClick(View v) {
//					selectMonitor.dismiss();
//				}
//			});
//		}
//		selectMonitor.show();
//
//	}

	private ArrayAdapter<String> getAdapter() {
//		String[] from = { "text" };
//		int[] to = { R.id.type_text };
//		getTypeData();
//		return new SimpleAdapter(mainActivity, typeLists,
//				R.layout.frame_ideatype_list_detaill, from, to);
		List<String> datas=new ArrayList<String>();
		datas.add("功能建议");
		datas.add("界面意见");
		datas.add("您的需求");
		datas.add("操作意见");
		datas.add("其他");
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(mainActivity,
				android.R.layout.simple_spinner_item, datas);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}

//	/**
//	 * 得到提议类型列表
//	 * 
//	 * @return
//	 */
//	private void getTypeData() {
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("text", "功能建议");
//		typeLists.add(map);
//		map = new HashMap<String, Object>();
//		map.put("text", "界面意见");
//		typeLists.add(map);
//		map = new HashMap<String, Object>();
//		map.put("text", "您的需求");
//		typeLists.add(map);
//		map = new HashMap<String, Object>();
//		map.put("text", "操作意见");
//		typeLists.add(map);
//		map = new HashMap<String, Object>();
//		map.put("text", "其他");
//		typeLists.add(map);
//	}


	// 提交意见
	@SuppressWarnings("unchecked")
	public void submit(View v) {
		String feedback = (String) tv_type.getSelectedItem();
		String opin = opinion.getText().toString().trim();
		if (opin == null || "".equals(opin)) {//内容验证
			YisiApp.tell(mainActivity, "请输入您的宝贵意见");
			return;
		}else if (opin.length()>50) {
			YisiApp.tell(mainActivity, "输入字数太长了...");
			return;
		}
		String nte = contact_way.getText().toString().trim();
		if (nte == null || "".equals(nte)||nte.length()<10) {//联系方式验证
			YisiApp.tell(mainActivity, "请留下您的联系方式");
			return;
		}
		BasicNameValuePair basName = new BasicNameValuePair("feedBackStyle",
				feedback);
		BasicNameValuePair obnv = new BasicNameValuePair("feedBackContent",
				opin);
		BasicNameValuePair cbnv = new BasicNameValuePair("feedBackContact", nte);
		BasicNameValuePair mobile = new BasicNameValuePair(
				"feedBackMobileType", android.os.Build.MODEL);
		BasicNameValuePair mobileimei = new BasicNameValuePair("feedBackImei",
				tm.getDeviceId());
		List<NameValuePair> bass = new ArrayList<NameValuePair>(4);
		bass.add(basName);
		bass.add(obnv);
		bass.add(cbnv);
		bass.add(mobile);
		bass.add(mobileimei);

		new AsyncTask<List<NameValuePair>, Void, String>() {

			protected void onPreExecute() {
				ll_ani.setVisibility(View.VISIBLE);
				animDance.start();
			};

			@Override
			protected String doInBackground(List<NameValuePair>... params) {
				WebHelper<String> result = new WebHelper<String>(String.class);
				return result.getObject(Constants.URL_FEEDBACK, params[0]);
//				return "";
			}

			protected void onPostExecute(String result) {
//				String res=JSONObject.parseObject(result).get("result").toString();
				animDance.stop();
				ll_ani.setVisibility(View.GONE);
				if (result!=null&&result.contains("true")) {
					YisiApp.tell(mainActivity, "感谢您的意见，谢谢！！！");
					opinion.setText("");
					contact_way.setText("");
				} else {
					YisiApp.tell(mainActivity, "提交失败，请重试！！！");
				}
			};
		}.execute(bass);
	}

}
