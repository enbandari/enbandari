package com.ben.quickwifi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingActivity extends Activity{
	private ToggleButton mLockDisconn;
	private ToggleButton mWeakDisconn;
	private ToggleButton mNotable;
	private SharedPreferences sp;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.setting);
		sp = this.getSharedPreferences("qw", Context.MODE_PRIVATE);
		this.mLockDisconn = (ToggleButton) this.findViewById(R.id.autolockdisconn);
		this.mWeakDisconn = (ToggleButton) this.findViewById(R.id.autoweakdisconn);
		this.mNotable = (ToggleButton) this.findViewById(R.id.notablebutton);
		this.mLockDisconn.setChecked(sp.getBoolean("lockdisconn", false));
		this.mWeakDisconn.setChecked(sp.getBoolean("weakdisconn", false));
		this.mNotable.setChecked(sp.getBoolean("notable", true));
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Toast.makeText(this, "设置成功，请点击登录重新启动服务！", Toast.LENGTH_SHORT).show();
			this.finish();
		}
		return true;
	}


	protected void onDestroy() {
		super.onDestroy();
		Editor edit = sp.edit();
		edit.putBoolean("lockdisconn", this.mLockDisconn.isChecked());
		edit.putBoolean("weakdisconn", this.mWeakDisconn.isChecked());
		edit.putBoolean("notable", this.mNotable.isChecked());
		edit.commit();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
	
}
