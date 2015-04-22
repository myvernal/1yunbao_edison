package com.maogousoft.logisticsmobile.driver.im;

import com.maogousoft.logisticsmobile.driver.model.MessageInfo;

import android.os.Binder;


public class KBBinder extends Binder {

	private XmppUtil xmppUtil;
	
	public KBBinder(XmppUtil xmppUtil){
		this.xmppUtil=xmppUtil;
	}
	
	/**发送聊天消息**/
	public void sendMessage(MessageInfo messageInfo,SendMessageCallback sendMessageCallback){
		xmppUtil.sendMessage(messageInfo,sendMessageCallback);
	}
}
