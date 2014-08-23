package com.ben.receiverpack;

import com.ben.data.Constant;
import com.ben.servicepack.MusicService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class PlayStateReceiver extends BroadcastReceiver {
	private int currPosition=0;
	@Override
	public void onReceive(Context context, Intent intent) {
		// ͨ�����receiver����������Activity�Ĳ���
		// ͨ����Intentȡ�������ж��ǲ��š���ͣ����ֹͣ��
		MusicService mContext = (MusicService) context;
		Bundle bd = intent.getExtras();
		String currPath = bd.getString("CurrentSong");
		int operate = bd.getInt("operate");
		switch (operate) {
		case Constant.PAUSE_STATE:

			mContext.currPosition=mContext.mp.getCurrentPosition();
			mContext.mp.pause();
			mContext.isPlay=false;
			break;
		case Constant.PLAY_STATE:
			mContext.mp.reset();
			mContext.onStart(intent, 0);
			break;
		case Constant.CONTINUE_STATE:
			mContext.mp.seekTo(mContext.currPosition);
			mContext.mp.start();
			mContext.isPlay=true;
			break;
		}
	}

}
