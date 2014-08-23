package com.ben.timecall;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button startService = null;
	private Button stopService = null;
	private TimePicker timePicker = null;
	private EditText callingNumber = null;

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
//		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
//
//			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//				String time = String.format("%1$02d:%2$02d",
//						MainActivity.this.timePicker.getCurrentHour(),
//						MainActivity.this.timePicker.getCurrentMinute());
//				System.out.println(time);
//			}
//		});
		callingNumber = (EditText) this.findViewById(R.id.callingNumber);

		startService = (Button) this.findViewById(R.id.startservice);
		startService.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				String number = MainActivity.this.callingNumber.getText()
						.toString().trim();
				String time = String.format("%1$02d:%2$02d",
						MainActivity.this.timePicker.getCurrentHour(),
						MainActivity.this.timePicker.getCurrentMinute());
				SharedPreferences sp = MainActivity.this.getSharedPreferences(
						Constant.SHAREDPREFERENCES_CALLING, MODE_PRIVATE);
				Editor editor = sp.edit();
				editor.putString(Constant.CALLING_NUMBER, number);
				editor.putString(Constant.CALLING_TIME, time);
				editor.commit();

				System.out.println(number + "=====" + time);
				if (number.matches("^\\d{11}$")) {
					Intent serviceIntent = new Intent(MainActivity.this,
							TimeService.class);
					MainActivity.this.startService(serviceIntent);
					Toast.makeText(MainActivity.this,
							Constant.SUCCESS_WHEN_START_SERVICE,
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this,
							Constant.ERROR_INPUT_NUMBER, Toast.LENGTH_SHORT)
							.show();
					Toast.makeText(MainActivity.this,
							Constant.ERROR_WHEN_START_SERVICE,
							Toast.LENGTH_SHORT).show();
				}

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
