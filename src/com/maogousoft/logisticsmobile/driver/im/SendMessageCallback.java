package com.maogousoft.logisticsmobile.driver.im;

import com.maogousoft.logisticsmobile.driver.model.MessageInfo;

/**
 * 发送消息回调
 * 
 * @author fuxianwei
 */
public interface SendMessageCallback {

	// 开始发送消息
	void start(MessageInfo messageInfo);

	// 发送消息失败
	void failed(MessageInfo messageInfo);

	// 发送成功
	void success(MessageInfo messageInfo);
}
