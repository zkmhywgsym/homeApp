package com.yisi.yisiHome.baseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yisi.yisiHome.R;
import com.yisi.yisiHome.utils.AndroidUtils;

//关于
public class AboutFragment extends BaseFragment {
//	private ListView aboutList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_about, container, false);
		((TextView) rootView.findViewById(R.id.val_version))
				.setText("V "+AndroidUtils.getVersion(mainActivity));
//		aboutList = (ListView) rootView.findViewById(R.id.list_about);
//		initList();
		return rootView;
	}

//	private void initList() {
//		String[] datas = mainActivity.getResources().getStringArray(
//				R.array.items_about);
//		
//		aboutList.setAdapter(new MyAdapter(Arrays.asList(datas)));
//		aboutList.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				String text = null;
//				if (view instanceof TextView) {
//					text = ((TextView) view).getText().toString();
//				}
//				if (!TextUtils.isEmpty(text)) {
//					if ("版本升级".equals(text)) {
//						mainActivity.otherBackableFragment(new UpdateFragment(
//								mainActivity));
//					} else if ("意见反馈".equals(text)) {
//						mainActivity
//								.otherBackableFragment(new FeedBackFragment());
//					} else if ("系统设置".equals(text)) {
//					} else if ("联系我们".equals(text)) {
//
//					}
//				}
//			}
//		});
//	}

	@Override
	public String getTitle() {
		return getResources().getString(R.string.mode_about);
	}

//	private class MyAdapter extends BaseAdapter {
//		private List<String> items;
//
//		
//		public MyAdapter(List<String> items) {
//			super();
//			this.items = items;
//		}
//
//		@Override
//		public int getCount() {
//			return items.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return items.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			if (convertView == null) {
//				convertView = LayoutInflater.from(parent.getContext()).inflate(
//						android.R.layout.simple_list_item_1, parent,false);
//				convertView.setTag(convertView.findViewById(android.R.id.text1));
//			}
//			TextView textView=(TextView) convertView.getTag();
//			textView.setText(items.get(position));
//			textView.setTextColor(Color.BLACK);
//			return convertView;
//		}
//
//	}
}
