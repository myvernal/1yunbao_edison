package com.ybxiang.driver.model;

import java.io.Serializable;

public class FocusLineInfo implements Serializable {

    private int id;
    // 多少条货源
    private int count = 0;
    // 线路名称
    private String title;
    // 出发地
    private int start_province;
    private int start_city;
    private int start_district;
    // 目的地
    private int end_province;
    private int end_city;
    private int end_district;
    // 出发地str
    private String start_str;
    // 目的地str
    private String end_str;
    // 车型
    private int car_type;
    // 车长
    private int car_length;
    // 运输方式
    private int car_way;
    //零担专用
    private String start_area_str;
    private String end_area_str;
    private String start_city_str;
    private String end_city_str;

    public FocusLineInfo() {
    }

    public String getStart_area_str() {
        return start_area_str;
    }

    public void setStart_area_str(String start_area_str) {
        this.start_area_str = start_area_str;
    }

    public String getEnd_area_str() {
        return end_area_str;
    }

    public void setEnd_area_str(String end_area_str) {
        this.end_area_str = end_area_str;
    }

    public String getStart_city_str() {
        return start_city_str;
    }

    public void setStart_city_str(String start_city_str) {
        this.start_city_str = start_city_str;
    }

    public String getEnd_city_str() {
        return end_city_str;
    }

    public void setEnd_city_str(String end_city_str) {
        this.end_city_str = end_city_str;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getCar_type() {
        return car_type;
    }

    public void setCar_type(int car_type) {
        this.car_type = car_type;
    }

    public int getCar_length() {
        return car_length;
    }

    public void setCar_length(int car_length) {
        this.car_length = car_length;
    }

    public int getCar_way() {
        return car_way;
    }

    public void setCar_way(int car_way) {
        this.car_way = car_way;
    }

    public String getStart_str() {
        return start_str;
    }

    public void setStart_str(String start_str) {
        this.start_str = start_str;
    }

    public String getEnd_str() {
        return end_str;
    }

    public void setEnd_str(String end_str) {
        this.end_str = end_str;
    }

    @Override
    public String toString() {
        return "FocusLineInfo [title=" + title + ", start_province="
                + start_province + ", start_city=" + start_city
                + ", start_district=" + start_district + ", end_province="
                + end_province + ", end_city=" + end_city + ", end_district="
                + end_district + ", car_type=" + car_type + ", car_length="
                + car_length + ", car_way=" + car_way + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + car_length;
        result = prime * result + car_type;
        result = prime * result + car_way;
        result = prime * result + end_city;
        result = prime * result + end_district;
        result = prime * result + end_province;
        result = prime * result + start_city;
        result = prime * result + start_district;
        result = prime * result + start_province;
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FocusLineInfo other = (FocusLineInfo) obj;
        if (car_length != other.car_length)
            return false;
        if (car_type != other.car_type)
            return false;
        if (car_way != other.car_way)
            return false;
        if (end_city != other.end_city)
            return false;
        if (end_district != other.end_district)
            return false;
        if (end_province != other.end_province)
            return false;
        if (start_city != other.start_city)
            return false;
        if (start_district != other.start_district)
            return false;
        if (start_province != other.start_province)
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

}
