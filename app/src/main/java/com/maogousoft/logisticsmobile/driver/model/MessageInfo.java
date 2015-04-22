package com.maogousoft.logisticsmobile.driver.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 消息记录
 * 
 * @author lenovo
 */
public class MessageInfo implements Parcelable {

	// 消息id
	private long msgId = 0l;

	// 消息发送方
	private String msgFrom = "";

	// 消息接收方
	private String msgTo = "";

	// 消息类型
	private Integer msgType = 1;

	// 消息时间
	private long msgTime = 0l;

	// 消息内容
	private String msgContent = "";

	// 创建者
	private String createUser = "";

	// 语音时长
	private Integer audioTime = 0;

	// 消息状态,0已发送，1发送失败
	private Integer msgState = 0;

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public String getMsgFrom() {
		return msgFrom;
	}

	public void setMsgFrom(String msgFrom) {
		this.msgFrom = msgFrom;
	}

	public String getMsgTo() {
		return msgTo;
	}

	public void setMsgTo(String msgTo) {
		this.msgTo = msgTo;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public long getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(long msgTime) {
		this.msgTime = msgTime;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Integer getAudioTime() {
		return audioTime;
	}

	public void setAudioTime(Integer audioTime) {
		this.audioTime = audioTime;
	}

	public Integer getMsgState() {
		return msgState;
	}

	public void setMsgState(Integer msgState) {
		this.msgState = msgState;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeLong(getMsgId());
		dest.writeString(getMsgFrom());
		dest.writeString(getMsgTo());
		dest.writeInt(getMsgType());
		dest.writeLong(getMsgTime());
		dest.writeString(getMsgContent());
		dest.writeString(getCreateUser());
		dest.writeInt(getAudioTime());
		dest.writeInt(getMsgState());
	}

	public static final Parcelable.Creator<MessageInfo> CREATOR = new Creator<MessageInfo>() {

		@Override
		public MessageInfo[] newArray(int size) {
			return new MessageInfo[size];
		}

		@Override
		public MessageInfo createFromParcel(Parcel source) {
			MessageInfo messageInfo = new MessageInfo();
			messageInfo.setMsgId(source.readLong());
			messageInfo.setMsgFrom(source.readString());
			messageInfo.setMsgTo(source.readString());
			messageInfo.setMsgType(source.readInt());
			messageInfo.setMsgTime(source.readLong());
			messageInfo.setMsgContent(source.readString());
			messageInfo.setCreateUser(source.readString());
			messageInfo.setAudioTime(source.readInt());
			messageInfo.setMsgState(source.readInt());
			return messageInfo;
		}
	};
}
