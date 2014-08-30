package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * 车源信息
 *
 * @author ybxiang 管理车源界面：线路，载重，车型 车源详细界面：线路，车牌号，车长，车型，载重，报价，位置，联系人，手机
 */
public class CarInfo implements Serializable{
    private int id;//货车id
    private int start_province; // 路线
    private int start_city; // 路线
    private int start_district; // 路线
    private int end_province; // 路线
    private int end_city; // 路线
    private int end_district; // 路线
    private int end_province1; // 路线
    private int end_city1; // 路线
    private int end_district1; // 路线
    private int end_province2; // 路线
    private int end_city2; // 路线
    private int end_district2; // 路线
    private int end_province3; // 路线
    private int end_city3; // 路线
    private int end_district3; // 路线
    private int car_weight;// 载重
    private int car_type;// 车型
    private String plate_number = "";// 车牌号
    private String car_length;// 车长
    private String price;// 报价
    private int units;// 报价单位
    private String ower_name = "";// 联系人
    private String ower_phone = "";// 手机
    private String driver_name = "";// 联系人
    private String phone = "";// 手机
    private String description = "";// 补充说明
    private String location_time; // 定位时间==我的车队
    private String location;// 位置
    private String address;//位置
    private long pulish_date;
    private String wayStartStr;
    private String wayEndStr;
    private String remark;//备注

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CarInfo() {
        super();
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

    public int getEnd_province1() {
        return end_province1;
    }

    public void setEnd_province1(int end_province1) {
        this.end_province1 = end_province1;
    }

    public int getEnd_city1() {
        return end_city1;
    }

    public void setEnd_city1(int end_city1) {
        this.end_city1 = end_city1;
    }

    public int getEnd_district1() {
        return end_district1;
    }

    public void setEnd_district1(int end_district1) {
        this.end_district1 = end_district1;
    }

    public int getEnd_province2() {
        return end_province2;
    }

    public void setEnd_province2(int end_province2) {
        this.end_province2 = end_province2;
    }

    public int getEnd_city2() {
        return end_city2;
    }

    public void setEnd_city2(int end_city2) {
        this.end_city2 = end_city2;
    }

    public int getEnd_district2() {
        return end_district2;
    }

    public void setEnd_district2(int end_district2) {
        this.end_district2 = end_district2;
    }

    public int getEnd_province3() {
        return end_province3;
    }

    public void setEnd_province3(int end_province3) {
        this.end_province3 = end_province3;
    }

    public int getEnd_city3() {
        return end_city3;
    }

    public void setEnd_city3(int end_city3) {
        this.end_city3 = end_city3;
    }

    public int getEnd_district3() {
        return end_district3;
    }

    public void setEnd_district3(int end_district3) {
        this.end_district3 = end_district3;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
