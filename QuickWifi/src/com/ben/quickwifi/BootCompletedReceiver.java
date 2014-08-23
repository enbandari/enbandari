package com.ben.quickwifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {
	
	public void onReceive(Context context, Intent intent) {
		Intent it = new Intent(context, QuickWifiService.class);
		context.stopService(it);
		context.startService(it);
		System.out.println("监听服务正在启动！");
	}
}