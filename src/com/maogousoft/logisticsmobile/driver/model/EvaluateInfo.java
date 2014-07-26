package com.maogousoft.logisticsmobile.driver.model;

/****
 * 评价信息
 * 
 * @author admin
 * 
 */
public class EvaluateInfo {

	private int order_id;
	private float score1;
	private float score2;
	private float score3;
	private int start_province;
	private int start_city;
	private int start_district;
	private int end_province;
	private int end_city;
	private int end_district;
	private String cargo_desc = "";
	private String reply_content = "";

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public float getScore1() {
		return score1;
	}

	public void setScore1(float score1) {
		this.score1 = score1;
	}

	public float getScore2() {
		return score2;
	}

	public void setScore2(float score2) {
		this.score2 = score2;
	}

	public float getScore3() {
		return score3;
	}

	public void setScore3(float score3) {
		this.score3 = score3;
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

	public String getCargo_desc() {
		return cargo_desc;
	}

	public void setCargo_desc(String cargo_desc) {
		this.cargo_desc = cargo_desc;
	}

	public String getReply_content() {
		return reply_content;
	}

	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}

}
