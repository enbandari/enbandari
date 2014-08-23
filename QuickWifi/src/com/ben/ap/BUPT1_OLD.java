package com.ben.ap;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class BUPT1_OLD extends AP {

	private ArrayList<NameValuePair> mConnParam;
	private final String mConnPath = "http://10.8.0.1/cgi-bin/login";
	private final String mDisconnPath = "http://10.8.0.1/cgi-bin/login?cmd=logout";

	public String getConnPath() {
		return mConnPath;
	}

	public String getDisconnPath() {
		return mDisconnPath;
	}

	public BUPT1_OLD(String userName, String userPwd) {
		this.mSsid = "BUPT-1";

		mConnParam = new ArrayList<NameValuePair>();
		mConnParam.add(new BasicNameValuePair("user", userName));
		mConnParam.add(new BasicNameValuePair("password", userPwd));
		mConnParam.add(new BasicNameValuePair("cmd", "authenticate"));
		mConnParam.add(new BasicNameValuePair("Login", "Log+In"));
	}

	public BUPT1_OLD() {
		this.mSsid = "BUPT-1";

		// 构造断开网络，采用get方式，显示传了个参数cmd=logout
		// GET /cgi-bin/login?cmd=logout HTTP/1.1
		// Accept: text/html, application/xhtml+xml, */*
		// Referer: http://10.8.0.1/cgi-bin/login
		// Accept-Language: zh-CN
		// User-Agent: Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64;
		// Trident/5.0)
		// Accept-Encoding: gzip, deflate
		// Host: 10.8.0.1
		// Connection: Keep-Alive
		// Cookie:
		// CPsession=http%3A%2F%2Fwww%2Ebaidu%2Ecom%2F%26ip%3D10%2E8%2E23%2E235

	}

	public ArrayList<NameValuePair> getLoginParam() {
		return mConnParam;
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
