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

    private double tpy_1;//基本险 费率
    private double tpy_2;//基本险(易碎) 费率
    private double tpy_3;//基本险(鲜活) 费率
    private double tpy_4;//综合险     费率
    private double tpy_5;//综合险(易碎) 费率
    private double tpy_6;//综合险附加被盗险费率
    private double pa_1;//平安费率

    public double getTpy_1() {
        return tpy_1;
    }

    public void setTpy_1(double tpy_1) {
        this.tpy_1 = tpy_1;
    }

    public double getTpy_2() {
        return tpy_2;
    }

    public void setTpy_2(double tpy_2) {
        this.tpy_2 = tpy_2;
    }

    public double getTpy_3() {
        return tpy_3;
    }

    public void setTpy_3(double tpy_3) {
        this.tpy_3 = tpy_3;
    }

    public double getTpy_4() {
        return tpy_4;
    }

    public void setTpy_4(double tpy_4) {
        this.tpy_4 = tpy_4;
    }

    public double getTpy_5() {
        return tpy_5;
    }

    public void setTpy_5(double tpy_5) {
        this.tpy_5 = tpy_5;
    }

    public double getTpy_6() {
        return tpy_6;
    }

    public void setTpy_6(double tpy_6) {
        this.tpy_6 = tpy_6;
    }

    public double getPa_1() {
        return pa_1;
    }

    public void setPa_1(double pa_1) {
        this.pa_1 = pa_1;
    }

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
