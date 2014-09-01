package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * Created by aliang on 2014/9/1.
 */
public class SafePinanInfo implements Serializable {
    private String name;//保险人
    private int type = 1;//0 太平洋 1 平安
    private String insured_name;//被保险人
    private String insured_phone;//被保险人电话
    private String provinceListArea = "";//地址(省)
    private String provinceListCity = "";//地址(市)
    private String districtList = "";//地址(区域)
    private String lianxiren_name;//联系人
    private String bill_number;//发票号码
    private String shiping_number;//运单号
    private String plate_number;//车牌号
    private String guache_Num;//挂车号
    private String start_date;//起运日期 yyyy-MM-dd
    private String start_area;//起运地(省)
    private String start_city;//起运地(市)
    private String end_area;//目的地(省)
    private String end_city;//目的地(市)
    private String start_area_str;//起运地(省)
    private String start_city_str;//起运地(市)
    private String end_area_str;//目的地(省)
    private String end_city_str;//目的地(市)
    private String peichang_area;//赔款偿付地点
    private int package_type;//包装方式
    private String package_type_str;//包装方式
    private double amount_covered;//保险金额

    public String getPackage_type_str() {
        return package_type_str;
    }

    public void setPackage_type_str(String package_type_str) {
        this.package_type_str = package_type_str;
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

    public String getEnd_city_str() {
        return end_city_str;
    }

    public void setEnd_city_str(String end_city_str) {
        this.end_city_str = end_city_str;
    }

    public String getStart_city_str() {
        return start_city_str;
    }

    public void setStart_city_str(String start_city_str) {
        this.start_city_str = start_city_str;
    }

    private double insurance_charge;//保险费用

    public double getInsurance_charge() {
        return insurance_charge;
    }

    public void setInsurance_charge(double insurance_charge) {
        this.insurance_charge = insurance_charge;
    }

    private int packet_number = 1;//保险险种 平安保险险种只有1

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInsured_name() {
        return insured_name;
    }

    public void setInsured_name(String insured_name) {
        this.insured_name = insured_name;
    }

    public String getInsured_phone() {
        return insured_phone;
    }

    public void setInsured_phone(String insured_phone) {
        this.insured_phone = insured_phone;
    }

    public String getProvinceListArea() {
        return provinceListArea;
    }

    public void setProvinceListArea(String provinceListArea) {
        this.provinceListArea = provinceListArea;
    }

    public String getProvinceListCity() {
        return provinceListCity;
    }

    public void setProvinceListCity(String provinceListCity) {
        this.provinceListCity = provinceListCity;
    }

    public String getDistrictList() {
        return districtList;
    }

    public void setDistrictList(String districtList) {
        this.districtList = districtList;
    }

    public String getLianxiren_name() {
        return lianxiren_name;
    }

    public void setLianxiren_name(String lianxiren_name) {
        this.lianxiren_name = lianxiren_name;
    }

    public String getBill_number() {
        return bill_number;
    }

    public void setBill_number(String bill_number) {
        this.bill_number = bill_number;
    }

    public String getShiping_number() {
        return shiping_number;
    }

    public void setShiping_number(String shiping_number) {
        this.shiping_number = shiping_number;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getGuache_Num() {
        return guache_Num;
    }

    public void setGuache_Num(String guache_Num) {
        this.guache_Num = guache_Num;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getStart_area() {
        return start_area;
    }

    public void setStart_area(String start_area) {
        this.start_area = start_area;
    }

    public String getStart_city() {
        return start_city;
    }

    public void setStart_city(String start_city) {
        this.start_city = start_city;
    }

    public String getEnd_area() {
        return end_area;
    }

    public void setEnd_area(String end_area) {
        this.end_area = end_area;
    }

    public String getEnd_city() {
        return end_city;
    }

    public void setEnd_city(String end_city) {
        this.end_city = end_city;
    }

    public String getPeichang_area() {
        return peichang_area;
    }

    public void setPeichang_area(String peichang_area) {
        this.peichang_area = peichang_area;
    }

    public int getPackage_type() {
        return package_type;
    }

    public void setPackage_type(int package_type) {
        this.package_type = package_type;
    }

    public double getAmount_covered() {
        return amount_covered;
    }

    public void setAmount_covered(double amount_covered) {
        this.amount_covered = amount_covered;
    }

    public int getPacket_number() {
        return packet_number;
    }

    public void setPacket_number(int packet_number) {
        this.packet_number = packet_number;
    }
}
