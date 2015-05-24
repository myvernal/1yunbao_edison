package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * Created by EdisonZhao on 15/5/19.
 */
public class AgreementInfo implements Serializable {

    private int order_id;//合同ID
    private int contract_type;//合同类型 1协议 2 三方合同
    private String goods_name;//货物名称
    private String examination;//体积 重量
    private String pack;//包装
    private String number;//件数
    private String loading_address;//装货地址
    private String unload_address;//卸货地址
    private String loading_time;//装货时间
    private String reach_time;//到达时间
    private String carriage;//运费
    private String pay_way;//支付方式
    private String receiving_people_phone;//收货人电话
    private float bond;//保证金
    private String prevent_money;//预付款或代收款
    private String driver_name;//驾驶员姓名
    private String driver_id_card;//身份证号码
    private String driver_phone;//联系电话
    private String car_type;//车型
    private String car_frame_number;//车架号
    private String engine_number;//发动机号
    private String driver_receiving_people_bank;//收款开户行
    private String bank_count;//账号
    private String loading_bond;//本次装货保证金
    private int payment_money;//垫付代收款或平台代收预付款
    private String otherthing;//其他事项
    private int deal_type;//交易类型
    private String consign;//托运方
    private String consign_people;//代表
    private String consign_phone;//联系电话
    private long consign_confim_date;//签约日期
    private String carrier;//承载方
    private String carrier_people;//代表
    private String carrier_phone;//联系电话
    private long carrier_confim_date;//签约日期
    private String stowage;//配载方
    private String stowage_people;//配载方代表
    private String stowage_phone;//配载方联系电话
    private long stowage_confim_date;//配载方签约时间
    private int status;//合同状态 0生成合同 1 签订了合同 3 待结算 4结算合同
    private int driver_id;//司机id
    private int user_id;//货主ID
    private String base_consign_name;//托运方
    private String base_carrier_name;//承运方
    private String base_stowage_name;//配载方
    private String base_consign_count;//托运方账号
    private String base_carrier_count;//承运方账号
    private String base_stowage_count;//配载方账号
    private String base_send_order_name;//发货人
    private String base_plate_number;//车牌号
    private String base_stowage_people;//配载方联系人
    private String base_consign_phone;//托运方电话
    private String base_carrier_phone;//承运方电话
    private String base_stowage_phone;//配载方电话
    private int order_user_id;//托运方货主id
    private int is_forward;//是否转发 0  为转发 1 已转发
    private long create_time;//合同创建时间
    private String contract_page_url;//合同地址

    public String getContract_page_url() {
        return contract_page_url;
    }

    public void setContract_page_url(String contract_page_url) {
        this.contract_page_url = contract_page_url;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getContract_type() {
        return contract_type;
    }

    public void setContract_type(int contract_type) {
        this.contract_type = contract_type;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getExamination() {
        return examination;
    }

    public void setExamination(String examination) {
        this.examination = examination;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLoading_address() {
        return loading_address;
    }

    public void setLoading_address(String loading_address) {
        this.loading_address = loading_address;
    }

    public String getUnload_address() {
        return unload_address;
    }

    public void setUnload_address(String unload_address) {
        this.unload_address = unload_address;
    }

    public String getLoading_time() {
        return loading_time;
    }

    public void setLoading_time(String loading_time) {
        this.loading_time = loading_time;
    }

    public String getReach_time() {
        return reach_time;
    }

    public void setReach_time(String reach_time) {
        this.reach_time = reach_time;
    }

    public String getCarriage() {
        return carriage;
    }

    public void setCarriage(String carriage) {
        this.carriage = carriage;
    }

    public String getPay_way() {
        return pay_way;
    }

    public void setPay_way(String pay_way) {
        this.pay_way = pay_way;
    }

    public String getReceiving_people_phone() {
        return receiving_people_phone;
    }

    public void setReceiving_people_phone(String receiving_people_phone) {
        this.receiving_people_phone = receiving_people_phone;
    }

    public float getBond() {
        return bond;
    }

    public void setBond(float bond) {
        this.bond = bond;
    }

    public String getPrevent_money() {
        return prevent_money;
    }

    public void setPrevent_money(String prevent_money) {
        this.prevent_money = prevent_money;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_id_card() {
        return driver_id_card;
    }

    public void setDriver_id_card(String driver_id_card) {
        this.driver_id_card = driver_id_card;
    }

    public String getDriver_phone() {
        return driver_phone;
    }

    public void setDriver_phone(String driver_phone) {
        this.driver_phone = driver_phone;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getCar_frame_number() {
        return car_frame_number;
    }

    public void setCar_frame_number(String car_frame_number) {
        this.car_frame_number = car_frame_number;
    }

    public String getEngine_number() {
        return engine_number;
    }

    public void setEngine_number(String engine_number) {
        this.engine_number = engine_number;
    }

    public String getDriver_receiving_people_bank() {
        return driver_receiving_people_bank;
    }

    public void setDriver_receiving_people_bank(String driver_receiving_people_bank) {
        this.driver_receiving_people_bank = driver_receiving_people_bank;
    }

    public String getBank_count() {
        return bank_count;
    }

    public void setBank_count(String bank_count) {
        this.bank_count = bank_count;
    }

    public String getLoading_bond() {
        return loading_bond;
    }

    public void setLoading_bond(String loading_bond) {
        this.loading_bond = loading_bond;
    }

    public int getPayment_money() {
        return payment_money;
    }

    public void setPayment_money(int payment_money) {
        this.payment_money = payment_money;
    }

    public String getOtherthing() {
        return otherthing;
    }

    public void setOtherthing(String otherthing) {
        this.otherthing = otherthing;
    }

    public int getDeal_type() {
        return deal_type;
    }

    public void setDeal_type(int deal_type) {
        this.deal_type = deal_type;
    }

    public String getConsign() {
        return consign;
    }

    public void setConsign(String consign) {
        this.consign = consign;
    }

    public String getConsign_people() {
        return consign_people;
    }

    public void setConsign_people(String consign_people) {
        this.consign_people = consign_people;
    }

    public String getConsign_phone() {
        return consign_phone;
    }

    public void setConsign_phone(String consign_phone) {
        this.consign_phone = consign_phone;
    }

    public long getConsign_confim_date() {
        return consign_confim_date;
    }

    public void setConsign_confim_date(long consign_confim_date) {
        this.consign_confim_date = consign_confim_date;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getCarrier_people() {
        return carrier_people;
    }

    public void setCarrier_people(String carrier_people) {
        this.carrier_people = carrier_people;
    }

    public String getCarrier_phone() {
        return carrier_phone;
    }

    public void setCarrier_phone(String carrier_phone) {
        this.carrier_phone = carrier_phone;
    }

    public long getCarrier_confim_date() {
        return carrier_confim_date;
    }

    public void setCarrier_confim_date(long carrier_confim_date) {
        this.carrier_confim_date = carrier_confim_date;
    }

    public String getStowage() {
        return stowage;
    }

    public void setStowage(String stowage) {
        this.stowage = stowage;
    }

    public String getStowage_people() {
        return stowage_people;
    }

    public void setStowage_people(String stowage_people) {
        this.stowage_people = stowage_people;
    }

    public String getStowage_phone() {
        return stowage_phone;
    }

    public void setStowage_phone(String stowage_phone) {
        this.stowage_phone = stowage_phone;
    }

    public long getStowage_confim_date() {
        return stowage_confim_date;
    }

    public void setStowage_confim_date(long stowage_confim_date) {
        this.stowage_confim_date = stowage_confim_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getBase_consign_name() {
        return base_consign_name;
    }

    public void setBase_consign_name(String base_consign_name) {
        this.base_consign_name = base_consign_name;
    }

    public String getBase_carrier_name() {
        return base_carrier_name;
    }

    public void setBase_carrier_name(String base_carrier_name) {
        this.base_carrier_name = base_carrier_name;
    }

    public String getBase_stowage_name() {
        return base_stowage_name;
    }

    public void setBase_stowage_name(String base_stowage_name) {
        this.base_stowage_name = base_stowage_name;
    }

    public String getBase_consign_count() {
        return base_consign_count;
    }

    public void setBase_consign_count(String base_consign_count) {
        this.base_consign_count = base_consign_count;
    }

    public String getBase_carrier_count() {
        return base_carrier_count;
    }

    public void setBase_carrier_count(String base_carrier_count) {
        this.base_carrier_count = base_carrier_count;
    }

    public String getBase_stowage_count() {
        return base_stowage_count;
    }

    public void setBase_stowage_count(String base_stowage_count) {
        this.base_stowage_count = base_stowage_count;
    }

    public String getBase_send_order_name() {
        return base_send_order_name;
    }

    public void setBase_send_order_name(String base_send_order_name) {
        this.base_send_order_name = base_send_order_name;
    }

    public String getBase_plate_number() {
        return base_plate_number;
    }

    public void setBase_plate_number(String base_plate_number) {
        this.base_plate_number = base_plate_number;
    }

    public String getBase_stowage_people() {
        return base_stowage_people;
    }

    public void setBase_stowage_people(String base_stowage_people) {
        this.base_stowage_people = base_stowage_people;
    }

    public String getBase_consign_phone() {
        return base_consign_phone;
    }

    public void setBase_consign_phone(String base_consign_phone) {
        this.base_consign_phone = base_consign_phone;
    }

    public String getBase_carrier_phone() {
        return base_carrier_phone;
    }

    public void setBase_carrier_phone(String base_carrier_phone) {
        this.base_carrier_phone = base_carrier_phone;
    }

    public String getBase_stowage_phone() {
        return base_stowage_phone;
    }

    public void setBase_stowage_phone(String base_stowage_phone) {
        this.base_stowage_phone = base_stowage_phone;
    }

    public int getOrder_user_id() {
        return order_user_id;
    }

    public void setOrder_user_id(int order_user_id) {
        this.order_user_id = order_user_id;
    }

    public int getIs_forward() {
        return is_forward;
    }

    public void setIs_forward(int is_forward) {
        this.is_forward = is_forward;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }
}
