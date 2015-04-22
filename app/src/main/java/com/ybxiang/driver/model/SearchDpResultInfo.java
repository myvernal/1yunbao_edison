package com.ybxiang.driver.model;

import java.io.Serializable;

/**
 * Created by aliang on 2014/9/6.
 */
public class SearchDpResultInfo implements Serializable {
    private String NAME;//联系人
    private String ADDRESS;//地址
    private String COMPANY_NAME;//名称
    private String FROMAREA;//出发地省
    private String FROMAREA1;//出发地市
    private String ENDAREA;//目的地省
    private String ENDAREA1;//目的地市
    private double LAT;
    private double LNG;
    private String MIAOSU;//描述
    private String PHONE;//联系电话

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getCOMPANY_NAME() {
        return COMPANY_NAME;
    }

    public void setCOMPANY_NAME(String COMPANY_NAME) {
        this.COMPANY_NAME = COMPANY_NAME;
    }

    public String getFROMAREA() {
        return FROMAREA;
    }

    public void setFROMAREA(String FROMAREA) {
        this.FROMAREA = FROMAREA;
    }

    public String getFROMAREA1() {
        return FROMAREA1;
    }

    public void setFROMAREA1(String FROMAREA1) {
        this.FROMAREA1 = FROMAREA1;
    }

    public String getENDAREA() {
        return ENDAREA;
    }

    public void setENDAREA(String ENDAREA) {
        this.ENDAREA = ENDAREA;
    }

    public String getENDAREA1() {
        return ENDAREA1;
    }

    public void setENDAREA1(String ENDAREA1) {
        this.ENDAREA1 = ENDAREA1;
    }

    public double getLAT() {
        return LAT;
    }

    public void setLAT(double LAT) {
        this.LAT = LAT;
    }

    public double getLNG() {
        return LNG;
    }

    public void setLNG(double LNG) {
        this.LNG = LNG;
    }

    public String getMIAOSU() {
        return MIAOSU;
    }

    public void setMIAOSU(String MIAOSU) {
        this.MIAOSU = MIAOSU;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }
}
