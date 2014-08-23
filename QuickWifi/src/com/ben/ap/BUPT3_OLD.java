package com.ben.ap;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class BUPT3_OLD extends AP {
	private ArrayList<NameValuePair> param;
	private final String mConnPath = "http://10.0.18.10/cgi-bin/wp_eag_login.cgi";
	private final String mDisconnPath = "http://10.0.18.10/cgi-bin/wp_eag_login.cgi?op_auth=logout";
	
	public String getConnPath() {
		return mConnPath;
	}

	public String getDisconnPath() {
		return mDisconnPath;
	}

	public BUPT3_OLD(String userName, String userPwd) {
		this.mSsid = "BUPT-3";
		param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair("a_name", userName));
		param.add(new BasicNameValuePair("a_pass", userPwd));
		param.add(new BasicNameValuePair("submit", "登录 Login"));//%B5%C7%C2%BC+Login
		param.add(new BasicNameValuePair("op_auth", "login"));
	}

	public BUPT3_OLD() {
		// GET /cgi-bin/wp_eag_login.cgi?op_auth=logout HTTP/1.1
		// Accept: text/html, application/xhtml+xml, */*
		// Referer: http://10.0.18.10/portal/cgiPortal/auth_suc.html
		// Accept-Language: zh-CN
		// User-Agent: Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64;
		// Trident/5.0)
		// Accept-Encoding: gzip, deflate
		// Host: 10.0.18.10
		// Connection: Keep-Alive
		//
		this.mSsid = "BUPT-3";
	}

	public ArrayList<NameValuePair> getLoginParam() {
		return param;
	}

//	public int startConnect() {
//		HttpPost httpRequest = new HttpPost(this.getConnPath());
//		System.out.println("尝试连接！");
//
//		try {
//			httpRequest.setEntity(new UrlEncodedFormEntity(this.getLoginParam(), HTTP.UTF_8));
//			HttpResponse httpResponse;
//			httpResponse = new DefaultHttpClient().execute(httpRequest);
//
//			if (httpResponse.getStatusLine().getStatusCode() == 200) {
//				/* 取出响应字符串 */
//				String strResult;
//				strResult = EntityUtils.toString(httpResponse.getEntity());
//				byte[] b = strResult.getBytes();
//				strResult = new String(b, "GBK");
//				System.out.println("连接完毕");
//				System.out.println(strResult);
//			} else {
//				System.out.println(httpResponse.getStatusLine().getStatusCode());
//			}
//
//			return this.testConn();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}

	public int startDisconnect() {
		HttpGet httpRequest = new HttpGet(this.getDisconnPath());
		try {
			System.out.println("尝试断开");
			HttpResponse httpResponse;
			httpResponse = new DefaultHttpClient().execute(httpRequest);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				/* 取出响应字符串 */
				String strResult;
				strResult = EntityUtils.toString(httpResponse.getEntity());
				byte[] b = strResult.getBytes();
				strResult = new String(b, "GBK");
				System.out.println("断开完毕");
				System.out.println(strResult);
			} else {
				System.out.println(httpResponse.getStatusLine().getStatusCode());
			}

		} catch (Exception e) {
			e.printStackTrace();
			return 755;

		}
		return 555;

	}

}
