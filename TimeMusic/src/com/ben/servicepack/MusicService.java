package com.ben.servicepack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import com.ben.data.Constant;
import com.ben.receiverpack.PlayStateReceiver;

public class MusicService extends Service {
	public static boolean isPlay = false;
	public static int currentSong = 0;
	public int currPosition=0;
	public MediaPlayer mp = null;
	ArrayList<String> filePathList = null;
	String currPath = null;
	PlayStateReceiver psr= null;

	@Override
	public void onStart(Intent intent, int startId) {

		// 如果是从播放列表直接开启，则需要从当前位置开始播放
		Bundle bd = intent.getExtras();
		if (!(bd == null)) {
			currPath = bd.getString("CurrentSong");
		}

		// 如果是直接启动服务，则从列表开始播放
		System.out.println("获取播放列表");
		filePathList = new ArrayList<String>();
		if (!initPath()) {
			Toast.makeText(this, Constant.ERROR_MUSIC_RES, Toast.LENGTH_SHORT)
					.show();
			this.stopSelf();
			return;
		}

		try {
			mp.setDataSource(this.filePathList.get(currentSong));
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		mp.setLooping(false);
		// if (mp != null) {
		// mp.stop();
		// }
		try {
			mp.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//mp.seekTo(currPosition);
		mp.start();
		this.isPlay = true;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("创建service");
		IntentFilter filterState = new IntentFilter();
		filterState.addAction(Constant.ACTION_UPDATE_SERVICE);
		psr= new PlayStateReceiver();
		this.registerReceiver(psr, filterState);
		
		
		mp = new MediaPlayer();
		mp.setOnCompletionListener(new OnCompletionListener() {

			public void onCompletion(MediaPlayer arg0) {
				
				filePathList = new ArrayList<String>();
				currPath=null;
				if (!initPath()) {
					Toast.makeText(MusicService.this, Constant.ERROR_MUSIC_RES, Toast.LENGTH_SHORT)
							.show();
					MusicService.this.stopSelf();
					return;
				}
				Intent nextSong =new Intent(Constant.ACTION_UPDATE_ACTIVITY);
				nextSong.putExtra("lastsong", currentSong);
				MusicService.this.sendBroadcast(nextSong);
				currentSong++;
				if (currentSong >= filePathList.size()) {
					currentSong = 0;
					
				}
				try {
					arg0.reset();
					arg0.setDataSource(filePathList.get(currentSong));
					arg0.prepare();
					arg0.start();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}

		});

	}

	@Override
	public void onDestroy() {
		if (mp != null) {
			// mp.stop();
			mp.release();
		}
		this.isPlay = false;
		this.unregisterReceiver(psr);
		super.onDestroy();
	}

	public boolean initPath() {
//		SharedPreferences sp = getSharedPreferences(
//				Constant.SHAREDPREFERENCES_CALLING, MODE_PRIVATE);
		// String item = sp.getString(Constant.MUSIC_PATH, "");

		this.filePathList = this.readSongList();
		if (this.filePathList.size() == 0) {
			return false;
		} else {
			if (!(this.currPath == null)) {
				MusicService.currentSong = this.filePathList.indexOf(this.currPath);
				if (MusicService.currentSong == -1) {
					this.filePathList.add(currPath);
					MusicService.currentSong = this.filePathList.indexOf(this.currPath);
				}
			}
			return true;
		}

		// 获得音乐列表
	}

	public void playMusic(){
		
	}
	
	public void start(){
		
	}
	
	public ArrayList<String> readSongList() {
		ArrayList<String> songList = new ArrayList<String>();
		File sdCardDir = Environment.getExternalStorageDirectory();
		File file = new File(sdCardDir + File.separator + "TimerMusic"
				+ File.separator + "songlist.list");
		if (!file.exists()) {
			return songList;
		}
		try {
			Scanner sc = new Scanner(new FileInputStream(file));
			sc.useDelimiter("\\n");
			while (sc.hasNext()) {
				String song = sc.next();
				songList.add(song);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return songList;
	}
}
