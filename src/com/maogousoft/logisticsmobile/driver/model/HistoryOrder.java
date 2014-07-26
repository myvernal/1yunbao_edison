package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * 历史订单
 * 
 * @version 1.0.0
 * @author wst
 * @date 2013-6-7 下午4:03:26
 */
public class HistoryOrder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4743301982928209174L;
	private int id;// 是 订单号
	private int start_province;// 是 出发地省份
	private int start_city;// 是 出发地城市
	private int start_district;// 是 出发地区县
	private int end_province;// 是 目的地省份
	private int end_city;// 是 目的地城市
	private int end_district;// 是 目的地区县
	private String start_city_str;// 是 出发地城市字符串
	private String start_district_str;// 是 出发地区县字符串
	private String end_province_str;// 是 目的地省份字符串
	private String end_city_str;// 是 目的地城市字符串
	private String end_district_str;// 是 目的地区县字符串
	private String cargo_desc;// 是 货物名称
	private int cargo_type;// 是 货物类别
	private String cargo_type_str;// 是 货物类别字符串
	private String car_length;// 是 要求车长
	private int cargo_number;// 是 货物数量
	private int cargo_unit;// 是 货物数量单位 1-车 2-吨 3-方
	private String cargo_unit_name;// 是 货物数量单位字符串 车、吨、方
	private double unit_price;// 是 货物单价
	private String price;// 是 货物价格(总价)
	private int ship_type;// 是 运输方式
	private String ship_type_str;// 是 运输方式字符串
	private int car_type;// 是 货车类型
	private String car_type_str;// 是 货车类型字符串
	private String cargo_photo1;// 否 货物照片1 图片地址
	private String cargo_photo2;// 否 货物照片2
	private String cargo_photo3;// 否 货物照片3
	private long loading_time;// 是 装车时间 使用long型日期的毫秒数表示
	private String cargo_remark;// 是 补充说明
	private long validate_time;// 是 有效截止日期 使用long型日期的毫秒数表示
	private int user_bond;// 是 押金 单位：元，整数
	private String user_name;// 是 货主姓名
	private String user_phone;// 是 货主电话
	private int User_id;// 货主id
	private int reply_flag;// 是否已评价 0-未评价，1-已评价
	private int score1;// Int 评分1 未评价时，该字段值为-1
	private int Score2;// Int 评分2 未评价时，该字段值为-1
	private int Score3;// Int 评分3 未评价时，该字段值为-1
	private String reply_content;// 评价内容 未评价时，该字段值为空字符串
	private String reply_time;// 评价时间 未评价时，该字段值为空字符串
	private String company_name;// 公司名称

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getStart_city_str() {
		return start_city_str;
	}

	public void setStart_city_str(String start_city_str) {
		this.start_city_str = start_city_str;
	}

	public String getStart_district_str() {
		return start_district_str;
	}

	public void setStart_district_str(String start_district_str) {
		this.start_district_str = start_district_str;
	}

	public String getEnd_province_str() {
		return end_province_str;
	}

	public void setEnd_province_str(String end_province_str) {
		this.end_province_str = end_province_str;
	}

	public String getEnd_city_str() {
		return end_city_str;
	}

	public void setEnd_city_str(String end_city_str) {
		this.end_city_str = end_city_str;
	}

	public String getEnd_district_str() {
		return end_district_str;
	}

	public void setEnd_district_str(String end_district_str) {
		this.end_district_str = end_district_str;
	}

	public String getCargo_desc() {
		return cargo_desc;
	}

	public void setCargo_desc(String cargo_desc) {
		this.cargo_desc = cargo_desc;
	}

	public int getCargo_type() {
		return cargo_type;
	}

	public void setCargo_type(int cargo_type) {
		this.cargo_type = cargo_type;
	}

	public String getCargo_type_str() {
		return cargo_type_str;
	}

	public void setCargo_type_str(String cargo_type_str) {
		this.cargo_type_str = cargo_type_str;
	}

	public String getCar_length() {
		return car_length;
	}

	public void setCar_length(String car_length) {
		this.car_length = car_length;
	}

	public int getCargo_number() {
		return cargo_number;
	}

	public void setCargo_number(int cargo_number) {
		this.cargo_number = cargo_number;
	}

	public int getCargo_unit() {
		return cargo_unit;
	}

	public void setCargo_unit(int cargo_unit) {
		this.cargo_unit = cargo_unit;
	}

	public String getCargo_unit_name() {
		return cargo_unit_name;
	}

	public void setCargo_unit_name(String cargo_unit_name) {
		this.cargo_unit_name = cargo_unit_name;
	}

	public double getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(double unit_price) {
		this.unit_price = unit_price;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getShip_type() {
		return ship_type;
	}

	public void setShip_type(int ship_type) {
		this.ship_type = ship_type;
	}

	public String getShip_type_str() {
		return ship_type_str;
	}

	public void setShip_type_str(String ship_type_str) {
		this.ship_type_str = ship_type_str;
	}

	public int getCar_type() {
		return car_type;
	}

	public void setCar_type(int car_type) {
		this.car_type = car_type;
	}

	public String getCar_type_str() {
		return car_type_str;
	}

	public void setCar_type_str(String car_type_str) {
		this.car_type_str = car_type_str;
	}

	public String getCargo_photo1() {
		return cargo_photo1;
	}

	public void setCargo_photo1(String cargo_photo1) {
		this.cargo_photo1 = cargo_photo1;
	}

	public String getCargo_photo2() {
		return cargo_photo2;
	}

	public void setCargo_photo2(String cargo_photo2) {
		this.cargo_photo2 = cargo_photo2;
	}

	public String getCargo_photo3() {
		return cargo_photo3;
	}

	public void setCargo_photo3(String cargo_photo3) {
		this.cargo_photo3 = cargo_photo3;
	}

	public long getLoading_time() {
		return loading_time;
	}

	public void setLoading_time(long loading_time) {
		this.loading_time = loading_time;
	}

	public String getCargo_remark() {
		return cargo_remark;
	}

	public void setCargo_remark(String cargo_remark) {
		this.cargo_remark = cargo_remark;
	}

	public long getValidate_time() {
		return validate_time;
	}

	public void setValidate_time(long validate_time) {
		this.validate_time = validate_time;
	}

	public int getUser_bond() {
		return user_bond;
	}

	public void setUser_bond(int user_bond) {
		this.user_bond = user_bond;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_phone() {
		return user_phone;
	}

	public void setUser_phone(String user_phone) {
		this.user_phone = user_phone;
	}

	public int getUser_id() {
		return User_id;
	}

	public void setUser_id(int user_id) {
		User_id = user_id;
	}

	public int getReply_flag() {
		return reply_flag;
	}

	public void setReply_flag(int reply_flag) {
		this.reply_flag = reply_flag;
	}

	public int getScore1() {
		return score1;
	}

	public void setScore1(int score1) {
		this.score1 = score1;
	}

	public int getScore2() {
		return Score2;
	}

	public void setScore2(int score2) {
		Score2 = score2;
	}

	public int getScore3() {
		return Score3;
	}

	public void setScore3(int score3) {
		Score3 = score3;
	}

	public String getReply_content() {
		return reply_content;
	}

	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}

	public String getReply_time() {
		return reply_time;
	}

	public void setReply_time(String reply_time) {
		this.reply_time = reply_time;
	}

}
