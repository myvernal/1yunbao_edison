package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

/**
 * 货主个人信息
 * 
 * @version 1.0.0
 * @author wst
 * @date 2013-6-6 下午5:02:21
 */
public class ShipperInfo implements Serializable {

	// 货主公司名字、地址、联系手机、座机电话

	/**
	 * 
	 */
	private static final long serialVersionUID = -6581632252578946774L;
    private int id;
	private String phone = "";	// 手机号码
	private String name = "";	// 姓名
	private String company_name = "";	// 公司名称
	private String address = "";// 地址
	private String telcom = "";// 座机
    private String insurance_count;
    private String fleet_count;
    private String verify_count;
    private String company_logo = "";
    private String company_photo1 = "";
    private String company_photo2 = "";
    private String company_photo3 = "";
    private String company_recommendation = "";
    private String yunbao_pay;
    private float score = 0;
    private float score1 = 0;
    private float score2 = 0;
    private float score3 = 0;
    private float gold = 0;
    private Double tpy_1;//基本险 费率
    private Double tpy_2;//基本险(易碎) 费率
    private Double tpy_3;//基本险(鲜活) 费率
    private Double tpy_4;//综合险     费率
    private Double tpy_5;//综合险(易碎) 费率
    private Double tpy_6;//综合险附加被盗险费率
    private Double pa_1;//平安费率

    private String bank;//开户银行名称
    private String bank_account;//银行账号
    private String account_name;//账号姓名
    private String yunbao_gold = "0";//运宝币
    private String freeze_gold;//冻结的现金金额
    private String freeze_yunbao_gold;//冻结的运宝币

    public String getFreeze_gold() {
        return freeze_gold;
    }

    public void setFreeze_gold(String freeze_gold) {
        this.freeze_gold = freeze_gold;
    }

    public String getFreeze_yunbao_gold() {
        return freeze_yunbao_gold;
    }

    public void setFreeze_yunbao_gold(String freeze_yunbao_gold) {
        this.freeze_yunbao_gold = freeze_yunbao_gold;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getGold() {
        return gold;
    }

    public void setGold(float gold) {
        this.gold = gold;
    }

    public String getYunbao_gold() {
        return yunbao_gold;
    }

    public void setYunbao_gold(String yunbao_gold) {
        this.yunbao_gold = yunbao_gold;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
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

    public String getYunbao_pay() {
        return yunbao_pay;
    }

    public void setYunbao_pay(String yunbao_pay) {
        this.yunbao_pay = yunbao_pay;
    }

    public String getCompany_photo1() {
        return company_photo1;
    }

    public void setCompany_photo1(String company_photo1) {
        this.company_photo1 = company_photo1;
    }

    public String getCompany_photo2() {
        return company_photo2;
    }

    public void setCompany_photo2(String company_photo2) {
        this.company_photo2 = company_photo2;
    }

    public String getCompany_photo3() {
        return company_photo3;
    }

    public void setCompany_photo3(String company_photo3) {
        this.company_photo3 = company_photo3;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }

    public String getCompany_recommendation() {
        return company_recommendation;
    }

    public void setCompany_recommendation(String company_recommendation) {
        this.company_recommendation = company_recommendation;
    }

    public String getInsurance_count() {
        return insurance_count;
    }

    public void setInsurance_count(String insurance_count) {
        this.insurance_count = insurance_count;
    }

    public String getFleet_count() {
        return fleet_count;
    }

    public void setFleet_count(String fleet_count) {
        this.fleet_count = fleet_count;
    }

    public String getVerify_count() {
        return verify_count;
    }

    public void setVerify_count(String verify_count) {
        this.verify_count = verify_count;
    }

    public Double getTpy_1() {
        return tpy_1;
    }

    public void setTpy_1(Double tpy_1) {
        this.tpy_1 = tpy_1;
    }

    public Double getTpy_2() {
        return tpy_2;
    }

    public void setTpy_2(Double tpy_2) {
        this.tpy_2 = tpy_2;
    }

    public Double getTpy_3() {
        return tpy_3;
    }

    public void setTpy_3(Double tpy_3) {
        this.tpy_3 = tpy_3;
    }

    public Double getTpy_4() {
        return tpy_4;
    }

    public void setTpy_4(Double tpy_4) {
        this.tpy_4 = tpy_4;
    }

    public Double getTpy_5() {
        return tpy_5;
    }

    public void setTpy_5(Double tpy_5) {
        this.tpy_5 = tpy_5;
    }

    public Double getTpy_6() {
        return tpy_6;
    }

    public void setTpy_6(Double tpy_6) {
        this.tpy_6 = tpy_6;
    }

    public Double getPa_1() {
        return pa_1;
    }

    public void setPa_1(Double pa_1) {
        this.pa_1 = pa_1;
    }

    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelcom() {
		return telcom;
	}

	public void setTelcom(String telcom) {
		this.telcom = telcom;
	}

}
