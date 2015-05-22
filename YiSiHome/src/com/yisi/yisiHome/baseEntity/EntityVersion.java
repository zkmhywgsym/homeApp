package com.yisi.yisiHome.baseEntity;

public class EntityVersion {
	
	private String verCode="0";//版本号        
	private String verName;//版本名
	private String apkUrl;//下载地址
	private String apkname;//apk包名
	private String updateTime;//上传时间
	private String verContent;//升级内容
	private String appname;//应用名字
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getVerName() {
		return verName;
	}
	public String getVerCode() {
		return verCode;
	}
	public void setVerCode(String verCode) {
		this.verCode = verCode;
	}
	public void setVerName(String verName) {
		this.verName = verName;
	}
	public String getApkUrl() {
		return apkUrl;
	}
	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	public String getApkname() {
		return apkname;
	}
	public void setApkname(String apkname) {
		this.apkname = apkname;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getVerContent() {
		return verContent;
	}
	public void setVerContent(String verContent) {
		this.verContent = verContent;
	}
}