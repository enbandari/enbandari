package com.ben.timemusic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import com.ben.data.ItemListAdapter;
import com.ben.util.DirectoryFilter;
import com.ben.util.FileComparator;
import com.ben.util.MusicFilter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SelectSong extends Activity {
	public static final int RESULT_CODE = 0;
	private String musicPath = null;
	private String currentPath = null;
	private ArrayList<HashMap<String, String>> currList = null;
	private ListView mList = null;
	private ItemListAdapter mAdapter = null;
	private File sdCardDir = null;
	private Set<String> SongSet = null;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.selectmusic);
		SongSet = initSongSet();
		sdCardDir = Environment.getExternalStorageDirectory();
		currList = this.getChildNames(sdCardDir);
		mAdapter = new ItemListAdapter(this.currList, this);
		this.mList = (ListView) this.findViewById(R.id.filelist);
		this.mList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String parentPath = SelectSong.this.currentPath
						+ File.separator
						+ SelectSong.this.currList.get(position).get("name");

				File file = new File(parentPath);

				// 判断file是否为文件夹，如果是则返回其子文件，否则呢。。
				if (file.isDirectory()) {
					SelectSong.this.currList.clear();
					SelectSong.this.currList.addAll(SelectSong.this
							.getChildNames(file));

				} else {
					// SelectSong.this.musicPath = parentPath;
					if (SongSet.contains(parentPath)) {
						SongSet.remove(parentPath);
						SelectSong.this.currList.get(position).put("operate",
								"add");
					} else {
						SongSet.add(parentPath);
						SelectSong.this.currList.get(position).put("operate",
								"del");
					}
				}
				SelectSong.this.mAdapter.notifyDataSetChanged();
			}

		});
		this.mList.setAdapter(mAdapter);
	}

	private Set<String> initSongSet() {
		Set<String> songSet = new HashSet<String>();
		File sdCardDir = Environment.getExternalStorageDirectory();
		File file = new File(sdCardDir + File.separator + "TimerMusic"
				+ File.separator + "songlist.list");
		if (!file.exists()) {
			return songSet;
		}
		try {
			Scanner sc = new Scanner(new FileInputStream(file));
			sc.useDelimiter("\\n");
			while (sc.hasNext()) {
				String song = sc.next();
				songSet.add(song);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return songSet;
	}

	// 获取子文件或者打开音乐文件
	public ArrayList<HashMap<String, String>> getChildNames(File file) {
		ArrayList<HashMap<String, String>> childList = new ArrayList<HashMap<String, String>>();
		this.currentPath = file.getPath();

		File[] childDirs = file.listFiles(new DirectoryFilter());
		File[] childFiles = file.listFiles(new MusicFilter());
		Arrays.sort(childDirs, new FileComparator());
		Arrays.sort(childFiles, new FileComparator());
		for (File fileTemp : childDirs) {
			HashMap<String, String> childInfo = new HashMap<String, String>();
			childInfo.put("name", fileTemp.getName());
			childInfo.put("icon", "0");
			childInfo.put("operate", "enter");
			childList.add(childInfo);
		}
		for (File fileTemp : childFiles) {
			HashMap<String, String> childInfo = new HashMap<String, String>();
			childInfo.put("name", fileTemp.getName());
			childInfo.put("icon", "1");
			if (this.SongSet.contains(fileTemp.getPath())) {
				childInfo.put("operate", "del");
			} else {
				childInfo.put("operate", "add");
			}
			childList.add(childInfo);
		}

		return childList;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			File file = new File(this.currentPath);
			if (file.equals(this.sdCardDir)) {
				this.musicPath = "";
				Intent intent = new Intent();
				intent.putExtra("back", this.musicPath);
				this.setResult(RESULT_CODE, intent);
				saveSongSet();
				this.finish();
			} else {

				SelectSong.this.currList.clear();
				SelectSong.this.currList.addAll(SelectSong.this
						.getChildNames(file.getParentFile()));
				SelectSong.this.mAdapter.notifyDataSetChanged();
			}
		}
		return true;
	}

	private void saveSongSet() {
		File file = new File(this.sdCardDir + File.separator + "TimerMusic"
				+ File.separator + "songlist.list");
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdir();
		}
		OutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Iterator it = this.SongSet.iterator();
		while (it.hasNext()) {
			byte[] buffer = (it.next().toString() + "\n").getBytes();
			try {
				fos.write(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
