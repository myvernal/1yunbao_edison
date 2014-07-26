package com.maogousoft.logisticsmobile.driver.model;

/**
 * 字典表
 * 
 * @author lenovo
 */
public class DictInfo {

	private int id = -1;

	// 名称
	private String name = "";

	// 字典类型，car_type,cargo_type,disburden,ship_type
	private String dict_type = "";

	// 字典描述
	private String dict_desc = "";

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDict_type() {
		return dict_type;
	}

	public void setDict_type(String dict_type) {
		this.dict_type = dict_type;
	}

	public String getDict_desc() {
		return dict_desc;
	}

	public void setDict_desc(String dict_desc) {
		this.dict_desc = dict_desc;
	}

}
