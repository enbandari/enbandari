package com.ben.quickwifi;

import android.content.Context;
import android.os.Handler;

import com.ben.ap.AP;
import com.ben.ap.BUPT1;
import com.ben.ap.BUPT2;
import com.ben.ap.BUPT3;
import com.ben.ap.BUPT_OTHER;

public class NetThreadOff extends Thread {
	private String apName;
	private AP ap;
	private Context mContext;
	private Handler mHandler;


	public NetThreadOff(Handler mHandler, Context mContext, String apName) {
		super();
		this.mHandler = mHandler;
		this.mContext = mContext;
		this.apName = apName;
	}

	private void initAP() {
		if (this.apName.equals("BUPT-1")) {
			ap = new BUPT1();
		} else if (this.apName.equals("BUPT-2")) {
			ap = new BUPT2();
		} else if (this.apName.equals("BUPT-3")) {
			ap = new BUPT3();
		} else {
			ap = new BUPT_OTHER();
			ap.mSsid = this.apName;
		}
	}

	public void run() {
		initAP();
		mHandler.sendEmptyMessage(ap.startDisconnect());
	}


}
