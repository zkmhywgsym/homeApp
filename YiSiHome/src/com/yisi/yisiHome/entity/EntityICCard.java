package com.yisi.yisiHome.entity;

import java.io.Serializable;

import com.yisi.yisiHome.baseEntity.DBBaseEntitiy;

@SuppressWarnings("serial")
public class EntityICCard extends DBBaseEntitiy implements Serializable{
	private String cardId;//卡号
	private String orderId;//单号
	private String custom;//客户
	private String carNum;//车牌号
	private String weightTime;//过磅时间
	private String phoneNum;//手持号
	private String operator;//操作员
	private String material;//物料
	private long operateTime;//操作时间
	private String imageName;//车辆拍照
	private double longitude;//经度坐标
	private double latitude;//纬度坐标
	
	public EntityICCard() {
		
	}

	public EntityICCard(int id, String cardId, String orderId, String custom,
			String carNum, String weightTime, String phoneNum, String operator,
			String material, long operateTime, String imageName,
			double longitude, double latitude) {
		super();
		this.cardId = cardId;
		this.orderId = orderId;
		this.custom = custom;
		this.carNum = carNum;
		this.weightTime = weightTime;
		this.phoneNum = phoneNum;
		this.operator = operator;
		this.material = material;
		this.operateTime = operateTime;
		this.imageName = imageName;
		this.longitude = longitude;
		this.latitude = latitude;
	}


	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCustom() {
		return custom;
	}

	public void setCustom(String custom) {
		this.custom = custom;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getWeightTime() {
		return weightTime;
	}

	public void setWeightTime(String weightTime) {
		this.weightTime = weightTime;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public long getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(long operateTime) {
		this.operateTime = operateTime;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "EntityICCard [cardId=" + cardId + ", orderId=" + orderId
				+ ", custom=" + custom + ", carNum=" + carNum + ", weightTime="
				+ weightTime + ", phoneNum=" + phoneNum + ", operator="
				+ operator + ", material=" + material + ", operateTime="
				+ operateTime + ", imageName=" + imageName + ", longitude="
				+ longitude + ", latitude=" + latitude + "]";
	}

	
	
	
	
}
