package com.maogousoft.logisticsmobile.driver;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;

import com.maogousoft.logisticsmobile.driver.activity.info.LoginActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.UserInfo;
import com.maogousoft.logisticsmobile.driver.utils.LocHelper;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.MD5;

/**
 * 开机广播
 * 
 * @author wst
 * 
 */
public class RestartReceiver extends BroadcastReceiver {

	private static final String TAG = "RestartReceiver";

	@Override
	public void onReceive(Context ctx, Intent intent) {

		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

			// MGApplication application = (MGApplication) ctx.getApplicationContext();
			// application.startXMPPService();

			login(ctx);
			LogUtil.e(TAG, "接收到开机广播");

			MGApplication application = (MGApplication) ctx.getApplicationContext();
			application.startXMPPService();
			LocHelper.addAlarm(ctx, System.currentTimeMillis(), Constants.LOC_UPDATE_TIME);

			JPushInterface.init(ctx);
			String driverId = application.getDriverId();
			if (!TextUtils.isEmpty(driverId)) {
				JPushInterface.setAliasAndTags(ctx, driverId, null);
			}

		} else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
			LogUtil.i(TAG, "接收到解锁广播");

			boolean result = serviceIsStart(ctx, "com.maogousoft.logisticsmobile.driver.im.NotificationService");

			if (result) {
				LogUtil.i(TAG, "通知服务已经开启");

			} else {
				LogUtil.i(TAG, "通知服务已经停止");
				MGApplication application = (MGApplication) ctx.getApplicationContext();
				application.startXMPPService();
				LocHelper.addAlarm(ctx, System.currentTimeMillis(), Constants.LOC_UPDATE_TIME);
				
				JPushInterface.init(ctx);
				String driverId = application.getDriverId();
				if (!TextUtils.isEmpty(driverId)) {
					JPushInterface.setAliasAndTags(ctx, driverId, null);
				}
			}

		}

		else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			LogUtil.i(TAG, "接收到网络连接改变广播");

			boolean result = serviceIsStart(ctx, "com.maogousoft.logisticsmobile.driver.im.NotificationService");

			if (result) {
				LogUtil.i(TAG, "通知服务已经开启");
			} else {
				LogUtil.i(TAG, "通知服务已经停止");
				MGApplication application = (MGApplication) ctx.getApplicationContext();
				application.startXMPPService();
				LocHelper.addAlarm(ctx, System.currentTimeMillis(), Constants.LOC_UPDATE_TIME);
				
				JPushInterface.init(ctx);
				String driverId = application.getDriverId();
				if (!TextUtils.isEmpty(driverId)) {
					JPushInterface.setAliasAndTags(ctx, driverId, null);
				}
			}
		} else if (intent.getAction().equalsIgnoreCase("android.provier.Telephony.SMS_RECEIVED")) {
			LogUtil.i(TAG, "接收到短信广播");

			boolean result = serviceIsStart(ctx, "com.maogousoft.logisticsmobile.driver.im.NotificationService");

			if (result) {
				LogUtil.i(TAG, "通知服务已经开启");
			} else {
				LogUtil.i(TAG, "通知服务已经停止");
				MGApplication application = (MGApplication) ctx.getApplicationContext();
				application.startXMPPService();
				LocHelper.addAlarm(ctx, System.currentTimeMillis(), Constants.LOC_UPDATE_TIME);
				
				JPushInterface.init(ctx);
				String driverId = application.getDriverId();
				if (!TextUtils.isEmpty(driverId)) {
					JPushInterface.setAliasAndTags(ctx, driverId, null);
				}
			}
		}
	}

	/**
	 * 判断某个服务是否启动
	 * 
	 * @param ctx 上下文
	 * @param className 类名
	 * @return
	 */
	public boolean serviceIsStart(Context ctx, String className) {

		ActivityManager mActivityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> mServiceList = mActivityManager.getRunningServices(100);

		for (int i = 0; i < mServiceList.size(); i++) {
			if (className.equals(mServiceList.get(i).service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	// 登录
	private void login(final Context ctx) {

		final MGApplication application = (MGApplication) ctx.getApplicationContext();

		final String username = application.getUserName();
		final String password = application.getPassword();

		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION, Constants.DRIVER_LOGIN);
			jsonObject.put(Constants.TOKEN, null);
			jsonObject.put(Constants.JSON, new JSONObject().put("phone", username)
					.put("password", MD5.encode(password)).put("device_type", Constants.DEVICE_TYPE).toString());
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject, UserInfo.class, new AjaxCallBack() {

				@Override
				public void receive(int code, Object result) {
					switch (code) {
						case ResultCode.RESULT_OK:
							UserInfo userInfo = (UserInfo) result;

							// 写入用户信息
							application.writeUserInfo(username, password, userInfo.getDriver_id());
							application.setToken(userInfo.getToken());
							application.writeInfo("name", userInfo.getName());
							application.startXMPPService();
							break;
					}
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// public String getUserName(Context ctx) {
	//
	// SharedPreferences mSharedPreferences = ctx.getSharedPreferences("user", Context.MODE_PRIVATE);
	//
	// String result = "";
	// try {
	// result = mSharedPreferences.getString(Constants.XMPP_USERNAME, null);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return result;
	//
	// }
	//
	// /** 获取保存的密码 **/
	// public String getPassword(Context ctx) {
	//
	// SharedPreferences mSharedPreferences = ctx.getSharedPreferences("user", Context.MODE_PRIVATE);
	//
	// String result = "";
	// try {
	// result = mSharedPreferences.getString(Constants.XMPP_PASSWORD, null);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return result;
	// }
}