package com.maogousoft.logisticsmobile.driver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;
import com.maogousoft.logisticsmobile.driver.activity.home.NewSourceActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.SourceDetailActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.InformationActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.MessageInfo;
import com.maogousoft.logisticsmobile.driver.model.PushBean;
import com.maogousoft.logisticsmobile.driver.model.UserInfo;
import com.maogousoft.logisticsmobile.driver.utils.LocHelper;
import com.maogousoft.logisticsmobile.driver.utils.LocHelper.LocCallback;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.MD5;
import org.jivesoftware.smack.packet.Message;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Collection;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyPushReceiver extends BroadcastReceiver {

    private static final String TAG = "MyReceiver";

    private NotificationManager mNotificationManager;

    // // 上报位置，订单号
    // private String update_loc_order_id = null;

    private PushBean pushBean;

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {

            String msg = intent.getStringExtra(JPushInterface.EXTRA_MESSAGE);
            LogUtil.i(TAG, "接受到推送下来的自定义消息: " + msg);

            // int msgType = 0;
            // String content = null;
            // int order_id = 0;

            if (mNotificationManager == null) {
                mNotificationManager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
            }

            JSONObject json;
            pushBean = new PushBean();

            try {

                // {"cargo_desc":"糖葫芦","id":100000297,"cargo_unit_name":"吨","validate_time":"2013-06-27 23:05:21.0","cargo_unit":2,"msg_type":5,"cargo_number":3}

                json = new JSONObject(msg);
                if (json.has("cargo_desc")) {
                    String cargo_desc = json.getString("cargo_desc");
                    pushBean.setCargo_desc(cargo_desc);
                }
                if (json.has("id")) {
                    int id = json.getInt("id");
                    pushBean.setId(id);
                }
                if (json.has("cargo_unit_name")) {
                    String cargo_unit_name = json.getString("cargo_unit_name");
                    pushBean.setCargo_unit_name(cargo_unit_name);
                }
                if (json.has("validate_time")) {
                    String validate_time = json.getString("validate_time");
                    pushBean.setValidate_time(validate_time);
                }
                if (json.has("cargo_unit")) {
                    int cargo_unit = json.getInt("cargo_unit");
                    pushBean.setCargo_unit(cargo_unit);
                }
                if (json.has("msg_type")) {
                    int msg_type = json.getInt("msg_type");
                    pushBean.setMsg_type(msg_type);
                }
                if (json.has("cargo_number")) {
                    int cargo_number = json.getInt("cargo_number");
                    pushBean.setCargo_number(cargo_number);
                }

                if (json.has("order_id")) {
                    int order_id = json.getInt("order_id");
                    pushBean.setOrder_id(order_id);
                }

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            // 1普通文本，
            // 2图片消息，
            // 3语音消息，
            // 4位置信息
            // 5 新货源消息
            // 6 司机位置上报命令
            // 7 信息中心收到新消息推送

            switch (pushBean.getMsg_type()) {
                case 5:
                    // {"cargo_desc":"糖葫芦","id":100000297,"cargo_unit_name":"吨","validate_time":"2013-06-27 23:05:21.0","cargo_unit":2,"msg_type":5,"cargo_number":3}
                    MGApplication application = (MGApplication) context.getApplicationContext();
                    if(application.getUserType() == Constants.USER_SHIPPER) {
                        //货主不应收到货源推送消息
                        return;
                    }
                    StringBuffer sb = new StringBuffer();
                    sb.append("有新的货源,");
                    sb.append(pushBean.getCargo_desc());
                    sb.append(",");
                    sb.append(pushBean.getCargo_number());
                    Integer unitPrice = pushBean.getCargo_unit();
                    if (unitPrice != null && unitPrice > 0) {
                        String[] unitPriceStr = context.getResources().getStringArray(R.array.car_price_unit);
                        for (int i = 0; i < Constants.unitTypeValues.length; i++) {
                            if (Constants.unitTypeValues[i] == unitPrice) {
                                sb.append(unitPriceStr[i]);
                            }
                        }
                    }

                    mNotificationManager.notify(0, getOrderNotification(context, sb.toString(), pushBean.getId()));
                    break;
                case 6:
                    LocHelper.getInstance(context).getResult(new LocCallback() {

                        @Override
                        public void onRecivedLoc(double lat, double lng, String addr) {
                            if (lat == 0 || lng == 0 || TextUtils.isEmpty(addr)) {
                                return;
                            }

                            // SendFood.this.lat = lat;
                            // SendFood.this.lng = lng;

                            // order_id int 否 订单号
                            //
                            // location String 是 位置字符串
                            // longitude double 是 经度
                            // latitude double 是 纬度

                            LogUtil.i("MessageBroadCastReceiver", "获取到位置");

                            MGApplication application = (MGApplication) context.getApplicationContext();

                            JSONObject jsonObject = new JSONObject();
                            try {
                                String submitJson = new JSONObject()
                                            .put("isTimerChange", 1)//1为定时上报,0为资料完善
                                            .put("address", addr)
                                            .put("longitude", lng)
                                            .put("latitude", lat).toString();

                                jsonObject.put(Constants.ACTION, Constants.DRIVER_UPDATE_LOCATION);
                                jsonObject.put(Constants.TOKEN, application.getToken());
                                jsonObject.put(Constants.JSON, submitJson);
                                ApiClient.doWithObject(Constants.DRIVER_SERVER_URL,
                                        jsonObject, null, new AjaxCallBack() {

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

                    break;
                case 7:
                    mNotificationManager.notify(0, getInfoCenterNotification(context, "信息中心有新的消息"));
                    break;

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

    private void test(Message message) {
        final MessageInfo messageInfo = new MessageInfo();
        final String msgFrom = message.getFrom().split("@")[0];
        final String msgTo = message.getTo().split("@")[0];
        final Collection<String> names = message.getPropertyNames();
        messageInfo.setMsgContent(message.getBody());
        messageInfo.setMsgFrom(msgFrom);
        messageInfo.setMsgTo(msgTo);
        if (names.contains("msgId")) {
            messageInfo.setMsgId(Long.parseLong(message.getProperty("msgId")
                    .toString()));
        }
        if (names.contains("msgType")) {
            messageInfo.setMsgType(Integer.parseInt(message.getProperty(
                    "msgType").toString()));
        }

    }

    // // 跳入聊天界面
    // private Notification getChatNotification(Context context, String title,
    // String message, String msgFrom) {
    //
    // LogUtil.e("wst", "聊天，msgFrom是：" + msgFrom);
    //
    // Notification notification = new Notification(R.drawable.ic_launcher,
    // title, System.currentTimeMillis());
    // final Intent intent = new Intent(context, ChatListActivity.class);
    // Bundle bundle = new Bundle();
    // bundle.putString("user_id", msgFrom);
    // intent.putExtras(bundle);
    // PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
    // intent, PendingIntent.FLAG_UPDATE_CURRENT);
    //
    // notification.sound = getSound(context);
    //
    // notification.setLatestEventInfo(context, title, message, pendingIntent);
    // notification.flags |= Notification.FLAG_AUTO_CANCEL;
    // return notification;
    // }

    // 跳入新货源详情
    private static Notification getOrderNotification(Context context, String title, int order_id) {
        Notification notification = new Notification(R.drawable.ic_launcher, title, System.currentTimeMillis());
        Intent intent = new Intent(context, NewSourceActivity.class);
        intent.putExtra("QUERY_MAIN_LINE_ORDER", true);
        intent.putExtra(SourceDetailActivity.ORDER_ID, order_id);
//        intent.putExtra("type", "MessageBroadCastReceiver");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, title, "点击查看关注货源", pendingIntent);
        notification.sound = getSound(context);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        return notification;
    }

    // 跳入信息中心
    private Notification getInfoCenterNotification(Context context, String title) {
        Notification notification = new Notification(R.drawable.ic_launcher,
                title, System.currentTimeMillis());
        final Intent intent = new Intent(context, InformationActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification
                .setLatestEventInfo(context, title, "点击查看详情", pendingIntent);
        notification.sound = getSound(context);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        return notification;
    }

    private static Uri getSound(Context ctx) {
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

                                    // 写入用户信息
                                    application.setUserInfo(userInfo);
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
