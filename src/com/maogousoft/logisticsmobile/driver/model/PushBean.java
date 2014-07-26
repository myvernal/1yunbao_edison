package com.maogousoft.logisticsmobile.driver.model;

public class PushBean {

	// {"cargo_desc":"糖葫芦","id":100000297,"cargo_unit_name":"吨","validate_time":"2013-06-27 23:05:21.0","cargo_unit":2,"msg_type":5,"cargo_number":3}

	String cargo_desc;
	int id;
	String cargo_unit_name;
	String validate_time;
	int cargo_unit;
	int msg_type;
	int cargo_number;
	int order_id;

	public String getCargo_desc() {
		return cargo_desc;
	}

	public void setCargo_desc(String cargo_desc) {
		this.cargo_desc = cargo_desc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCargo_unit_name() {
		return cargo_unit_name;
	}

	public void setCargo_unit_name(String cargo_unit_name) {
		this.cargo_unit_name = cargo_unit_name;
	}

	public String getValidate_time() {
		return validate_time;
	}

	public void setValidate_time(String validate_time) {
		this.validate_time = validate_time;
	}

	public int getCargo_unit() {
		return cargo_unit;
	}

	public void setCargo_unit(int cargo_unit) {
		this.cargo_unit = cargo_unit;
	}

	public int getMsg_type() {
		return msg_type;
	}

	public void setMsg_type(int msg_type) {
		this.msg_type = msg_type;
	}

	public int getCargo_number() {
		return cargo_number;
	}

	public void setCargo_number(int cargo_number) {
		this.cargo_number = cargo_number;
	}

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

}
