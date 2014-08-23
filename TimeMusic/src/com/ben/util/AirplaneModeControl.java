package com.ben.util;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class AirplaneModeControl {
	/**
	 * 判断飞航模式的当前状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAirplaneModeOn(Context context) {

		return Settings.System.getInt(context.getContentResolver(),

		Settings.System.AIRPLANE_MODE_ON, 0) != 0;

	}

	/**
	 * 设定飞航模式状态
	 * 
	 * @param context
	 * @param status
	 */
	public static void setAirplaneMode(Context context, boolean status) {
		// 先判断目前是已否开启飞航模式
		boolean isAirplaneModeOn = isAirplaneModeOn(context);
		if ((status && isAirplaneModeOn) || (!status && !isAirplaneModeOn)) {
			return;
		}
		int mode = status ? 1 : 0;
		// 设定飞航模式的状态并广播出去
		Settings.System.putInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, mode);
		Intent i = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		i.putExtra("state", mode);
		context.sendBroadcast(i);
	}
}
