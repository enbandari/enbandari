package com.ben.quickwifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;

public class WifiStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("WIFI_STATE");
		Bundle bundle = intent.getExtras();
		int newInt = bundle.getInt("wifi_state");
		switch (newInt) {
		case WifiManager.WIFI_STATE_DISABLED:
			Intent it = new Intent(context, QuickWifiService.class);
			context.stopService(it);
			System.out.println("WIFI_STATE:监听服务正在关闭！");
			break;
		case WifiManager.WIFI_STATE_DISABLING:

			break;
		case WifiManager.WIFI_STATE_ENABLED:
			Intent itstop = new Intent(context, QuickWifiService.class);
			context.stopService(itstop);
			context.startService(itstop);
			System.out.println("WIFI_STATE:监听服务正在启动！");
			break;
		case WifiManager.WIFI_STATE_ENABLING:

			break;
		default:
			break;
		}
	}
}
