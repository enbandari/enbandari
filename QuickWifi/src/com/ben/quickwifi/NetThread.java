package com.ben.quickwifi;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.ben.ap.AP;
import com.ben.ap.BUPT1;
import com.ben.ap.BUPT2;
import com.ben.ap.BUPT3;
import com.ben.ap.BUPT_OTHER;
import com.ben.ap.ChinaUnicom;
import com.ben.util.NetMethod;

public class NetThread extends Thread {
	private String apName;
	private String mUserName;
	private String mUserPwd;
	private AP ap;
	private Context mContext;
	private Handler mHandler;
	private int ap_type = -1; // 0 for bupt-123, 1 for else

	public NetThread(Handler mHandler, Context mContext, String apName, String mUserName, String mUserPwd) {
		super();
		this.mHandler = mHandler;
		this.mContext = mContext;
		this.mUserName = mUserName;
		this.mUserPwd = mUserPwd;
		this.apName = apName;
	}

	private void initAP() {
		switch (ap_type) {
		case 0:
			ap = new BUPT1(mUserName, mUserPwd);
			break;
		case 1:
			ap = new BUPT_OTHER(mUserName, mUserPwd);
			break;
		case -1:
		default:
			if (this.apName.contains("BUPT-")) {
				ap = new BUPT1(mUserName, mUserPwd);
			}
			// else if (this.apName.contains("BUPT-2")) {
			// ap = new BUPT2(mUserName, mUserPwd);
			// } else if (this.apName.contains("BUPT-3")) {
			// ap = new BUPT3(mUserName, mUserPwd);
			// } else if(this.apName.contains("ChinaUnicom")){
			// ap = new ChinaUnicom(mUserName, mUserPwd);
			// }
			else {
				ap = new BUPT_OTHER(mUserName, mUserPwd);
				ap.mSsid = this.apName;
			}
		}
	}

	public void run() {
		initAP();
		System.out.println(ap.mSsid);
		int what = ap.startConnect();
		mHandler.sendEmptyMessage(what);

		if (what == 201) {
			Message msg = new Message();
			msg.obj = ap.mSsid;
			msg.what = NetMethod.testConn();
			mHandler.sendMessage(msg);
		}
	}

	public int getAp_type() {
		return ap_type;
	}

	public void setAp_type(int ap_type) {
		this.ap_type = ap_type;
	}

}
