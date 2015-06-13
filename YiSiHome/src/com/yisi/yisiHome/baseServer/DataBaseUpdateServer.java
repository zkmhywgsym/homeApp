package com.yisi.yisiHome.baseServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yisi.yisiHome.baseActivity.YisiApp;
import com.yisi.yisiHome.baseEntity.DBBaseEntitiy;
import com.yisi.yisiHome.baseEntity.DBTimeStamp;
import com.yisi.yisiHome.baseEntity.EntityUser;
import com.yisi.yisiHome.entity.EntityICCard;
import com.yisi.yisiHome.utils.WebUtils;

public class DataBaseUpdateServer<T> {
	private Context context;
	private int httpCount = 0;
	private Map<String, Class> classMap;
	private Map<String, String> urls;
	private String[] keys;

	public void update(Context context, Map<String, Class> classMap,
			Map<String, String> urls) {
		this.context = context;
		this.classMap = classMap;
		this.urls = urls;
		keys = new String[classMap.keySet().size()];
		classMap.keySet().toArray(keys);
		updatings(0);
	}

	// 和后台数据库同步
	private void updatings(int i) {
		if (i >= keys.length) {
			return;
		}
		if (httpCount > 3) {
			YisiApp.tell(context, "数据更新失败");
			YisiApp.disMissProgressDialog();
			return;
		}
		YisiApp.showProgressDialog(context, "正在更新", "正在更新数据库：" + i + "/"
				+ keys.length);
		String key = keys[i];
		// 获取时间戳
		DbUtils dbuTimeStamp = DbUtils.create(context);
		DBTimeStamp dbTimeStamp = new DBTimeStamp();
		dbTimeStamp.setName(key);
		DBTimeStamp entityTimeStamp=new DBTimeStamp();
		try {
			entityTimeStamp = dbuTimeStamp.findFirst(DBTimeStamp.class);
		} catch (DbException e1) {
			e1.printStackTrace();
		}
		long timeStamp = 0;
		if (entityTimeStamp == null) {// 首次加载
			timeStamp = System.currentTimeMillis();
		} else {
			timeStamp = Long.valueOf(entityTimeStamp.getTimeStamp());
		}
		// 查找新数据
		Class<DBBaseEntitiy> c = classMap.get(key);
		DbUtils dbu = DbUtils.create(context);
		DBBaseEntitiy t = null;
		try {
			Object o = c.newInstance();
			t = (DBBaseEntitiy) (o instanceof DBBaseEntitiy ? o
					: new DBBaseEntitiy());
			t.setOptTime(timeStamp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ArrayList<DBBaseEntitiy> data = new ArrayList<DBBaseEntitiy>();
//		ArrayList<DBBaseEntitiy> data = dbu.findAll(Selector.from(DBBaseEntitiy.class).where("","",""));
		// 同步数据
		RequestParams params = new RequestParams();
		params.put("timeStamp", timeStamp);
		params.put("list", JSON.toJSONString(data));
		final Integer index = Integer.valueOf(i);
		WebUtils.getInstance().doHttp(urls.get(key), params,
				new TextHttpResponseHandler() {
					// json返回格式为{timeStamp:v,json:[list json]}
					@Override
					public void onSuccess(int arg0, Header[] arg1, String json) {
						try {
							saveToLocal(index.intValue(), json);
						} catch (DbException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						httpCount++;
						updatings(index.intValue());
					}
				});
	}

	private void saveToLocal(int index, String json) throws DbException {
		Class<DBBaseEntitiy> clazz = classMap.get(keys[index]);
		List<DBBaseEntitiy> list = JSON.parseArray(JSON.parseObject(json)
				.getString("json"), clazz);
		DbUtils dbu = DbUtils.create(context);
		if (list == null || list.isEmpty()) {
			YisiApp.e(this, "返回结果为空");
			YisiApp.disMissProgressDialog();
			return;
		}
		for (DBBaseEntitiy t : list) {
			if (t.getStatue() == 'd') {
				dbu.delete(t);
			} else {
				dbu.update(t);
			}
		}
		DBTimeStamp dbt = new DBTimeStamp();
		dbt.setName(keys[index]);
		dbt = dbu.findFirst(DBTimeStamp.class);
		if (dbt == null ) {
			dbt = new DBTimeStamp();
		}
		dbt.setName(keys[index]);
		dbt.setTimeStamp(JSON.parseObject(json).getLongValue("timeStamp"));
		List<DBTimeStamp> items = new ArrayList<DBTimeStamp>();
		items.add(dbt);
		dbu.update(items);
		YisiApp.disMissProgressDialog();
		YisiApp.tell(context, "数据库" + index + "更新成功");
		index++;
		updatings(index);
	}
}
