package com.yisi.yisiHome.utils;

public class Constants {
	public static final String PHONE_REGULAR_EXPRESSION="^1[3|4|5|8][0-9]\\d{8}$";
	public static final String EMCRUPTION_KEY = "com.yisi.yisiHome";

	public static final String HAS_INITED = "hasInited";
	public static final String REQUEST_CODE="requestCode";
	public static final String RESULT_CODE="resultCode";
	public static final String RESULT="result";
	public static final int ADD_OR_UPDATE = 1;
	public static final int DELETE = 2;
	
	public static final String DATE_TIME_TYPE_DAY="day";
	public static final String DATE_TIME_TYPE_MONTH="month";
	public static final String DATE_TIME_TYPE_YEAR="year";
	public static final String DATE_TIME_TYPE_MINUTES="minutes";
	public static final String DATE_TIME_TYPE_SECOND="second";
	
//	public static final String URL = "http://localhost:8080/WebUser";
	private static String URL="http://211.161.100.8:8080/androidService/";	
	public static final String URL_UPDATE_USER = URL+"";
	public static final String URL_REGISTER_USER = URL+"";
	public static final String URL_RESET = URL+"";
	public static final String URL_LOGIN = URL+"";
	public static final String URL_FEEDBACK = URL+"";
	public static final String URL_UPDATE = URL+"";
	public static final String URL_CRASH_SEND=URL+"feedbackController/feedback.do";
	
	public static String LOGOUT="logout";
	public static final String LOGIN_STATUS_SUCCESS="success";
	public static final String LOGIN_STATUS_USERORPWD_ERR="userOnPwdErr";
	public static final String LOGIN_STATUS_OTHER="other";
	public static final String LOGIN_ACCOUNT="loginAccount";
	public static final String LOGIN_PWD="loginPwd";
	public static final String LOGIN_REPWD="rememberPwd";
	public static final String LOGIN_AUTO="autoLogin";

}
