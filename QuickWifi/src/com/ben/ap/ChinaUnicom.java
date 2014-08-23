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

public class ChinaUnicom extends AP {
	//http://wlan7.bj.chinaunicom.cn/index.jsp?basname=&macAddr=&isMacAuth=false
//			POST /login.do?time=11:17:59%20UTC+0800 HTTP/1.1
//			Accept: text/plain, */*; q=0.01
//			Content-Type: application/x-www-form-urlencoded
//			X-Requested-With: XMLHttpRequest
//			Referer: http://wlan7.bj.chinaunicom.cn/index.jsp?basname=&macAddr=&isMacAuth=false
//			Accept-Language: zh-cn
//			Accept-Encoding: gzip, deflate
//			User-Agent: Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)
//			Host: wlan7.bj.chinaunicom.cn
//			Content-Length: 151
//			Connection: Keep-Alive
//			Cache-Control: no-cache
//			Cookie: JSESSIONID=8AFA0C730F4BA78F17F6D23F335DA1F2
//
	//	username=12231283779281
	// &password=sadfad
	// &passwordType=6
	// &userOpenAddress=bj
	// &checkbox=1
	// &basname=null
	// &setUserOnline=
	// &sap=
	// &macAddr=
	// &bandMacAuth=0
	// &isMacAuth=
	//
	
	private ArrayList<NameValuePair> mConnParam;
	private ArrayList<NameValuePair> mDisconnParam;
	private final String mConnPath = "http://wlan7.bj.chinaunicom.cn/login.do";
	private final String mDisconnPath = "";

	public String getConnPath() {
		return mConnPath;
	}

	public String getDisconnPath() {
		return mDisconnPath;
	}

	public ChinaUnicom(String userName, String userPwd) {
		this.mSsid = "ChinaUnicom";

		mConnParam = new ArrayList<NameValuePair>();
		mConnParam.add(new BasicNameValuePair("username", userName));
		mConnParam.add(new BasicNameValuePair("password", userPwd));
		mConnParam.add(new BasicNameValuePair("passwordType", "6"));
		mConnParam.add(new BasicNameValuePair("userOpenAddress", "bj"));
		mConnParam.add(new BasicNameValuePair("checkbox", "1"));
		mConnParam.add(new BasicNameValuePair("basname", "null"));
		mConnParam.add(new BasicNameValuePair("setUserOnline", ""));
		mConnParam.add(new BasicNameValuePair("sap", ""));
		mConnParam.add(new BasicNameValuePair("macAddr", ""));
		mConnParam.add(new BasicNameValuePair("bandMacAuth", ""));
		mConnParam.add(new BasicNameValuePair("isMacAuth", ""));
	}

	public ChinaUnicom() {
		this.mSsid = "ChinaUnicom";

		// PtButton Logoff
//		mDisconnParam = new ArrayList<NameValuePair>();
//		mDisconnParam.add(new BasicNameValuePair("PtButton", "Logoff"));
	}

	public ArrayList<NameValuePair> getLoginParam() {
		return mConnParam;
	}

	public ArrayList<NameValuePair> getLogoffParam() {
		return this.mDisconnParam;
	}



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
