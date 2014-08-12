package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * 商户信息
 * 
 * @version 1.0.0
 * @author wst
 * @date 2013-6-3 下午3:00:23
 */
public class ShopInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1065127737032264398L;
    private int id;// 是 商家名称
    private String vender_name;// 是 商家名称
    private int category;// 是 分类
    private String vender_address = "";// 是 地址
    private int vender_province;// 省
    private int vender_city;// 市
    private int vender_district;// 区
    private double longitude;// 经度
    private double latitude;// 纬度
    private String contact = "";// 联系人
    private String vender_mobile = "";// 联系手机
    private String vender_phone = "";// 联系电话
    private String goods_name = "";// 商品名称
    private String normal_price = "";// 正常价格
    private String member_price = "";// 会员特惠
    private String other = "";// 其他
    private String photo1 = "";// 照片1
    private String Photo2 = "";// 照片2
    private String Photo3 = "";// 照片3
    private String Photo4 = "";// 照片4
    private String Photo5 = "";// 照片5
    private double score1;// 评分1
    private double score2;// 评分2
    private double score3;// 评分3
    private double score;// 评分
    private int read_time;// 阅读次数
    private int status;// 状态
    private int parking_spaces_num; //车位数
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getVender_name() {
		return vender_name;
	}
	
	public void setVender_name(String vender_name) {
		this.vender_name = vender_name;
	}
	
	public int getCategory() {
		return category;
	}
	
	public void setCategory(int category) {
		this.category = category;
	}
	
	public String getVender_address() {
		return vender_address;
	}
	
	public void setVender_address(String vender_address) {
		this.vender_address = vender_address;
	}
	
	public int getVender_province() {
		return vender_province;
	}
	
	public void setVender_province(int vender_province) {
		this.vender_province = vender_province;
	}
	
	public int getVender_city() {
		return vender_city;
	}
	
	public void setVender_city(int vender_city) {
		this.vender_city = vender_city;
	}
	
	public int getVender_district() {
		return vender_district;
	}
	
	public void setVender_district(int vender_district) {
		this.vender_district = vender_district;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public String getContact() {
		return contact;
	}
	
	public void setContact(String contact) {
		this.contact = contact;
	}
	
	public String getVender_mobile() {
		return vender_mobile;
	}
	
	public void setVender_mobile(String vender_mobile) {
		this.vender_mobile = vender_mobile;
	}
	
	public String getVender_phone() {
		return vender_phone;
	}
	
	public void setVender_phone(String vender_phone) {
		this.vender_phone = vender_phone;
	}
	
	public String getGoods_name() {
		return goods_name;
	}
	
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	
	public String getNormal_price() {
		return normal_price;
	}
	
	public void setNormal_price(String normal_price) {
		this.normal_price = normal_price;
	}
	
	public String getMember_price() {
		return member_price;
	}
	
	public void setMember_price(String member_price) {
		this.member_price = member_price;
	}
	
	public String getOther() {
		return other;
	}
	
	public void setOther(String other) {
		this.other = other;
	}
	
	public String getPhoto1() {
		return photo1;
	}
	
	public void setPhoto1(String photo1) {
		this.photo1 = photo1;
	}
	
	public String getPhoto2() {
		return Photo2;
	}
	
	public void setPhoto2(String photo2) {
		Photo2 = photo2;
	}
	
	public String getPhoto3() {
		return Photo3;
	}
	
	public void setPhoto3(String photo3) {
		Photo3 = photo3;
	}
	
	public String getPhoto4() {
		return Photo4;
	}
	
	public void setPhoto4(String photo4) {
		Photo4 = photo4;
	}
	
	public String getPhoto5() {
		return Photo5;
	}
	
	public void setPhoto5(String photo5) {
		Photo5 = photo5;
	}
	
	public double getScore1() {
		return score1;
	}
	
	public void setScore1(double score1) {
		this.score1 = score1;
	}
	
	public double getScore2() {
		return score2;
	}
	
	public void setScore2(double score2) {
		this.score2 = score2;
	}
	
	public double getScore3() {
		return score3;
	}
	
	public void setScore3(double score3) {
		this.score3 = score3;
	}
	
	public double getScore() {
		return score;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	public int getRead_time() {
		return read_time;
	}
	
	public void setRead_time(int read_time) {
		this.read_time = read_time;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}

	private String campus_activities;//园区活动

    public int getParking_spaces_num() {
        return parking_spaces_num;
    }

    public void setParking_spaces_num(int parking_spaces_num) {
        this.parking_spaces_num = parking_spaces_num;
    }

    public String getCampus_activities() {
        return campus_activities;
    }

    public void setCampus_activities(String campus_activities) {
        this.campus_activities = campus_activities;
    }
}
