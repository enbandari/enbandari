package com.ben.receiverpack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ben.timemusic.CheckSongs;

public class UIStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		int lastSong = intent.getIntExtra("lastsong", 0);
		CheckSongs mContext = (CheckSongs) context;
		mContext.mHandler.sendEmptyMessage(lastSong);
	}

}
