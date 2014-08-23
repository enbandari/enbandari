package com.ben.ap;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class BUPT2_OLD extends AP {
	private ArrayList<NameValuePair> mConnParam;
	private ArrayList<NameValuePair> mDisconnParam;
	private final String mConnPath = "http://10.8.128.1/portal/logon.cgi";
	private final String mDisconnPath = "http://10.8.128.1/portal/logon.cgi";

	public String getConnPath() {
		return mConnPath;
	}

	public String getDisconnPath() {
		return mDisconnPath;
	}

	public BUPT2_OLD(String userName, String userPwd) {
		this.mSsid = "BUPT-2";

		mConnParam = new ArrayList<NameValuePair>();
		mConnParam.add(new BasicNameValuePair("PtUser", userName));
		mConnParam.add(new BasicNameValuePair("PtPwd", userPwd));
		mConnParam.add(new BasicNameValuePair("PtButton", "Logon"));
	}

	public BUPT2_OLD() {
		this.mSsid = "BUPT-2";

		// PtButton Logoff
		mDisconnParam = new ArrayList<NameValuePair>();
		mDisconnParam.add(new BasicNameValuePair("PtButton", "Logoff"));
	}

	public ArrayList<NameValuePair> getLoginParam() {
		return mConnParam;
	}

	public ArrayList<NameValuePair> getLogoffParam() {
		return this.mDisconnParam;
	}

//	public int startConnect() {
//		HttpPost httpRequest = new HttpPost(this.getConnPath());
//		System.out.println("尝试连接！");
//
//		try {
//			httpRequest.setEntity(new UrlEncodedFormEntity(this.getLoginParam(), HTTP.UTF_8));
//			HttpResponse httpResponse;
//			httpResponse = new DefaultHttpClient().execute(httpRequest);
//			System.out.println("尝试连接。。好慢啊！");
//
//			if (httpResponse.getStatusLine().getStatusCode() == 200) {
//				/* 取出响应字符串 */
//				String strResult;
//				strResult = EntityUtils.toString(httpResponse.getEntity());
//				byte[] b = strResult.getBytes();
//				strResult = new String(b, "UTF-8");
//				System.out.println("连接完毕");
//				System.out.println(strResult);
//				return this.testConn();
//			} else {
//				System.out.println(httpResponse.getStatusLine().getStatusCode());
//			}
//
//			//return this.testConn();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}

	public int startDisconnect() {
		HttpPost httpRequest = new HttpPost(this.getDisconnPath());
		System.out.println("尝试断开！");

		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(this.getLogoffParam(), HTTP.UTF_8));
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