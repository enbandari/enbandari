package com.ben.quickwifi;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;

public class WifiConnService extends Service {
	private WifiStateReceiver mWifiStateReceiver;
	private Intent itQuickWifi;

	public void onCreate() {
		super.onCreate();
	}

	public void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(mWifiStateReceiver);
		this.stopService(itQuickWifi);
	}

	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		mWifiStateReceiver = new WifiStateReceiver();
		this.registerReceiver(mWifiStateReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
		itQuickWifi = new Intent(WifiConnService.this, QuickWifiService.class);
		itQuickWifi.putExtras(intent.getExtras());
	}

	public IBinder onBind(Intent arg0) {
		return null;
	}

	public class WifiStateReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			int newInt = bundle.getInt("wifi_state");
			switch (newInt) {
			case WifiManager.WIFI_STATE_DISABLED:

				break;
			case WifiManager.WIFI_STATE_DISABLING:

				break;
			case WifiManager.WIFI_STATE_ENABLED:
				startService(itQuickWifi);
				System.out.println("连接服务正在启动！");
				break;
			case WifiManager.WIFI_STATE_ENABLING:
				break;
			default:
				break;
			}
		}
	}
}
