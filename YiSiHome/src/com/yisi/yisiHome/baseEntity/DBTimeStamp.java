package com.yisi.yisiHome.baseEntity;

import com.lidroid.xutils.db.annotation.Id;


public class DBTimeStamp {
	@Id
	private int id;
	private String name;
	private long timeStamp;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}
