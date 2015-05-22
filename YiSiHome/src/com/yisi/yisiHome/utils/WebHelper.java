package com.yisi.yisiHome.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yisi.yisiHome.BuildConfig;

public class WebHelper<T> {

	/**
	 * 超时时间设置
	 */
	private static final int REQUEST_TIME = 15000;
	private boolean net = true;
	public static boolean IS_TIMEOUT = false;
	@SuppressWarnings("deprecation")
	private static Cookie cookie;
	private Class<T> clazz;

	public WebHelper() {

	}

	public WebHelper(Class<T> t) {
		clazz = t;
	}

	@SuppressWarnings("deprecation")
	private String requestHttp(String urlStr) {
		String result = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIME);
		HttpGet get = new HttpGet(urlStr);
		if (cookie!=null) {
			get.setHeader("Cookie", cookie.getName()+"="+cookie.getValue());
//			get.setHeader(cookie.getName(), cookie.getValue());
		}
		try {
			HttpResponse response = httpClient.execute(get);
			List<Cookie> cookies=httpClient.getCookieStore().getCookies();
			for (Cookie c : cookies) {
				if ("JSESSIONID".equals(c.getName())) {
					cookie=c;
				}
			}
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// Post请求Http
	@SuppressWarnings("deprecation")
	private String requestPostHttp(String urlStr, List<NameValuePair> parameters) {
		if (net) {
			DefaultHttpClient httpClient = null;
			String result = "";
			try {
				httpClient = new DefaultHttpClient();
				httpClient.getParams().setParameter(
						CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIME);
				httpClient.getParams().setParameter(
						CoreConnectionPNames.SO_TIMEOUT, REQUEST_TIME);
				HttpPost post = new HttpPost(urlStr);
				if (cookie!=null) {
					post.setHeader("Cookie", cookie.getName()+"="+cookie.getValue());
//					post.setHeader(cookie.getName(), cookie.getValue());
				}
				if (null != parameters)
					post.setEntity(new UrlEncodedFormEntity(parameters,
							HTTP.UTF_8));
				HttpResponse response = httpClient.execute(post);
				List<Cookie> cookies=httpClient.getCookieStore().getCookies();
				for (Cookie c : cookies) {
					if ("JSESSIONID".equals(c.getName())) {
						cookie=c;
					}
				}
				if (response.getStatusLine().getStatusCode() == 200) {
					result = EntityUtils.toString(response.getEntity());
					IS_TIMEOUT = false;
				}
			} catch (SocketException e) {
				e.printStackTrace();
				IS_TIMEOUT = true;
				return null;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				IS_TIMEOUT = true;
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				IS_TIMEOUT = true;
				return null;
			} finally {
				httpClient.getConnectionManager().shutdown();
			}
			return result.toString();
		}
		return null;

	}

//	@SuppressWarnings("deprecation")
//	private int requestPostHttpCode(String urlStr, List<NameValuePair> parameters) {
//		if (net) {
//			int result = 0;
//			DefaultHttpClient httpClient = null;
//			BufferedReader br = null;
//			try {
//				httpClient = new DefaultHttpClient();
//				httpClient.getParams().setParameter(
//						CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIME);
//				HttpPost post = new HttpPost(urlStr);
//				if (cookie!=null) {
//					post.setHeader("Cookie", cookie.getName()+"="+cookie.getValue());
////					post.setHeader(cookie.getName(), cookie.getValue());
//				}
//				if (null != parameters)
//					post.setEntity(new UrlEncodedFormEntity(parameters,
//							HTTP.UTF_8));
//				HttpResponse response = null;
//				response = httpClient.execute(post);
//				List<Cookie> cookies=httpClient.getCookieStore().getCookies();
//				for (Cookie c : cookies) {
//					if ("JSESSIONID".equals(c.getName())) {
//						cookie=c;
//					}
//				}
//				int statusCode = response.getStatusLine().getStatusCode();
//				result = statusCode;
//
//				InputStream in = response.getEntity().getContent();
//				br = new BufferedReader(new InputStreamReader(in));
//			} catch (SocketException e) {
//				return result;
//			} catch (MalformedURLException e) {
//				return result;
//			} catch (IOException e) {
//				return result;
//			} finally {
//				httpClient.getConnectionManager().shutdown();
//				try {
//					if (br != null) {
//						br.close();
//						br = null;
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			return result;
//		}
//		return 0;
//
//	}

	/**
	 * 
	 * @author HuangF
	 * @version 创建时间2012-12-2 下午4:02:50 方法说明:
	 * @param url
	 *            请求地址
	 * @param parameters
	 *            请求参数 为null 使用Get请求
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getResult(String url, List<NameValuePair> parameters) {
		String result = null;
		if (null == parameters) {
			result = requestHttp(url);
		} else {
			result = requestPostHttp(url, parameters);
		}
		if (BuildConfig.DEBUG) {
			Log.i("测试数据getResult", "------>" + result);
		}
		return result;
	}
	//获取集合
	@SuppressWarnings("deprecation")
	public List<T> getArray(String url, List<NameValuePair> parameters) {
		String result = getResult(url, parameters);
		if (BuildConfig.DEBUG) {
			Log.i("测试数据getArray", "------>" + result);
		}
		try {
			if (result != null && !result.equals("") && !result.equals("[]")
					&& !"[{\"result\":\"error\"}]".equals(result)){
				if (result.contains("no login")) {
					return new ArrayList<T>();
				}else{
					return JSON.parseArray(result, clazz);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
//			return JSON.parseArray("[{'cars':'晋','materials':{'石灰':'1s451','石灰2':'1s452','石灰3':'1s453'},'persons':{'大同煤业':'deg121','大同煤业2':'deg122','大同煤业3':'deg123'},'status':'已交票，已发货，已派车','type':0},{'cars':'晋','materials':{'$ref':'$[0].materials'},'persons':{'$ref':'$[0].persons'},'status':'已交票，已发货，已派车','type':3}]", clazz);
		}
		return new ArrayList<T>();
	}
	//获取实体
	@SuppressWarnings("deprecation")
	public T getObject(String url, List<NameValuePair> parameters) {
		T t=null;
		try {
			t=clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		String result = getResult(url, parameters);
		if (BuildConfig.DEBUG) {
			Log.i("测试数据getObject", "------>" + result);
		}
		if (result != null){
			if (result.contains("no login")) {
				return t;
			}
			return JSON.parseObject(result, clazz);
		}
		return t;
		
	}

	/**
	 * 返回的结果包含不同的实体集合
	 * 
	 * @param url
	 * @param parameters
	 * @param clazzs
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Map<Class<?>, List<?>> requestDiffereeArrayResult(String url,
			List<NameValuePair> parameters, Class<?>... clazzs) {
		String result = getResult(url, parameters);
		Log.i("test", "------>" + result);
		return analyConter(result, clazzs);
	}

	private Map<Class<?>, List<?>> analyConter(String result, Class<?>... clazzs) {
		Map<Class<?>, List<?>> maps = new HashMap<Class<?>, List<?>>();
		JSONObject json = null;
		try {
			json = JSON.parseObject(result);
		} catch (Exception e) {
			Log.i("message", "结果解析错误");
		}
		if (json != null) {
			for (Class<?> clazz : clazzs) {
				String jsonResult = json.getString(clazz.getSimpleName());
				maps.put(clazz, JSONArray.parseArray(jsonResult, clazz));
			}
		}
		return maps;
	}

	public Map<Class<?>, List<?>> analyConter4Array(String result,
			Class<?>... clazzs) {
		Map<Class<?>, List<?>> maps = new HashMap<Class<?>, List<?>>();
		JSONArray array = JSON.parseArray(result);
		if (array != null && array.size() > 0) {
			for (int i = 0; i < array.size(); i += 2) {
				for (Class<?> clazz : clazzs) {
					if (clazz.getSimpleName().equals(array.get(i) + "")) {
						List<?> list = JSON.parseArray(array.get(i + 1) + "",
								clazz);
						maps.put(clazz, list);
					}
				}

			}
		}
		return maps;
	}

	/**
	 * @author HuangF
	 * @version 创建时间2013-1-16 上午10:49:24 方法说明: 提交数据
	 */

	@SuppressWarnings("deprecation")
	public boolean submit(String urlStr,
			List<? extends NameValuePair> parameters) {
		if (net) {
			DefaultHttpClient submitClient = new DefaultHttpClient();
			submitClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIME);
			HttpPost post = new HttpPost(urlStr);
			if (cookie!=null) {
				post.setHeader("Cookie", cookie.getName()+"="+cookie.getValue());
//				post.setHeader(cookie.getName(), cookie.getValue());
			}
			if (null != parameters)
				try {
					post.setEntity(new UrlEncodedFormEntity(parameters,
							HTTP.UTF_8));
					HttpResponse response = submitClient.execute(post);
					List<Cookie> cookies=submitClient.getCookieStore().getCookies();
					for (Cookie c : cookies) {
						if ("JSESSIONID".equals(c.getName())) {
							cookie=c;
						}
					}
					if (response.getStatusLine().getStatusCode() == 200) {
						return true;
					}
				} catch (UnsupportedEncodingException e) {
				} catch (ClientProtocolException e) {
				} catch (IOException e) {
				}
		}
		return false;
	}


}