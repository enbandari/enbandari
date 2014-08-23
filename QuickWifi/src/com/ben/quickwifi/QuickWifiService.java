package com.ben.quickwifi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.ben.update.UpdateManager;
import com.ben.util.NetMethod;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.Toast;

public class QuickWifiService extends Service {
	private WifiStatusReceiver mWifiStatusReceiver;
	private WifiManager wm;
	private WifiInfo wi;
	private String apName;
	private String mUserName;
	private String mUserPwd;
	private Handler mHandler;
	private SharedPreferences sp;
	private int cnt = 0;
	private Timer timer;

	private ScreenActionReceiver mScreenStateReceiver;
	private Vibrator vibrator;
	private boolean mAutologin = true;
	private boolean mLockDisconn = true;
	private boolean mWeakDisconn = true;
	private boolean mNotable = true;
	private boolean mScreenState = true;
	private boolean mWifiState = false;// 连接状态标记
	private boolean connFlag = false;// 登录状态标记
	private NotificationManager noteMan;
	private Notification wifiConnNote;
	private NetThread netThreadLogin;
	private NetThreadOff netThreadLogoff;

	public IBinder onBind(Intent arg0) {
		return null;
	}

	class ListenerTimer extends TimerTask {

		public void run() {

			wi = wm.getConnectionInfo();
			int strength = wi.getRssi();
			// System.out.println("信号强度是：" + strength);
			// mHandler.sendEmptyMessage(387);
			if (strength < -85 && connFlag) {

				stopConnect();
				connFlag = false;
			} else if (strength > -75 && !connFlag) {
				// 链接
				startConnect();
			}
		}
	}

	private void startTimer() {
		if (this.mWeakDisconn) {
			timer = new Timer();
			timer.schedule(new ListenerTimer(), 1000, 1000);
			System.out.println("启动timer");
		}
	}

	private void stopTimer() {
		if (this.mWeakDisconn) {
			System.out.println(timer);
			if (timer != null) {
				timer.cancel();
				System.out.println("停止timer");

			} else {
				timer = null;
			}
		}
	}

	private void initNote() {
		if (this.mNotable) {
			this.wifiConnNote = new Notification(R.drawable.icon_small, "操作成功^_^", System.currentTimeMillis());
			this.wifiConnNote.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
			this.wifiConnNote.flags |= Notification.FLAG_NO_CLEAR; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
			this.wifiConnNote.contentView = new RemoteViews(this.getPackageName(), R.layout.notify_text);

			if (Build.VERSION.SDK_INT > 10) {
				Intent conIntent = new Intent("LOGIN_WIFI");
				Intent disconIntent = new Intent("LOGOFF_WIFI");
				PendingIntent conPendIntent = PendingIntent.getBroadcast(QuickWifiService.this, 0, conIntent, 0);
				PendingIntent disconPendIntent = PendingIntent.getBroadcast(QuickWifiService.this, 0, disconIntent, 0);

				this.wifiConnNote.contentView.setOnClickPendingIntent(R.id.conbutton, conPendIntent);
				this.wifiConnNote.contentView.setOnClickPendingIntent(R.id.disconbutton, disconPendIntent);
			} else {
				this.wifiConnNote.contentView.setViewVisibility(R.id.conbutton, View.GONE);
				this.wifiConnNote.contentView.setViewVisibility(R.id.disconbutton, View.GONE);
				this.wifiConnNote.contentView.setViewVisibility(R.id.time, View.VISIBLE);
			}
		}
	}

	private void setNote(String action, String text, String tickerText) {
		if (this.mNotable) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Intent notificationIntent = new Intent(); // 点击该通知后要跳转的Activity
			if (Build.VERSION.SDK_INT > 10) {
				notificationIntent.setAction(action);
			} else {
				notificationIntent.setAction("POPDIALOG");
			}
			this.wifiConnNote.contentView.setTextViewText(R.id.text, text);
			this.wifiConnNote.contentView.setTextViewText(R.id.time, sdf.format(new Date()));
			this.wifiConnNote.contentView.setTextViewText(R.id.title, this.apName);
			this.wifiConnNote.contentIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, 0);
			this.wifiConnNote.tickerText = tickerText;
			noteMan.notify(0, wifiConnNote);
		}
	}

	private void popAlertDialog() {
		String tips = "由于程序显示的状态可能与实际情况不符，因此请选择需要操作内容。";
		AlertDialog ad = new AlertDialog.Builder(this).setTitle("选择操作").setIcon(R.drawable.ic_launcher).setMessage(tips)
				.setPositiveButton("注销", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						stopConnect();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).setNeutralButton("登录", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						startConnect();
					}
				}).create();
		ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		ad.show();
	}

	private void cancelNote() {
		if (noteMan != null) {
			noteMan.cancelAll();
		}
	}

	public void onCreate() {
		super.onCreate();
		initNote();
		vibrator = (Vibrator) this.getSystemService(Service.VIBRATOR_SERVICE);
		noteMan = (NotificationManager) this.getSystemService(Service.NOTIFICATION_SERVICE);
		cancelNote();

		final UpdateManager manager = new UpdateManager(QuickWifiService.this);
		// 检查软件更新
		manager.checkUpdate();
		
		mHandler = new Handler() {

			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 200:
					wifiConnNote.contentView.setTextViewText(R.id.title, apName);
					setNote("LOGOFF_WIFI", "已登录，注销请猛击。", apName + "登录成功");
					Toast.makeText(QuickWifiService.this, "登录成功~", Toast.LENGTH_SHORT).show();
					connFlag = true;
					vibrator.vibrate(20);
					startTimer();
					break;
				case 201:
					Toast.makeText(QuickWifiService.this, "登录请求发送成功~", Toast.LENGTH_SHORT).show();
					break;

				case 735:
					cnt++;
					Toast.makeText(QuickWifiService.this, "连接失败" + cnt + "次！", Toast.LENGTH_SHORT).show();
					break;
				case 302:
					// if (cnt < 3) {
					// startConnect();
					// System.out.println(mUserName + "  " + mUserPwd);
					// } else {
					// Toast.makeText(QuickWifiService.this,
					// "连接失败！用户名密码错误或信号较弱都会导致该问题。", Toast.LENGTH_SHORT).show();
					// cnt = 0;
					// }
					break;
				case 555:
					Toast.makeText(QuickWifiService.this, "已断开", Toast.LENGTH_SHORT).show();
					setNote("LOGIN_WIFI", "已连接，登录请猛击。", apName + "注销成功");
					vibrator.vibrate(20);
					break;
				case 755:
					Toast.makeText(QuickWifiService.this, "断开失败", Toast.LENGTH_SHORT).show();
					break;
				case 387:
					Toast.makeText(QuickWifiService.this, "当前网络没有登录", Toast.LENGTH_SHORT).show();
					break;
				case 0:
					Toast.makeText(QuickWifiService.this, "连接失败！用户名密码错误或信号较弱都会导致该问题。", Toast.LENGTH_SHORT).show();
					break;
				case 1024:
					testConnect();
					break;
				default:
					Toast.makeText(QuickWifiService.this, "难道百度挂了？！", Toast.LENGTH_SHORT).show();
					break;
				}
			}

		};

		sp = getSharedPreferences("qw", Context.MODE_PRIVATE);
		this.mUserName = sp.getString("mUserName", null);
		this.mUserPwd = sp.getString("mUserPwd", null);
		this.mAutologin = sp.getBoolean("autologin", false);
		this.mWeakDisconn = sp.getBoolean("weakdisconn", false);
		this.mLockDisconn = sp.getBoolean("lockdisconn", false);
		this.mNotable = sp.getBoolean("notable", true);
		
		if (this.mUserName == null || this.mUserPwd == null) {
			Toast.makeText(this, "获取用户信息错误！服务将停止！", Toast.LENGTH_SHORT).show();
			this.stopSelf();
		}
	}

	public void onDestroy() {
		this.unregisterReceiver(mWifiStatusReceiver);
		this.mScreenStateReceiver.unRegisterScreenActionReceiver(this);
		super.onDestroy();
	}

	public void onStart(Intent intent, int startId) {
		mWifiStatusReceiver = new WifiStatusReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		intentFilter.addAction("LOGOFF_WIFI");
		intentFilter.addAction("LOGIN_WIFI");
		intentFilter.addAction("POPDIALOG");
		this.registerReceiver(mWifiStatusReceiver, intentFilter);

		this.mScreenStateReceiver = new ScreenActionReceiver();
		this.mScreenStateReceiver.registerScreenActionReceiver(this);

		super.onStart(intent, startId);
	}

	private void initWifiName() {
		this.wm = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wi = wm.getConnectionInfo();
		this.apName = wi.getSSID().trim();
	}

	private void startConnect() {
		if (!this.mWifiState) {
			System.out.println("wifi已断开");
			return;
		}
		if ((this.mScreenState || !this.mLockDisconn) && (netThreadLogin == null || !netThreadLogin.isAlive())) {
			int type = sp.getInt("type", -1);
			netThreadLogin = new NetThread(mHandler, QuickWifiService.this, apName, mUserName, mUserPwd);
			netThreadLogin.setAp_type(type);
			netThreadLogin.start();
			
			sp.edit().putInt("type", -1).commit();
		}
	}

	private void stopConnect() {
		if (!this.mWifiState) {
			System.out.println("wifi已断开");
			return;
		}
		if (netThreadLogoff == null || !netThreadLogoff.isAlive()) {
			netThreadLogoff = new NetThreadOff(mHandler, QuickWifiService.this, apName);
			netThreadLogoff.start();

		}
	}

	private void testConnect() {
		new Thread() {
			public void run() {
				if (NetMethod.testConn() == 200) {
					mHandler.sendEmptyMessage(200);
				} else {
					mHandler.sendEmptyMessage(387);
				}
			}

		}.start();
	}

	public class WifiStatusReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
				DetailedState state = ((NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)).getDetailedState();
				changeState(state);
			}
			if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
				SupplicantState state = (SupplicantState) intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
				changeSuppState(state);
			}
			if (intent.getAction().equals("LOGOFF_WIFI")) {
				stopTimer();
				stopConnect();
			}
			if (intent.getAction().equals("LOGIN_WIFI")) {
				System.out.println("不启动吗?");
				startConnect();
			}
			if (intent.getAction().equals("POPDIALOG")) {
				popAlertDialog();
			}
		}
	}

	private void changeState(DetailedState state) {
		// Toast.makeText(QuickWifiService.this, "DetailedState:  " +
		// state.toString(), Toast.LENGTH_SHORT).show();
		switch (state) {
		case SCANNING:
			break;
		case CONNECTING:
			break;
		case OBTAINING_IPADDR:
			break;
		case DISCONNECTED:
			break;
		case CONNECTED:
			// 在这里处理连接的
			mWifiState = true;
			initWifiName();

			if (mAutologin) {
				startConnect();
			}
			setNote("LOGIN_WIFI", "已连接，点此登录。", apName + "连接成功");

			mHandler.sendEmptyMessage(1024);

			System.out.println(mUserName + "  " + mUserPwd);
			break;
		case DISCONNECTING:
			break;
		case FAILED:
			break;
		case IDLE:
			break;
		case SUSPENDED:
			break;

		default:
			break;
		}
	}

	private void changeSuppState(SupplicantState state) {
		// Toast.makeText(QuickWifiService.this, "SupplicantState:  " +
		// state.toString(), Toast.LENGTH_SHORT).show();

		switch (state) {
		case SCANNING:
			break;
		case ASSOCIATING:
			break;
		case FOUR_WAY_HANDSHAKE:
			break;
		case COMPLETED:
			break;
		case DISCONNECTED:
			mWifiState = false;
			stopTimer();
			connFlag = false;
			cancelNote();
			break;
		case ASSOCIATED:
			break;
		case DORMANT:
			break;
		case GROUP_HANDSHAKE:
			break;
		case INACTIVE:
			break;
		case INVALID:
			break;
		case UNINITIALIZED:
			break;

		default:
			break;
		}
	}

	public class ScreenActionReceiver extends BroadcastReceiver {

		private String TAG = "ScreenActionReceiver";
		private boolean isRegisterReceiver = false;

		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_SCREEN_ON)) {
				mScreenState = true;
				startConnect();
			} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
				mScreenState = false;
				stopTimer();
				stopConnect();
				if (mWifiState) {
					setNote("LOGIN_WIFI", "已连接，点此登录。", apName + "注销成功");
				}
			}
		}

		public void registerScreenActionReceiver(Context mContext) {
			if (mLockDisconn && !isRegisterReceiver) {
				isRegisterReceiver = true;

				IntentFilter filter = new IntentFilter();
				filter.addAction(Intent.ACTION_SCREEN_OFF);
				filter.addAction(Intent.ACTION_SCREEN_ON);
				mContext.registerReceiver(ScreenActionReceiver.this, filter);
			}
		}

		public void unRegisterScreenActionReceiver(Context mContext) {
			if (isRegisterReceiver) {
				isRegisterReceiver = false;
				mContext.unregisterReceiver(ScreenActionReceiver.this);
			}
		}

	}

}
