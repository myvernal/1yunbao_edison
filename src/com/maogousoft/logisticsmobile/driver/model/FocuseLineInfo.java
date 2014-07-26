package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;
/**
 * 关注路线
 * @author ybxiang
 *
 */
public class FocuseLineInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8792781579888602352L;
	private int start_province;
	private int start_city;
	private int start_district;
	private int end_province;
	private int end_city;
	private int end_district;
	
	
	
	public FocuseLineInfo() {
		super();
	}
	
	
	
	public FocuseLineInfo(int start_province, int end_province) {
		super();
		this.start_province = start_province;
		this.end_province = end_province;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "FocuseLineInfo [start_province=" + start_province
				+ ", start_city=" + start_city + ", start_district="
				+ start_district + ", end_province=" + end_province
				+ ", end_city=" + end_city + ", end_district=" + end_district
				+ "]";
	}

}
