package com.ben.quickwifi;

import com.ben.exception.CrashHandler;

import android.app.Application;

public class CrashApplication extends Application {
	
	public void onCreate() {
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
	}
}
