package com.maogousoft.logisticsmobile.driver.im;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;

public final class NotificationReceiver extends BroadcastReceiver {

	private static final String LOGTAG = LogUtil.makeLogTag(NotificationReceiver.class);

	public NotificationReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		// 接收到消息的广播
		if (Constants.MESSAGE_RECEIVE.equals(action)) {

		}

	}

}
