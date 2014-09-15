package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * 账户记录
 * 
 * @version 1.0.0
 * @author wst
 * @date 2013-6-5 下午4:38:39
 */
public class AccountRecordInfo implements Serializable {

	// id int 是 流水号
	// account Int 是 用户编号
	// business_amount Double 是 金额
	// business_type Int 是 类型
	// create_time long 是 时间

	
	/**
	 * 交易类型“充值
	 */
	public static final int BUSINESS_TYPE_RECHARGE = 0 ;


	/**
	 * 违约扣除
	 */
	public static final int BUSINESS_TYPE_VIOLATE = 2 ;

	/**
	 * 成功交易扣除
	 */
	public static final int BUSINESS_TYPE_DEAL = 3 ;

	/**
	 * 验证身份扣除
	 */
	public static final int BUSINESS_TYPE_VERIFY = 4 ;

	/**
	 * 冻结
	 */
	public static final int BUSINESS_TYPE_FREEZE = 5 ;

	/**
	 * 保证金扣除
	 */
	public static final int BUSINESS_TYPE_DEPOSIT = 6 ;

	/**
	 * 短信扣除
	 */
	public static final int BUSINESS_TYPE_SMS = 7 ;

	/**
	 * 保证金返还
	 */
	public static final int BUSINESS_TYPE_DEPOSIT_RETURN= 8 ;

	/**
	 * 对方违约返还
	 */
	public static final int BUSINESS_TYPE_VIOLATE_RETURN= 9 ;

	/**
	 * 手机定位扣除
	 */
	public static final int BUSINESS_TYPE_LOCATION= 10 ;

	/**
	 * 成功交易奖励
	 */
	public static final int BUSINESS_TYPE_AWARD = 11 ;

	/**
	 * 对方违约赔付
	 */
	public static final int BUSINESS_TYPE_VIOLATE_PAID = 12 ;

    /**
     *购买保险扣除
     */
    public static final int BUSINESS_TYPE_INSURE_PAID = 13 ;

	/**
	 * 
	 */
	private static final long serialVersionUID = -2893166774843992078L;

	private int id;// 流水号
	private int account;// 用户编号
	private double business_amount;// 金额
	private int business_type;// 类型
	private long create_time;// 时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

	public double getBusiness_amount() {
		return business_amount;
	}

	public void setBusiness_amount(double business_amount) {
		this.business_amount = business_amount;
	}

	public int getBusiness_type() {
		return business_type;
	}

	public void setBusiness_type(int business_type) {
		this.business_type = business_type;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

}
