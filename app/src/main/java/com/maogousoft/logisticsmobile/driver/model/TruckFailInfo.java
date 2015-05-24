package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * Created by aliang on 2015/5/24.
 */
public class TruckFailInfo implements Serializable {

    private String order_id; //货单ID
    private int responsible_people; //责任人 1托运方、2承运方 3配载方
    private String evidence_material; //补充证据材料
    private String user_id;//装车不成功的反应用户
    private String evicdence_pic0;//图片证据
    private String evicdence_pic1;
    private String evicdence_pic2;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getResponsible_people() {
        return responsible_people;
    }

    public void setResponsible_people(int responsible_people) {
        this.responsible_people = responsible_people;
    }

    public String getEvidence_material() {
        return evidence_material;
    }

    public void setEvidence_material(String evidence_material) {
        this.evidence_material = evidence_material;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEvicdence_pic0() {
        return evicdence_pic0;
    }

    public void setEvicdence_pic0(String evicdence_pic0) {
        this.evicdence_pic0 = evicdence_pic0;
    }

    public String getEvicdence_pic1() {
        return evicdence_pic1;
    }

    public void setEvicdence_pic1(String evicdence_pic1) {
        this.evicdence_pic1 = evicdence_pic1;
    }

    public String getEvicdence_pic2() {
        return evicdence_pic2;
    }

    public void setEvicdence_pic2(String evicdence_pic2) {
        this.evicdence_pic2 = evicdence_pic2;
    }
}
