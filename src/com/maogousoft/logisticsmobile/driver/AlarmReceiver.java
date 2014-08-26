package com.maogousoft.logisticsmobile.driver;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.UserInfo;
import com.maogousoft.logisticsmobile.driver.utils.LocHelper;
import com.maogousoft.logisticsmobile.driver.utils.LocHelper.LocCallback;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.MD5;

/**
 * 司机位置定时上报(提供给免费定位和附近车源使用)
 */
public class AlarmReceiver extends BroadcastReceiver {

	private static final String TAG = "AlarmReceiver";

	@Override
	public void onReceive(final Context context, Intent intent) {

		LogUtil.i(TAG, "定位、上报位置,开始一次");

		// 启动服务
		LocHelper.getInstance(context).getResult(new LocCallback() {

			@Override
			public void onRecivedLoc(double lat, double lng, String addr) {
				if (lat == 0 || lng == 0 || TextUtils.isEmpty(addr)) {
					return;
				}
				MGApplication application = (MGApplication) context.getApplicationContext();
				JSONObject jsonObject = new JSONObject();
				try {
					String submitJson = new JSONObject()
                            .put("isTimerChange", 1)
                            .put("address", addr)
                            .put("longitude", lng)
							.put("latitude", lat).toString();
					jsonObject.put(Constants.ACTION, Constants.DRIVER_UPDATE_LOCATION);
					jsonObject.put(Constants.TOKEN, application.getToken());
					jsonObject.put(Constants.JSON, submitJson);
					ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject, null, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							switch (code) {
								case ResultCode.RESULT_OK:
									LogUtil.i(TAG, "定位、上报位置成功一次");
									break;
								case ResultCode.RESULT_ERROR:
									LogUtil.i(TAG, "定位、上报位置失败一次");
									if (result instanceof String) {
										if (result.toString().contains("token")) {
											// token异常
											login(context);
										}
									}
									break;
								case ResultCode.RESULT_FAILED:
									LogUtil.i(TAG, "定位、上报位置失败一次");
									if (result instanceof String) {
										if (result.toString().contains("token")) {
											// token异常
											login(context);
										}
									}
									break;

							}
							// login
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

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
			jsonObject.put(Constants.JSON, new JSONObject()
                    .put("phone", username)
					.put("password", MD5.encode(password))
                    .put("device_type", Constants.DEVICE_TYPE).toString());
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
