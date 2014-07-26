package com.maogousoft.logisticsmobile.driver.im;

import org.jivesoftware.smack.ConnectionListener;

import android.util.Log;

import com.maogousoft.logisticsmobile.driver.utils.LogUtil;

/**
 * A listener class for monitoring connection closing and reconnection events.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class PersistentConnectionListener implements ConnectionListener {

	private static final String LOGTAG = LogUtil.makeLogTag(PersistentConnectionListener.class);

	private final XmppUtil xmppManager;

	public PersistentConnectionListener(XmppUtil xmppManager) {
		this.xmppManager = xmppManager;
	}

	@Override
	public void connectionClosed() {
		Log.d(LOGTAG, "connectionClosed()...");
	}

	@Override
	public void connectionClosedOnError(Exception e) {
		Log.d(LOGTAG, "connectionClosedOnError()...");
		if (xmppManager.getConnection() != null && xmppManager.getConnection().isConnected()) {
			xmppManager.getConnection().disconnect();
		}
		xmppManager.startReconnectionThread();
	}

	@Override
	public void reconnectingIn(int seconds) {
		Log.d(LOGTAG, "reconnectingIn()...");
	}

	@Override
	public void reconnectionFailed(Exception e) {
		Log.d(LOGTAG, "reconnectionFailed()...");
	}

	@Override
	public void reconnectionSuccessful() {
		Log.d(LOGTAG, "reconnectionSuccessful()...");
	}

}
