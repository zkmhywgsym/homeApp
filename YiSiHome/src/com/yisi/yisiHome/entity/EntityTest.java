package com.yisi.yisiHome.entity;

import java.io.Serializable;

import com.hyq.dbUtils.Id;

@SuppressWarnings("serial")
public class EntityTest implements Serializable{
	@Id
	private int id;
	private String name;
	private int sex;
	
	public EntityTest() {
		
	}
	
	public EntityTest(int id, String name, int sex) {
		super();
		this.id = id;
		this.name = name;
		this.sex = sex;
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
	
	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "EntityTest [id=" + id + ", name=" + name + ", sex=" + sex + "]";
	}
	
}
