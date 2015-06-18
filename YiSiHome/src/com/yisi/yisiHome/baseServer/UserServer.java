package com.yisi.yisiHome.baseServer;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.yisi.yisiHome.baseEntity.DBTimeStamp;
import com.yisi.yisiHome.baseEntity.EntityUser;
import com.yisi.yisiHome.utils.Constants;
import com.yisi.yisiHome.utils.MD5Utils;
import com.yisi.yisiHome.utils.WebHelper;

public class UserServer {
	private DbUtils dbu;
	WebHelper<EntityUser> webHelper=new WebHelper<EntityUser>(EntityUser.class);

	public UserServer(Context context) {
		super();
		dbu=DbUtils.create(context);
	}
	public boolean checkUser(EntityUser user){
		EntityUser u=new EntityUser();
		List<EntityUser> result=new ArrayList<EntityUser>();
		try {
			result = dbu.findAll(Selector.from(EntityUser.class).where("loginName", "=", user.getLoginName()));
		} catch (DbException e) {
			e.printStackTrace();
		}
		if (result==null||result.size()==0) {
			//TODO 联网登陆
			return false;
		}
		u=result.get(0);
		return u.getUserPwd().equals(MD5Utils.Md5(user.getUserPwd()));
	}
	public boolean saveUser(EntityUser...entityUsers){
		try {
			for (EntityUser entityUser : entityUsers) {
				entityUser.setUserPwd(MD5Utils.Md5(entityUser.getUserPwd()));
				dbu.saveOrUpdate(entityUser);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	public boolean upDateUser(){
//		try {
//			List<NameValuePair> params=new ArrayList<NameValuePair>();
//			params.add(new BasicNameValuePair("timestamp",getTimestamp()+""));
//			List<EntityUser> users=webHelper.getArray(Constants.URL_UPDATE_USER, params);
//			for (EntityUser entityUser : users) {
//				if(Constants.ADD_OR_UPDATE==entityUser.getType()){
//					dbu.saveOrUpdate(entityUser);
//				}else if(Constants.DELETE==entityUser.getType()){
//					entityUser.setId(0);
//					entityUser.setPwd("");
//					dbu.delete(entityUser);
//				}
//			}
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
		return false;
	}
	private long getTimestamp(){
		try {
			DBTimeStamp dbt=dbu.findFirst(Selector.from(DBTimeStamp.class).orderBy("timestamp"));
			return dbt.getTimeStamp();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	public boolean register(EntityUser user){
		try {
			user.setUserPwd(MD5Utils.Md5(user.getUserPwd()));
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
