package com.maogousoft.logisticsmobile.driver.pay.wx.utils;

import java.io.Serializable;

/**
 * Created by aliang on 2015/5/7.
 */
public class PrepayOrderResult implements Serializable {
    private String return_code;//此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
    private String appid;//调用接口提交的公众账号ID
    private String mch_id;//调用接口提交的商户号
    private String device_info;//调用接口提交的终端设备号
    private String nonce_str;//微信返回的随机字符串
    private String sign;//微信返回的签名，详见签名算法
    private String result_code;//SUCCESS FAIL
    private String err_code;//错误代码
    private String err_code_des;//错误代码描述
    private String trade_type;//调用接口提交的交易类型，取值如下：JSAPI，NATIVE，APP，详细说明见参数规定
    private String prepay_id;//微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
    private String code_url;//trade_type为NATIVE是有返回，可将该参数值生成二维码展示出来进行扫码支付

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public void setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getCode_url() {
        return code_url;
    }

    public void setCode_url(String code_url) {
        this.code_url = code_url;
    }
}
