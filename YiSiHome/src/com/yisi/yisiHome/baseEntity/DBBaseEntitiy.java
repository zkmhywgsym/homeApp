package com.yisi.yisiHome.baseEntity;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Id;
/**继承本类的实体，以key为ID，对数据进行操作时，必须用System.currentTimeMillis()更新操作时间optTime,所有属性都必须有默认值否则会按null保存*/
@SuppressWarnings("serial")
public class DBBaseEntitiy implements Serializable{
	private long key;//本地不维护，由服务端传
	@Id
	private int id;
	private long optTime;
	private int statue;//modify:'m',add:'a',del:'d'
	public long getKey() {
		return key;
	}
	public void setKey(long key) {
		this.key = key;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getOptTime() {
		return optTime;
	}
	public void setOptTime(long optTime) {
		this.optTime = optTime;
	}
	public int getStatue() {
		return statue;
	}
	public void setStatue(int statue) {
		this.statue = statue;
	}
	
}
