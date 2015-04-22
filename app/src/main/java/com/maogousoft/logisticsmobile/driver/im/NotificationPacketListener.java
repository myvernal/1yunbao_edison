package com.maogousoft.logisticsmobile.driver.im;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

import android.content.Intent;
import android.util.Log;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;

/**
 * This class notifies the receiver of incoming notifcation packets
 * asynchronously.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationPacketListener implements PacketListener {

	private static final String LOGTAG = LogUtil.makeLogTag(NotificationPacketListener.class);

	private final XmppUtil xmppManager;

	public NotificationPacketListener(XmppUtil xmppManager) {
		this.xmppManager = xmppManager;
	}

	@Override
	public void processPacket(Packet packet) {
		Log.d(LOGTAG, "NotificationPacketListener.processPacket()...");
		Log.d(LOGTAG, "packet.toXML()=" + packet.toXML());

		if (packet instanceof NotificationIQ) {
			NotificationIQ notification = (NotificationIQ) packet;

			if (notification.getChildElementXML().contains("androidpn:iq:notification")) {
				String notificationId = notification.getId();
				String notificationApiKey = notification.getApiKey();
				String notificationTitle = notification.getTitle();
				String notificationMessage = notification.getMessage();
				// String notificationTicker = notification.getTicker();
				String notificationUri = notification.getUri();

				Intent intent = new Intent(Constants.ACTION_SHOW_NOTIFICATION);
				intent.putExtra(Constants.NOTIFICATION_ID, notificationId);
				intent.putExtra(Constants.NOTIFICATION_API_KEY, notificationApiKey);
				intent.putExtra(Constants.NOTIFICATION_TITLE, notificationTitle);
				intent.putExtra(Constants.NOTIFICATION_MESSAGE, notificationMessage);
				intent.putExtra(Constants.NOTIFICATION_URI, notificationUri);

				xmppManager.getContext().sendBroadcast(intent);
			}
		}

	}

}
