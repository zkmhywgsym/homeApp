package com.yisi.yisiHome.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class WebUtils {
	private static WebUtils instacne;
	private static AsyncHttpClient client;
	private WebUtils(){};
	public static WebUtils getInstance(){
		if (instacne==null) {
			instacne=new WebUtils();
			client=new AsyncHttpClient();
		}
		return instacne;
	}
	public void doHttp(String url,RequestParams params,AsyncHttpResponseHandler callBack){
		client.get(url,params,callBack);
	}
}
