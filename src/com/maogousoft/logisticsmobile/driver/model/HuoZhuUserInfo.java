package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * 货主个人信息
 * 
 * @version 1.0.0
 * @author wst
 * @date 2013-6-6 下午5:02:21
 */
public class HuoZhuUserInfo implements Serializable {

	// 货主公司名字、地址、联系手机、座机电话

	/**
	 * 
	 */
	private static final long serialVersionUID = -6581632252578946774L;
	private String phone = "";	// 手机号码
	private String name = "";	// 姓名
	private String company_name = "";	// 公司名称
	private String address = "";// 地址
	private String telcom = "";// 座机

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelcom() {
		return telcom;
	}

	public void setTelcom(String telcom) {
		this.telcom = telcom;
	}

}
