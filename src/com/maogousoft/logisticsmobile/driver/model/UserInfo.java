package com.maogousoft.logisticsmobile.driver.model;

/**
 * 登录用户信息
 * 
 * @author admin
 */
public class UserInfo {

	//司机id
	private int driver_id = -1;
    //货主id
    private int id= -1;
	// 授权码
	private String token = "";
	private String name = "";
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

	public int getDriver_id() {
		return driver_id;
	}

	public void setDriver_id(int driver_id) {
		this.driver_id = driver_id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
