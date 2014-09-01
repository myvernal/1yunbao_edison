package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * Created by aliang on 2014/8/30.
 */
public class CardInfo implements Serializable {
    private int verifyresult = 3;//1 正常 2身份验证不一致 3库中无此号码4身份验证一致，但是无照片
    private String msg;//信息
    private String photo;//照片
    private String id_name;//姓名
    private String id_num;//身份证号码
    private String regioninfo;
    private String gender;//性别
    private String id_year;//出生年
    private String id_month;//出生月
    private String id_day;//出生日
    //验证记录中使用
    private long create_time;//创建日期
    private String status;//验证状态
    private String id_card;//身份证
    private String birthday;//生日

    public int getVerifyresult() {
        return verifyresult;
    }

    public void setVerifyresult(int verifyresult) {
        this.verifyresult = verifyresult;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getId_name() {
        return id_name;
    }

    public void setId_name(String id_name) {
        this.id_name = id_name;
    }

    public String getId_num() {
        return id_num;
    }

    public void setId_num(String id_num) {
        this.id_num = id_num;
    }

    public String getRegioninfo() {
        return regioninfo;
    }

    public void setRegioninfo(String regioninfo) {
        this.regioninfo = regioninfo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId_year() {
        return id_year;
    }

    public void setId_year(String id_year) {
        this.id_year = id_year;
    }

    public String getId_month() {
        return id_month;
    }

    public void setId_month(String id_month) {
        this.id_month = id_month;
    }

    public String getId_day() {
        return id_day;
    }

    public void setId_day(String id_day) {
        this.id_day = id_day;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
