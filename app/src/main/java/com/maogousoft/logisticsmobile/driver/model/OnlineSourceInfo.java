package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * 在途货源
 * 
 * @author lenovo
 */
public class OnlineSourceInfo implements Serializable {

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getUser_telcom() {
		return user_telcom;
	}

	public void setUser_telcom(String user_telcom) {
		this.user_telcom = user_telcom;
	}

	private int id = 0;

	private int start_province = -1;

	private int start_city = -1;

	private int start_district = -1;

	private int end_province = -1;

	private int end_city = -1;

	private int end_district = -1;

	private String start_province_str = "";

	private String start_city_str = "";

	private String start_district_str = "";

	private String end_province_str = "";

	private String end_city_str = "";

	private String end_district_str = "";

	private String cargo_desc = "";

	private int cargo_type = 0;

	private String cargo_type_str = "";

	private String car_length = "";

	private int cargo_number = 0;

	private int cargo_unit = 0;

	private String cargo_unit_name = "";

	private double unit_price = 0;

	private String price = "";

	private int ship_type = 0;

	private String ship_type_str = "";

	private String cargo_photo1 = "";

	private String cargo_photo2 = "";

	private String cargo_photo3 = "";

	private long loading_time = 0l;

	private String cargo_remark = "";

	private long validate_time = 0l;

	private int user_bond = 0;

	// 1-到达装车位置，2-启程，3-在途，4-到达终点
	private int order_status = 0;

	private String user_name = "";

	private String user_phone = "";

	private String driver_name = "";

	private String driver_phone = "";

	// 司机编号
	private int user_id;

	// 执行状态，-1：未开始。1-到达装货，2-启程，3-在途，4-到达目的地，5-卸货,6-回单密码完成
	private int execute_status = -1;

	// 货主公司名字 company
	private String company_name = "";
	// 货主座机telcom
	private String user_telcom = "";

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

	public String getStart_province_str() {
		return start_province_str;
	}

	public void setStart_province_str(String start_province_str) {
		this.start_province_str = start_province_str;
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

	public int getOrder_status() {
		return order_status;
	}

	public void setOrder_status(int order_status) {
		this.order_status = order_status;
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

	public String getDriver_name() {
		return driver_name;
	}

	public void setDriver_name(String driver_name) {
		this.driver_name = driver_name;
	}

	public String getDriver_phone() {
		return driver_phone;
	}

	public void setDriver_phone(String driver_phone) {
		this.driver_phone = driver_phone;
	}

	public int getExecute_status() {
		return execute_status;
	}

	public void setExecute_status(int execute_status) {
		this.execute_status = execute_status;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

}
