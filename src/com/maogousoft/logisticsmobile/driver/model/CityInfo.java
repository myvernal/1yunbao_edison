package com.maogousoft.logisticsmobile.driver.model;

/**
 * 城市信息类
 * 
 * @author lenovo
 * 
 */
public class CityInfo {

	private Integer id;
	private Integer parentId;
	private String name;
	private String shortName;
	private Integer deep;

	public CityInfo() {
	}

	public CityInfo(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Integer getDeep() {
		return deep;
	}

	public void setDeep(Integer deep) {
		this.deep = deep;
	}

}
