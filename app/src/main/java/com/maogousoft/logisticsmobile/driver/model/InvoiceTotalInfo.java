package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * Created by aliang on 2014/11/5.
 */
public class InvoiceTotalInfo implements Serializable {
    private int history_order_count;
    private int pending_order_count;
    private int shipment_order_count;
    private String has_invite;//是否有可邀约
    private String has_confim_contract;//是否有可确认

    public String getHas_invite() {
        return has_invite;
    }

    public void setHas_invite(String has_invite) {
        this.has_invite = has_invite;
    }

    public String getHas_confim_contract() {
        return has_confim_contract;
    }

    public void setHas_confim_contract(String has_confim_contract) {
        this.has_confim_contract = has_confim_contract;
    }

    public int getHistory_order_count() {
        return history_order_count;
    }

    public void setHistory_order_count(int history_order_count) {
        this.history_order_count = history_order_count;
    }

    public int getPending_order_count() {
        return pending_order_count;
    }

    public void setPending_order_count(int pending_order_count) {
        this.pending_order_count = pending_order_count;
    }

    public int getShipment_order_count() {
        return shipment_order_count;
    }

    public void setShipment_order_count(int shipment_order_count) {
        this.shipment_order_count = shipment_order_count;
    }
}