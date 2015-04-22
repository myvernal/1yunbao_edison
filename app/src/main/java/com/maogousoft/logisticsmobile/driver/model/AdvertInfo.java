package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * Created by aliang on 2014/8/11.
 */
public class AdvertInfo implements Serializable {
    private int id; //编号
    private int ad_location;//位置 1-首页 2-MAIN页
    private String ad_title;//标题
    private String ad_img;//图片
    private String ad_link;//链接
    private String create_time;//时间
    private int status;//状态 -1已删除 0-正常

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAd_location() {
        return ad_location;
    }

    public void setAd_location(int ad_location) {
        this.ad_location = ad_location;
    }

    public String getAd_title() {
        return ad_title;
    }

    public void setAd_title(String ad_title) {
        this.ad_title = ad_title;
    }

    public String getAd_img() {
        return ad_img;
    }

    public void setAd_img(String ad_img) {
        this.ad_img = ad_img;
    }

    public String getAd_link() {
        return ad_link;
    }

    public void setAd_link(String ad_link) {
        this.ad_link = ad_link;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
