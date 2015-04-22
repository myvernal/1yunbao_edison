package com.maogousoft.logisticsmobile.driver.model;

import java.net.URL;

/**
 * 我的好友：好友列表界面
 * headerURL 好友头像
 * name 好友姓名
 * phone 好友手机
 * @author ybxiang
 * 
 */
public class Friends {
	private URL headerURL;
	private String name;
	private String phone;
	
	public Friends() {
		super();
	}

	public Friends(String name, String phone) {
		super();
		this.name = name;
		this.phone = phone;
	}

	public Friends(URL headerURL, String name, String phone) {
		super();
		this.headerURL = headerURL;
		this.name = name;
		this.phone = phone;
	}

	public URL getHeaderURL() {
		return headerURL;
	}

	public void setHeaderURL(URL headerURL) {
		this.headerURL = headerURL;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "Friends [headerURL=" + headerURL + ", name=" + name
				+ ", phone=" + phone + "]";
	}


}
