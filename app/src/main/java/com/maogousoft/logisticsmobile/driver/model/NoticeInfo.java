package com.maogousoft.logisticsmobile.driver.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 信息中心
 * 
 * @author lenovo
 */
public class NoticeInfo implements Parcelable {

	// 消息id
	private int id = -1;

	// 消息类别
	private int category = 0;

	// 消息日期
	private long create_time = 0;

	// 消息标题
	private String title = "";

	// 消息内容
	private String content = "";

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(getId());
		dest.writeInt(getCategory());
		dest.writeLong(getCreate_time());
		dest.writeString(getTitle());
		dest.writeString(getContent());
	}
	public static final Parcelable.Creator<NoticeInfo> CREATOR=new Creator<NoticeInfo>() {
		
		@Override
		public NoticeInfo[] newArray(int size) {
			return new NoticeInfo[size];
		}
		
		@Override
		public NoticeInfo createFromParcel(Parcel source) {
			NoticeInfo noticeInfo=new NoticeInfo();
			noticeInfo.setId(source.readInt());
			noticeInfo.setCategory(source.readInt());
			noticeInfo.setCreate_time(source.readLong());
			noticeInfo.setTitle(source.readString());
			noticeInfo.setContent(source.readString());
			return noticeInfo;
		}
	};
}
