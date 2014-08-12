package com.ybxiang.driver.model;

public class FocusLineInfo {

    private int id;
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
    // 车型
    private int car_type;
    // 车长
    private int car_length;
    // 运输方式
    private int car_way;

    public FocusLineInfo() {
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
