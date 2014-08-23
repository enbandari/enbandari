package com.ben.receiverpack;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ben.data.Constant;
import com.ben.servicepack.MusicService;
import com.ben.util.AirplaneModeControl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class TimeReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
			// if (AirplaneModeControl.isAirplaneModeOn(context)) {
			// AirplaneModeControl.setAirplaneMode(context, false);
			// }

			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat simpleDate = new SimpleDateFormat("kk:mm");

			SharedPreferences sp = context.getSharedPreferences(Constant.SHAREDPREFERENCES_CALLING, Context.MODE_PRIVATE);
			String callingTime = sp.getString(Constant.CALLING_TIME, null);
			// String callingNumber = sp.getString(Constant.CALLING_NUMBER,
			// null);
			String curtime = simpleDate.format(date);
			System.out.println("当前时间" + curtime + "启动时间" + callingTime);
			if (curtime.equals(callingTime)) {
				// Intent callingIntent = new Intent(Intent.ACTION_CALL,
				// Uri.parse("tel:" + callingNumber));
				// callingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// context.startActivity(callingIntent);
				Intent musicIntent = new Intent(context, MusicService.class);
				context.startService(musicIntent);
			}
		}
	}

}
