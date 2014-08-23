package com.ben.timecall;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

public class TimeService extends Service {
	TimeReceiver tr = null;

	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void onCreate() {
		super.onCreate();
		IntentFilter intentfilter = new IntentFilter(Intent.ACTION_TIME_TICK);
		tr = new TimeReceiver();
		this.registerReceiver(tr, intentfilter);

	}

	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(tr);
		Toast.makeText(this, Constant.SUCCESS_WHEN_STOP_SERVICE, Toast.LENGTH_SHORT).show();
	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

	}

	public int onStartCommand(Intent intent, int flags, int startId) {

		return super.onStartCommand(intent, flags, startId);
	}

}
