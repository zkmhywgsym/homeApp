package com.yisi.yisiHome.baseServer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.hyq.dbUtils.DBManager;
import com.yisi.yisiHome.baseEntity.EntityUser;
import com.yisi.yisiHome.utils.Constants;
import com.yisi.yisiHome.utils.MD5Utils;
import com.yisi.yisiHome.utils.WebHelper;

public class UserServer {
	private DBManager<EntityUser> dbm;
	WebHelper<EntityUser> webHelper=new WebHelper<EntityUser>(EntityUser.class);

	public UserServer(Context context) {
		super();
		dbm=new DBManager<EntityUser>(context, EntityUser.class);
	}
	public boolean checkUser(EntityUser user){
		EntityUser u=new EntityUser();
		u.setName(user.getName());
		ArrayList<EntityUser> result=dbm.queryWhere(u);
		if (result.size()==0) {
			//TODO 联网登陆
			return false;
		}
		u=result.get(0);
		return u.getPwd().equals(MD5Utils.Md5(user.getPwd()));
	}
	public boolean saveUser(EntityUser...entityUsers){
		try {
			for (EntityUser entityUser : entityUsers) {
				entityUser.setPwd(MD5Utils.Md5(entityUser.getPwd()));
				dbm.insert(entityUser);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	public boolean upDateUser(){
		try {
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("timestamp",getTimestamp()+""));
			List<EntityUser> users=webHelper.getArray(Constants.URL_UPDATE_USER, params);
			for (EntityUser entityUser : users) {
				if(Constants.ADD_OR_UPDATE==entityUser.getType()){
					dbm.insert(entityUser);
				}else if(Constants.DELETE==entityUser.getType()){
					entityUser.setId(0);
					entityUser.setPwd("");
					dbm.delete(entityUser);
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	private long getTimestamp(){
		try {
			return dbm.query(new StringBuilder(" order by timestamp")).get(0).getTimestamp();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	public boolean register(EntityUser user){
		try {
			user.setPwd(MD5Utils.Md5(user.getPwd()));
			List<NameValuePair> params=new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user",JSON.toJSONString(user)));
			String result=webHelper.getResult(Constants.URL_REGISTER_USER, params);
			return result.contains("true");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
