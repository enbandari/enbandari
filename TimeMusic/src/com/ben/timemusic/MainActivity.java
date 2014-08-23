package com.ben.timemusic;

import java.util.Calendar;
import java.util.TimeZone;

import com.ben.data.Constant;
import com.ben.servicepack.MusicService;
import com.ben.servicepack.TimeService;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {
	private final static int REQUEST_CODE = 1;
	private Button startService = null;
	private Button stopService = null;
	private Button startMusic = null;
	private Button stopMusic = null;
	private Button selectSongs = null;
	private Button checkSongList = null;
	private TimePicker timePicker = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		timePicker = (TimePicker) this.findViewById(R.id.callingtime);
		timePicker.setIs24HourView(true);
		// 通过Calendar获取当前时分
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		// 设置时分初值为当前时刻
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);

		startService = (Button) this.findViewById(R.id.startservice);
		startService.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				String time = String.format("%1$02d:%2$02d",
						MainActivity.this.timePicker.getCurrentHour(),
						MainActivity.this.timePicker.getCurrentMinute());
				SharedPreferences sp = MainActivity.this.getSharedPreferences(
						Constant.SHAREDPREFERENCES_CALLING, MODE_PRIVATE);
				Editor editor = sp.edit();
				// editor.putString(Constant.CALLING_NUMBER, number);
				editor.putString(Constant.CALLING_TIME, time);
				System.out.println("设置启动时间"+time);
				editor.commit();
				//
				// System.out.println(number + "=====" + time);
				// if (number.matches("^\\d{11}$")) {
				Intent serviceIntent = new Intent(MainActivity.this,
						TimeService.class);
				MainActivity.this.startService(serviceIntent);
				Toast.makeText(MainActivity.this,
						Constant.SUCCESS_WHEN_START_SERVICE, Toast.LENGTH_SHORT)
						.show();
				// } else {
				// Toast.makeText(MainActivity.this,
				// Constant.ERROR_INPUT_NUMBER, Toast.LENGTH_SHORT)
				// .show();
				// Toast.makeText(MainActivity.this,
				// Constant.ERROR_WHEN_START_SERVICE,
				// Toast.LENGTH_SHORT).show();
				// }

			}
		});

		stopService = (Button) this.findViewById(R.id.stopservice);
		stopService.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent serviceIntent = new Intent(MainActivity.this,
						TimeService.class);
				MainActivity.this.stopService(serviceIntent);
			}

		});

		startMusic = (Button) this.findViewById(R.id.startmusic);
		startMusic.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent musicIntent = new Intent();
				musicIntent.setClass(MainActivity.this, MusicService.class);
				MainActivity.this.startService(musicIntent);
				Toast.makeText(MainActivity.this, Constant.START_PLAY_MUSIC,
						Toast.LENGTH_SHORT).show();
			}

		});

		stopMusic = (Button) this.findViewById(R.id.stopmusic);
		stopMusic.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent musicIntent = new Intent();
				musicIntent.setClass(MainActivity.this, MusicService.class);
				MainActivity.this.stopService(musicIntent);
				Toast.makeText(MainActivity.this, Constant.STOP_PLAY_MUSIC,
						Toast.LENGTH_SHORT).show();
			}

		});

		selectSongs = (Button) this.findViewById(R.id.selectsong);
		selectSongs.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent selectIntent = new Intent(MainActivity.this,
						SelectSong.class);
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					MainActivity.this.startActivityForResult(selectIntent,
							REQUEST_CODE);
				} else {
					// 输出错误信息
				}
			}

		});

		checkSongList = (Button) this.findViewById(R.id.checksonglist);
		checkSongList.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				Intent songListIntent = new Intent();
				songListIntent.setClass(MainActivity.this, CheckSongs.class);
				MainActivity.this.startActivity(songListIntent);

			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE) {
			if (resultCode == SelectSong.RESULT_CODE) {
//				SharedPreferences sp = MainActivity.this.getSharedPreferences(
//						Constant.SHAREDPREFERENCES_CALLING, MODE_PRIVATE);
//				Editor editor = sp.edit();
//				Bundle bundle = data.getExtras();
//				String str = bundle.getString("back");
//				editor.putString(Constant.MUSIC_PATH, str);
//				editor.commit();
//
//				Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG)
//						.show();
			}
		}
	}
}
