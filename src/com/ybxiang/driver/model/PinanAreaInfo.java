package com.ybxiang.driver.model;

import java.io.Serializable;

/**
 * Created by aliang on 2014/9/1.
 */
public class PinanAreaInfo implements Serializable {
    
    private String fID;
    private String province;//省
    private String pcode; //省编号
    private String city;//市
    private String citycode;//市编号
    private String sCode;

    public String getfID() {
        return fID;
    }

    public void setfID(String fID) {
        this.fID = fID;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getsCode() {
        return sCode;
    }

    public void setsCode(String sCode) {
        this.sCode = sCode;
    }
}
