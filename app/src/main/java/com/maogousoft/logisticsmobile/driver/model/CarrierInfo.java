package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * Created by aliang on 2014/11/11.
 */
public class CarrierInfo implements Serializable {
    private int driver_id;//司机ID
    private float driver_price;//司机报价
    private String name;//司机姓名
    private String phone;//司机手机号
    private String plate_number;//司机车牌号
    private String is_grab_single_car;//抢单车
    private String is_phone_car;//电话车
    private String is_price_car;//报价车

    public String getIs_grab_single_car() {
        return is_grab_single_car;
    }

    public void setIs_grab_single_car(String is_grab_single_car) {
        this.is_grab_single_car = is_grab_single_car;
    }

    public String getIs_phone_car() {
        return is_phone_car;
    }

    public void setIs_phone_car(String is_phone_car) {
        this.is_phone_car = is_phone_car;
    }

    public String getIs_price_car() {
        return is_price_car;
    }

    public void setIs_price_car(String is_price_car) {
        this.is_price_car = is_price_car;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public float getDriver_price() {
        return driver_price;
    }

    public void setDriver_price(float driver_price) {
        this.driver_price = driver_price;
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

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }
}
