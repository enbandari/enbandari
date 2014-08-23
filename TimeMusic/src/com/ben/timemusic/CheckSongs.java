package com.ben.timemusic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ben.data.Constant;
import com.ben.data.ItemListAdapter;
import com.ben.receiverpack.UIStateReceiver;
import com.ben.servicepack.MusicService;

public class CheckSongs extends Activity {
	private ListView songListView = null;
	private ItemListAdapter itemAdapter = null;
	private ArrayList<HashMap<String, String>> songListContent = null;
	private UIStateReceiver uir = null;

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(uir);
		super.onDestroy();
	}

	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			songListContent.get(msg.what).remove("operate");
			songListContent.get(MusicService.currentSong).put("operate", "play");
			itemAdapter.notifyDataSetChanged();
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.checkmusic);

		IntentFilter intentFilter = new IntentFilter(Constant.ACTION_UPDATE_ACTIVITY);
		uir = new UIStateReceiver();
		this.registerReceiver(uir, intentFilter);

		songListView = (ListView) this.findViewById(R.id.songlist);
		this.songListContent = this.readSongList();
		itemAdapter = new ItemListAdapter(this.songListContent, this);
		songListView.setAdapter(itemAdapter);
		songListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent itPlayCurrSong = new Intent();

				itPlayCurrSong.putExtra("CurrentSong", songListContent.get(position).get("path"));
				itPlayCurrSong.setAction(Constant.ACTION_UPDATE_SERVICE);
				if (MusicService.isPlay) {

					if (MusicService.currentSong == position) {
						itPlayCurrSong.putExtra("operate", Constant.PAUSE_STATE);
						songListContent.get(position).put("operate", "pause");
					} else {
						itPlayCurrSong.putExtra("operate", Constant.PLAY_STATE);
						songListContent.get(MusicService.currentSong).remove("operate");
						songListContent.get(position).put("operate", "play");
						MusicService.currentSong = position;
					}

					sendBroadcast(itPlayCurrSong);
				} else {
					if (MusicService.currentSong == position) {
						itPlayCurrSong.putExtra("operate", Constant.CONTINUE_STATE);
						songListContent.get(position).put("operate", "play");
						sendBroadcast(itPlayCurrSong);
					} else {
						itPlayCurrSong.setClass(CheckSongs.this, MusicService.class);
						CheckSongs.this.stopService(itPlayCurrSong);
						CheckSongs.this.startService(itPlayCurrSong);
						songListContent.get(MusicService.currentSong).remove("operate");
						songListContent.get(position).put("operate", "play");
					}
				}
				itemAdapter.notifyDataSetChanged();
			}
		});
	}

	public ArrayList<HashMap<String, String>> readSongList() {
		ArrayList<HashMap<String, String>> songList = new ArrayList<HashMap<String, String>>();

		File sdCardDir = Environment.getExternalStorageDirectory();
		File file = new File(sdCardDir + File.separator + "TimerMusic" + File.separator + "songlist.list");
		if (!file.exists()) {
			return songList;
		}
		try {
			Scanner sc = new Scanner(new FileInputStream(file));
			sc.useDelimiter("\\n");
			while (sc.hasNext()) {
				HashMap<String, String> song = new HashMap<String, String>();
				String songPath = sc.next();
				String[] nameTemp = songPath.substring(1).split(File.separator);
				song.put("name", nameTemp[nameTemp.length - 1]);
				song.put("path", songPath);
				song.put("icon", "1");
				songList.add(song);

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (MusicService.isPlay) {
			songList.get(MusicService.currentSong).put("operate", "play");
		}
		return songList;
	}
}
