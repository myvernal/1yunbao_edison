package com.maogousoft.logisticsmobile.driver.im;

import java.util.Calendar;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.home.ChatListActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.MessageInfo;
import com.maogousoft.logisticsmobile.driver.model.UserInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.MD5;

/**
 * xmpp消息处理
 * 
 * @author lenovo
 */
public class MessageBroadCastReceiver extends BroadcastReceiver {

	private NotificationManager mNotificationManager;

	// 上报位置，订单号
	private String update_loc_order_id = null;

	private static final String TAG = "MessageBroadCastReceiver";

	@Override
	public void onReceive(final Context context, Intent intent) {

		// Toast.makeText(context, "成功收到推送信息！！！", Toast.LENGTH_LONG).show();

		final String action = intent.getAction();
		if (mNotificationManager == null) {
			mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		// 如果是接收到消息的广播
		if (Constants.MESSAGE_RECEIVE.equals(action)) {
			if (intent.hasExtra(Constants.XMPP_MESSAGE)) {
				MessageInfo messageInfo = intent
						.getParcelableExtra(Constants.XMPP_MESSAGE);
				int msgType = messageInfo.getMsgType();
				String content = messageInfo.getMsgContent();

				// 1普通文本，
				// 2图片消息，
				// 3语音消息，
				// 4位置信息
				// 5 新货源消息
				// 6 司机位置上报命令
				// 7 信息中心收到新消息推送

				switch (msgType) {
				case 1:
					content = messageInfo.getMsgContent();
					mNotificationManager.notify(
							0,
							getChatNotification(context, "收到新消息,请点击查看",
									content, messageInfo.getMsgFrom()));
					break;
				case 2:
					content = "[/图片]";
					mNotificationManager.notify(
							0,
							getChatNotification(context, "收到新消息,请点击查看",
									content, messageInfo.getMsgFrom()));
					break;
				case 3:
					content = "[/语音]";
					mNotificationManager.notify(
							0,
							getChatNotification(context, "收到新消息,请点击查看",
									content, messageInfo.getMsgFrom()));
					break;
				case 4:
					content = "[/位置]";
					mNotificationManager.notify(
							0,
							getChatNotification(context, "收到新消息,请点击查看",
									content, messageInfo.getMsgFrom()));
					break;

				// case 5:
				//
				// NewSourceInfo newSourceInfo = JSON
				// .parseObject(messageInfo.getMsgContent(),
				// NewSourceInfo.class);
				// StringBuffer sb = new StringBuffer();
				// sb.append("有新的货源,");
				// sb.append(newSourceInfo.getCargo_desc());
				// sb.append(",");
				// sb.append(newSourceInfo.getCargo_number());
				//
				// switch (newSourceInfo.getCargo_unit()) {
				// case 1:
				// sb.append("车");
				// break;
				// case 2:
				// sb.append("吨");
				// break;
				// case 3:
				// sb.append("方");
				// break;
				// }
				//
				// mNotificationManager.notify(0,
				// getOrderNotification(context, sb.toString(),
				// newSourceInfo.getId()));
				// break;
				// case 6:
				//
				// if (content.contains("order_id")) {
				// LogUtil.i(TAG, "包含订单号!!!");
				// JSONObject j;
				// try {
				// j = new JSONObject(content);
				// update_loc_order_id = j.getString("order_id");
				// } catch (JSONException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				//
				// } else {
				// LogUtil.i(TAG, "不包含订单号!!!");
				// }
				//
				// LocHelper.getInstance(context.getApplicationContext()).getResult(new
				// LocCallback() {
				//
				// @Override
				// public void onRecivedLoc(double lat, double lng, String addr)
				// {
				// // SendFood.this.lat = lat;
				// // SendFood.this.lng = lng;
				//
				// // order_id int 否 订单号
				// //
				// // location String 是 位置字符串
				// // longitude double 是 经度
				// // latitude double 是 纬度
				//
				// LogUtil.i("MessageBroadCastReceiver", "获取到位置");
				//
				// MGApplication application = (MGApplication)
				// context.getApplicationContext();
				//
				// JSONObject jsonObject = new JSONObject();
				// try {
				//
				// String submitJson = "";
				//
				// if (update_loc_order_id != null) {
				// submitJson = new JSONObject().put("order_id",
				// update_loc_order_id)
				// .put("location", addr).put("longitude", lng).put("latitude",
				// lat)
				// .toString();
				// } else {
				// submitJson = new JSONObject().put("location",
				// addr).put("longitude", lng)
				// .put("latitude", lat).toString();
				// }
				//
				// jsonObject.put(Constants.ACTION,
				// Constants.SHIPPING_ORDER_UPDATE_LOCATION);
				// jsonObject.put(Constants.TOKEN, application.getToken());
				// jsonObject.put(Constants.JSON, submitJson);
				// ApiClient.doWithObject(Constants.DRIVER_SERVER_URL,
				// jsonObject, null,
				// new AjaxCallBack() {
				//
				// @Override
				// public void receive(int code, Object result) {
				//
				// update_loc_order_id = null;
				//
				// switch (code) {
				// case ResultCode.RESULT_OK:
				// LogUtil.i(TAG, "定位、上报位置成功一次");
				// break;
				// case ResultCode.RESULT_ERROR:
				// LogUtil.i(TAG, "定位、上报位置失败一次");
				// if (result instanceof String) {
				//
				// if (result.toString().contains("token")) {
				//
				// // token异常
				// login(context);
				// }
				// }
				// break;
				// case ResultCode.RESULT_FAILED:
				// LogUtil.i(TAG, "定位、上报位置失败一次");
				// if (result instanceof String) {
				// if (result.toString().contains("token")) {
				//
				// // token异常
				// login(context);
				// }
				// }
				// break;
				//
				// }
				// // login
				//
				// }
				// });
				// } catch (JSONException e) {
				// e.printStackTrace();
				// }
				// }
				// });
				//
				// break;
				// case 7:
				// mNotificationManager.notify(0,
				// getInfoCenterNotification(context, "信息中心有新的消息"));
				// break;

				// default:
				// content = messageInfo.getMsgContent();
				// mNotificationManager.notify(
				// 0,
				// getChatNotification(context, "收到新消息,请点击查看", content,
				// messageInfo.getMsgFrom()));
				// break;
				}

			}
		}
	}

	// 跳入聊天界面
	private Notification getChatNotification(Context context, String title,
			String message, String msgFrom) {

		LogUtil.e("wst", "聊天，msgFrom是：" + msgFrom);

		Notification notification = new Notification(R.drawable.ic_launcher,
				title, System.currentTimeMillis());
		final Intent intent = new Intent(context, ChatListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("user_id", msgFrom);
		intent.putExtras(bundle);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		notification.sound = getSound(context);

		notification.setLatestEventInfo(context, title, message, pendingIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		return notification;
	}

	// // 跳入新货源详情
	// private Notification getOrderNotification(Context context, String title,
	// int order_id) {
	// Notification notification = new Notification(R.drawable.ic_launcher,
	// title, System.currentTimeMillis());
	// final Intent intent = new Intent(context, SourceDetailActivity.class);
	// intent.putExtra(SourceDetailActivity.ORDER_ID, order_id);
	// intent.putExtra("type", "MessageBroadCastReceiver");
	// PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
	// intent, PendingIntent.FLAG_UPDATE_CURRENT);
	// notification.setLatestEventInfo(context, title, "点击查看货源详情",
	// pendingIntent);
	// notification.sound = getSound(context);
	// notification.flags |= Notification.FLAG_AUTO_CANCEL;
	// return notification;
	// }
	//
	// // 跳入信息中心
	// private Notification getInfoCenterNotification(Context context, String
	// title) {
	// Notification notification = new Notification(R.drawable.ic_launcher,
	// title, System.currentTimeMillis());
	// final Intent intent = new Intent(context, InformationActivity.class);
	// Bundle bundle = new Bundle();
	// intent.putExtras(bundle);
	// PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
	// intent, PendingIntent.FLAG_UPDATE_CURRENT);
	// notification.setLatestEventInfo(context, title, "点击查看详情", pendingIntent);
	// notification.sound = getSound(context);
	//
	// notification.flags |= Notification.FLAG_AUTO_CANCEL;
	// return notification;
	// }

	private Uri getSound(Context ctx) {
		Uri uri;

		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);

		if (hour > 22 || hour < 7) {
			uri = Uri.parse("android.resource://" + ctx.getPackageName() + "/"
					+ R.raw.silence);
		} else {

			MGApplication app = (MGApplication) ctx.getApplicationContext();

			if (app.checkIsRingNewSource()) {
				uri = Uri.parse("android.resource://" + ctx.getPackageName()
						+ "/" + R.raw.mm);
			} else {
				uri = Uri.parse("android.resource://" + ctx.getPackageName()
						+ "/" + R.raw.silence);
			}
		}

		return uri;

	}

	// 登录
	private void login(final Context ctx) {

		final MGApplication application = (MGApplication) ctx
				.getApplicationContext();

		final String username = application.getUserName();
		final String password = application.getPassword();

		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION, Constants.DRIVER_LOGIN);
			jsonObject.put(Constants.TOKEN, null);
			jsonObject.put(
					Constants.JSON,
					new JSONObject().put("phone", username)
							.put("password", MD5.encode(password))
							.put("device_type", Constants.DEVICE_TYPE)
							.toString());
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					UserInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							switch (code) {
							case ResultCode.RESULT_OK:
								UserInfo userInfo = (UserInfo) result;
								// 写入司机信息
								application.writeUserInfo(username, password,
										userInfo.getDriver_id(), userInfo.getId());
								application.setToken(userInfo.getToken());
								application.writeInfo("name",
										userInfo.getName());
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
