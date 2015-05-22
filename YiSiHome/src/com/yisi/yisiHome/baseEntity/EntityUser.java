package com.yisi.yisiHome.baseEntity;

import java.io.Serializable;

import com.hyq.dbUtils.Id;
import com.hyq.dbUtils.Operator;

@SuppressWarnings("serial")
public class EntityUser implements Serializable{
	@Id
	private int id;
	private String name;
	private String pwd;
	private long timestamp;
	@Operator
	private int type;//0 add or update;1 del
	
	public EntityUser() {}

	public EntityUser(int id, String name, String pwd,int type,long timestamp) {
		super();
		this.id = id;
		this.name = name;
		this.pwd = pwd;
		this.type = type;
		this.timestamp = timestamp;
	}

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

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "EntityUser [id=" + id + ", name=" + name + ", pwd=" + pwd
				+ ", timestamp=" + timestamp + ", type=" + type + "]";
	}

	
	
	
}
