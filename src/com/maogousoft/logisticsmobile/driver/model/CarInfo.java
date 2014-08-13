package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * 车源信息
 *
 * @author ybxiang 管理车源界面：线路，载重，车型 车源详细界面：线路，车牌号，车长，车型，载重，报价，位置，联系人，手机
 */
public class CarInfo implements Serializable{
    private int start_province; // 路线
    private int start_city; // 路线
    private int start_district; // 路线
    private int end_province; // 路线
    private int end_city; // 路线
    private int end_district; // 路线
    private int car_weight;// 载重
    private int car_type;// 车型
    private String plate_number = "";// 车牌号
    private String car_length;// 车长
    private String price;// 报价
    private int units;// 报价单位
    private String location;// 位置
    private String ower_name = "";// 联系人
    private String ower_phone = "";// 手机
    private String description = "";// 补充说明
    private String location_time; // 定位时间==我的车队
    private long pulish_date;
    private String wayStartStr;
    private String wayEndStr;

    public CarInfo() {
        super();
    }


    public CarInfo(int start_province, int start_city, int start_district, int end_province,
                   int end_city, int end_district, int car_weight, int car_type, String plate_number,
                   String car_length, String price, int units, String location, String ower_name,
                   String ower_phone, String description, String location_time, long pulish_date) {
        this.start_province = start_province;
        this.start_city = start_city;
        this.start_district = start_district;
        this.end_province = end_province;
        this.end_city = end_city;
        this.end_district = end_district;
        this.car_weight = car_weight;
        this.car_type = car_type;
        this.plate_number = plate_number;
        this.car_length = car_length;
        this.price = price;
        this.units = units;
        this.location = location;
        this.ower_name = ower_name;
        this.ower_phone = ower_phone;
        this.description = description;
        this.location_time = location_time;
        this.pulish_date = pulish_date;
    }

    public int getStart_province() {
        return start_province;
    }

    public void setStart_province(int start_province) {
        this.start_province = start_province;
    }

    public int getStart_city() {
        return start_city;
    }

    public void setStart_city(int start_city) {
        this.start_city = start_city;
    }

    public int getStart_district() {
        return start_district;
    }

    public void setStart_district(int start_district) {
        this.start_district = start_district;
    }

    public int getEnd_province() {
        return end_province;
    }

    public void setEnd_province(int end_province) {
        this.end_province = end_province;
    }

    public int getEnd_city() {
        return end_city;
    }

    public void setEnd_city(int end_city) {
        this.end_city = end_city;
    }

    public int getEnd_district() {
        return end_district;
    }

    public void setEnd_district(int end_district) {
        this.end_district = end_district;
    }

    public int getCar_weight() {
        return car_weight;
    }

    public void setCar_weight(int car_weight) {
        this.car_weight = car_weight;
    }

    public int getCar_type() {
        return car_type;
    }

    public void setCar_type(int car_type) {
        this.car_type = car_type;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getCar_length() {
        return car_length;
    }

    public void setCar_length(String car_length) {
        this.car_length = car_length;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOwer_name() {
        return ower_name;
    }

    public void setOwer_name(String ower_name) {
        this.ower_name = ower_name;
    }

    public String getOwer_phone() {
        return ower_phone;
    }

    public void setOwer_phone(String ower_phone) {
        this.ower_phone = ower_phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation_time() {
        return location_time;
    }

    public void setLocation_time(String location_time) {
        this.location_time = location_time;
    }

    public long getPulish_date() {
        return pulish_date;
    }

    public void setPulish_date(long pulish_date) {
        this.pulish_date = pulish_date;
    }

    public String getWayStartStr() {
        return wayStartStr;
    }

    public void setWayStartStr(String wayStartStr) {
        this.wayStartStr = wayStartStr;
    }

    public String getWayEndStr() {
        return wayEndStr;
    }

    public void setWayEndStr(String wayEndStr) {
        this.wayEndStr = wayEndStr;
    }
}
