package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * Created by aliang on 2014/8/29.
 */
public class SafeSeaInfo implements Serializable {

    private String insurer_name;//保险人
    private String insured_name;//被保险人
    private String insurer_phone;//保险人电话
    private String insured_phone;//被保险人电话
    private String shiping_number;//标记/发票号码/运单号
    private String cargo_desc;//货物名称及类型
    private String packet_number;//货物数量
    private String ship_type;//运输方式
    private String ship_tool;//运输工具
    private String plate_number;//车牌号
    private String start_date;//起运日期
    private String amount_covered;//保险金额
    private String insurance_type;//保险类型
    private String ratio;//保险费率
    private String insurance_charge;//保险费用
    private String sign_time;//签单日期
    private String create_time;//创建日期
    private String create_user;//创建用户
    private String start_area;//起运地
    private String end_area;//目的地
    private int package_type;//包装代码
    private int cargo_type1;//货物名称及类型1
    private int cargo_type2;//货物名称及类型2
    private int type;//0 太平洋 1 平安

    public String getInsurer_name() {
        return insurer_name;
    }

    public void setInsurer_name(String insurer_name) {
        this.insurer_name = insurer_name;
    }

    public String getInsured_name() {
        return insured_name;
    }

    public void setInsured_name(String insured_name) {
        this.insured_name = insured_name;
    }

    public String getInsurer_phone() {
        return insurer_phone;
    }

    public void setInsurer_phone(String insurer_phone) {
        this.insurer_phone = insurer_phone;
    }

    public String getInsured_phone() {
        return insured_phone;
    }

    public void setInsured_phone(String insured_phone) {
        this.insured_phone = insured_phone;
    }

    public String getShiping_number() {
        return shiping_number;
    }

    public void setShiping_number(String shiping_number) {
        this.shiping_number = shiping_number;
    }

    public String getCargo_desc() {
        return cargo_desc;
    }

    public void setCargo_desc(String cargo_desc) {
        this.cargo_desc = cargo_desc;
    }

    public String getPacket_number() {
        return packet_number;
    }

    public void setPacket_number(String packet_number) {
        this.packet_number = packet_number;
    }

    public String getShip_type() {
        return ship_type;
    }

    public void setShip_type(String ship_type) {
        this.ship_type = ship_type;
    }

    public String getShip_tool() {
        return ship_tool;
    }

    public void setShip_tool(String ship_tool) {
        this.ship_tool = ship_tool;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getAmount_covered() {
        return amount_covered;
    }

    public void setAmount_covered(String amount_covered) {
        this.amount_covered = amount_covered;
    }

    public String getInsurance_type() {
        return insurance_type;
    }

    public void setInsurance_type(String insurance_type) {
        this.insurance_type = insurance_type;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getInsurance_charge() {
        return insurance_charge;
    }

    public void setInsurance_charge(String insurance_charge) {
        this.insurance_charge = insurance_charge;
    }

    public String getSign_time() {
        return sign_time;
    }

    public void setSign_time(String sign_time) {
        this.sign_time = sign_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public String getStart_area() {
        return start_area;
    }

    public void setStart_area(String start_area) {
        this.start_area = start_area;
    }

    public String getEnd_area() {
        return end_area;
    }

    public void setEnd_area(String end_area) {
        this.end_area = end_area;
    }

    public int getPackage_type() {
        return package_type;
    }

    public void setPackage_type(int package_type) {
        this.package_type = package_type;
    }

    public int getCargo_type1() {
        return cargo_type1;
    }

    public void setCargo_type1(int cargo_type1) {
        this.cargo_type1 = cargo_type1;
    }

    public int getCargo_type2() {
        return cargo_type2;
    }

    public void setCargo_type2(int cargo_type2) {
        this.cargo_type2 = cargo_type2;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
