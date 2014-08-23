package com.ben.util;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class AirplaneModeControl {
	/**
	 * �жϷɺ�ģʽ�ĵ�ǰ״̬
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAirplaneModeOn(Context context) {

		return Settings.System.getInt(context.getContentResolver(),

		Settings.System.AIRPLANE_MODE_ON, 0) != 0;

	}

	/**
	 * �趨�ɺ�ģʽ״̬
	 * 
	 * @param context
	 * @param status
	 */
	public static void setAirplaneMode(Context context, boolean status) {
		// ���ж�Ŀǰ���ѷ����ɺ�ģʽ
		boolean isAirplaneModeOn = isAirplaneModeOn(context);
		if ((status && isAirplaneModeOn) || (!status && !isAirplaneModeOn)) {
			return;
		}
		int mode = status ? 1 : 0;
		// �趨�ɺ�ģʽ��״̬���㲥��ȥ
		Settings.System.putInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, mode);
		Intent i = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		i.putExtra("state", mode);
		context.sendBroadcast(i);
	}
}
