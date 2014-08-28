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
