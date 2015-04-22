package com.maogousoft.logisticsmobile.driver.im;

import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;

public final class ServiceManager {

	private static final String LOGTAG = LogUtil.makeLogTag(ServiceManager.class);

	private Context context;

	private SharedPreferences sharedPrefs;

	private Properties props;

	private String xmppHost;

	private String xmppPort;

	private String callbackActivityPackageName;

	private String callbackActivityClassName;

	public ServiceManager(Context context) {
		this.context = context;

		if (context instanceof Activity) {
			Log.i(LOGTAG, "Callback Activity...");
			Activity callbackActivity = (Activity) context;
			callbackActivityPackageName = callbackActivity.getPackageName();
			callbackActivityClassName = callbackActivity.getClass().getName();
		}

		props = loadProperties();
		xmppHost = props.getProperty("xmppHost", "www.1yunbao.com");
		xmppPort = props.getProperty("xmppPort", "5222");

		sharedPrefs = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPrefs.edit();
		editor.putString(Constants.XMPP_HOST, xmppHost);
		editor.putInt(Constants.XMPP_PORT, Integer.parseInt(xmppPort));
		editor.putString(Constants.CALLBACK_ACTIVITY_PACKAGE_NAME, callbackActivityPackageName);
		editor.putString(Constants.CALLBACK_ACTIVITY_CLASS_NAME, callbackActivityClassName);
		editor.commit();
	}

	public void startService(final ServiceConnection connection) {
		Intent intent = new Intent(context, NotificationService.class);
		context.startService(intent);
		if (connection != null) {
			context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
		}
	}

	public void stopService(final ServiceConnection connection) {
		try {
			Intent intent = new Intent(context, NotificationService.class);
			context.stopService(intent);
			context.unbindService(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Properties loadProperties() {
		Properties props = new Properties();
		try {
			int id = context.getResources().getIdentifier("androidpn", "raw", context.getPackageName());
			props.load(context.getResources().openRawResource(id));
		} catch (Exception e) {
			Log.e(LOGTAG, "Could not find the properties file.", e);
		}
		return props;
	}

	public void setNotificationIcon(int iconId) {
		Editor editor = sharedPrefs.edit();
		editor.putInt(Constants.NOTIFICATION_ICON, iconId);
		editor.commit();
	}

}
