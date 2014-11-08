package com.maogousoft.logisticsmobile.driver.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;

/**
 * Created by aliang on 2015/4/14.
 */
public class PhoneBroadcastListener extends BroadcastReceiver {

    private static final String TAG = "PhoneBroadcastListener";

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("action" + intent.getAction());
        //如果是去电
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Constants.CALL_NUMBER = phoneNumber;
            LogUtil.e(TAG, "call OUT:" + phoneNumber);
        } else {
            //查了下android文档，貌似没有专门用于接收来电的action,所以，非去电即来电.
            //如果我们想要监听电话的拨打状况，需要这么几步 :
            /* 第一：获取电话服务管理器TelephonyManager manager = this.getSystemService(TELEPHONY_SERVICE);
            * 第二：通过TelephonyManager注册我们要监听的电话状态改变事件。manager.listen(new MyPhoneStateListener(),
                    * PhoneStateListener.LISTEN_CALL_STATE);这里的PhoneStateListener.LISTEN_CALL_STATE就是我们想要
                    * 监听的状态改变事件，初次之外，还有很多其他事件哦。
            * 第三步：通过extends PhoneStateListener来定制自己的规则。将其对象传递给第二步作为参数。
            * 第四步：这一步很重要，那就是给应用添加权限。android.permission.READ_PHONE_STATE
            */
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
            //设置一个监听器
        }
    }

    PhoneStateListener listener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            //注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    LogUtil.e(TAG, "挂断");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    LogUtil.e(TAG, "接听");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    LogUtil.e(TAG, "响铃:来电号码" + incomingNumber);
                    //输出来电号码
                    break;
            }
        }
    };
}