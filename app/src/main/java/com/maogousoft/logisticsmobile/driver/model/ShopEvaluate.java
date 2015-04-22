package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * 商户评价信息
 * 
 * @version 1.0.0
 * @author wst
 * @date 2013-6-4 下午9:50:55
 */
public class ShopEvaluate implements Serializable {

	// Name String 司机姓名
	// id int not null auto_increment comment '编号',
	// vender_id int comment '商户编号',
	// driver_id int comment '司机编号',
	// score1 int comment '评分1',
	// score2 int comment '评分2',
	// score3 int comment '评分3',
	// reply_content varchar(500) comment '评价内容',
	// is_true int comment '是否如实',
	// reply_time datetime comment '评价时间',

	/**
	 * 
	 */
	private static final long serialVersionUID = 3918613337035436961L;
	private String Name = "";// 司机姓名
	private int id;// 编号
	private int vender_id;// 商户编号
	private int driver_id;// 司机编号
	private int score1;// 评分1
	private int score2;// 评分2
	private int score3;// 评分3
	private String reply_content = "";// 评价内容
	private int is_true;// 是否如实
	private long reply_time;// 评价时间

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getVender_id() {
		return vender_id;
	}

	public void setVender_id(int vender_id) {
		this.vender_id = vender_id;
	}

	public int getDriver_id() {
		return driver_id;
	}

	public void setDriver_id(int driver_id) {
		this.driver_id = driver_id;
	}

	public int getScore1() {
		return score1;
	}

	public void setScore1(int score1) {
		this.score1 = score1;
	}

	public int getScore2() {
		return score2;
	}

	public void setScore2(int score2) {
		this.score2 = score2;
	}

	public int getScore3() {
		return score3;
	}

	public void setScore3(int score3) {
		this.score3 = score3;
	}

	public String getReply_content() {
		return reply_content;
	}

	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}

	public int getIs_true() {
		return is_true;
	}

	public void setIs_true(int is_true) {
		this.is_true = is_true;
	}

	public long getReply_time() {
		return reply_time;
	}

	public void setReply_time(long reply_time) {
		this.reply_time = reply_time;
	}

}
