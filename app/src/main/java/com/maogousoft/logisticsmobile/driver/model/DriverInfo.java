package com.maogousoft.logisticsmobile.driver.model;

import java.io.Serializable;

public class DriverInfo implements Serializable {

    private int id = -1;

	// 手机号码
	private String phone = "";

	// 姓名
	private String name = "";

	// 车牌号码
	private String plate_number = "";

	// 推荐人帐号
	private String recommender = "";

	// 账户余额
	private double gold = 0;

	// 在线时间
	private int online_time = 0;

	// 在线时间排名
	private int online_time_rank = 0;

	// 推荐人数总数
	private int recommender_count = 0;

	// 推荐人数排名
	private int recommender_count_rank = 0;

	// 成交单数
	private int order_count = 0;

	// 成交单数排名
	private int order_count_rank = 0;

	private String car_photo1 = "";

	private String car_photo2 = "";

	private String car_photo3 = "";

    private String myself_recommendation = "";

	private String license = "";

	private String license_name = "";

	private String license_photo = "";

	private String registration = "";

	private String registration_name = "";

	private String registration_photo = "";

	private String owner_phone = "";

	private String id_card = "";

	private String id_card_name = "";

	private String id_card_photo = "";

	private float score = 0;

	private float scroe1 = 0;

	private float scroe2 = 0;

	private float scroe3 = 0;
    private long regist_time;//注册时间
	private int car_type;// 车型
	private String car_type_str = "";// 车型描述
	private double car_length;// 车长
	private int car_weight;// 载重
	private int start_province;// 是 线路起始点省份
	private int start_city;// 是 线路起始点城市
	private int end_province;// 是 线路终止点省份
	private int end_city;// 是 线路终止点城市
	private int start_province2;// 是 线路起始点省份2
	private int start_city2;// 是 线路起始点城市2
	private int end_province2;// 是 线路终止点省份2
	private int end_city2;// 是 线路终止点城市2
	private int start_province3;// 是 线路起始点省份3
	private int start_city3;// 是 线路起始点城市3
	private int end_province3;// 是 线路终止点省份3
	private int end_city3;// 是 线路终止点城市3
    private String yunbao_gold = "0";//运宝币
	private String car_phone = "";// 随车手机
    private String yunbao_pay;
    private String linkman;//联系人
    private String frame_number;//车架号
    private String bank;//开户银行名称
    private String account_name;//账户名称
    private String bank_account;//银行账号
    private String business_address;//办公地址

    public String getYunbao_gold() {
        return yunbao_gold;
    }

    public void setYunbao_gold(String yunbao_gold) {
        this.yunbao_gold = yunbao_gold;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getRegist_time() {
        return regist_time;
    }

    public void setRegist_time(long regist_time) {
        this.regist_time = regist_time;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getFrame_number() {
        return frame_number;
    }

    public void setFrame_number(String frame_number) {
        this.frame_number = frame_number;
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

    public String getBusiness_address() {
        return business_address;
    }

    public void setBusiness_address(String business_address) {
        this.business_address = business_address;
    }

    public String getYunbao_pay() {
        return yunbao_pay;
    }

    public void setYunbao_pay(String yunbao_pay) {
        this.yunbao_pay = yunbao_pay;
    }

    public String getCar_photo3() {
        return car_photo3;
    }

    public void setCar_photo3(String car_photo3) {
        this.car_photo3 = car_photo3;
    }

    public String getMyself_recommendation() {
        return myself_recommendation;
    }

    public void setMyself_recommendation(String myself_recommendation) {
        this.myself_recommendation = myself_recommendation;
    }

    public String getCar_phone() {
		return car_phone;
	}

	public void setCar_phone(String car_phone) {
		this.car_phone = car_phone;
	}

	public String getCar_type_str() {
		return car_type_str;
	}

	public void setCar_type_str(String car_type_str) {
		this.car_type_str = car_type_str;
	}

	public double getCar_length() {
		return car_length;
	}

	public void setCar_length(double car_length) {
		this.car_length = car_length;
	}

	public int getCar_weight() {
		return car_weight;
	}

	public void setCar_weight(int car_weight) {
		this.car_weight = car_weight;
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

	public int getStart_province2() {
		return start_province2;
	}

	public void setStart_province2(int start_province2) {
		this.start_province2 = start_province2;
	}

	public int getStart_city2() {
		return start_city2;
	}

	public void setStart_city2(int start_city2) {
		this.start_city2 = start_city2;
	}

	public int getEnd_province2() {
		return end_province2;
	}

	public void setEnd_province2(int end_province2) {
		this.end_province2 = end_province2;
	}

	public int getEnd_city2() {
		return end_city2;
	}

	public void setEnd_city2(int end_city2) {
		this.end_city2 = end_city2;
	}

	public int getStart_province3() {
		return start_province3;
	}

	public void setStart_province3(int start_province3) {
		this.start_province3 = start_province3;
	}

	public int getStart_city3() {
		return start_city3;
	}

	public void setStart_city3(int start_city3) {
		this.start_city3 = start_city3;
	}

	public int getEnd_province3() {
		return end_province3;
	}

	public void setEnd_province3(int end_province3) {
		this.end_province3 = end_province3;
	}

	public int getEnd_city3() {
		return end_city3;
	}

	public void setEnd_city3(int end_city3) {
		this.end_city3 = end_city3;
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

	public String getPlate_number() {
		return plate_number;
	}

	public void setPlate_number(String plate_number) {
		this.plate_number = plate_number;
	}

	public String getRecommender() {
		return recommender;
	}

	public void setRecommender(String recommender) {
		this.recommender = recommender;
	}

	public double getGold() {
		return gold;
	}

	public void setGold(double gold) {
		this.gold = gold;
	}

	public int getOnline_time() {
		return online_time;
	}

	public void setOnline_time(int online_time) {
		this.online_time = online_time;
	}

	public int getOnline_time_rank() {
		return online_time_rank;
	}

	public void setOnline_time_rank(int online_time_rank) {
		this.online_time_rank = online_time_rank;
	}

	public int getRecommender_count() {
		return recommender_count;
	}

	public void setRecommender_count(int recommender_count) {
		this.recommender_count = recommender_count;
	}

	public int getRecommender_count_rank() {
		return recommender_count_rank;
	}

	public void setRecommender_count_rank(int recommender_count_rank) {
		this.recommender_count_rank = recommender_count_rank;
	}

	public int getOrder_count() {
		return order_count;
	}

	public void setOrder_count(int order_count) {
		this.order_count = order_count;
	}

	public int getOrder_count_rank() {
		return order_count_rank;
	}

	public void setOrder_count_rank(int order_count_rank) {
		this.order_count_rank = order_count_rank;
	}

	public String getCar_photo1() {
		return car_photo1;
	}

	public void setCar_photo1(String car_photo1) {
		this.car_photo1 = car_photo1;
	}

	public String getCar_photo2() {
		return car_photo2;
	}

	public void setCar_photo2(String car_photo2) {
		this.car_photo2 = car_photo2;
	}

	public String getId_card_photo() {
		return id_card_photo;
	}

	public void setId_card_photo(String id_card_photo) {
		this.id_card_photo = id_card_photo;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getLicense_name() {
		return license_name;
	}

	public void setLicense_name(String license_name) {
		this.license_name = license_name;
	}

	public String getLicense_photo() {
		return license_photo;
	}

	public void setLicense_photo(String license_photo) {
		this.license_photo = license_photo;
	}

	public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}

	public String getRegistration_name() {
		return registration_name;
	}

	public void setRegistration_name(String registration_name) {
		this.registration_name = registration_name;
	}

	public String getRegistration_photo() {
		return registration_photo;
	}

	public void setRegistration_photo(String registration_photo) {
		this.registration_photo = registration_photo;
	}

	public String getOwner_phone() {
		return owner_phone;
	}

	public void setOwner_phone(String owner_phone) {
		this.owner_phone = owner_phone;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public float getScroe1() {
		return scroe1;
	}

	public void setScroe1(float scroe1) {
		this.scroe1 = scroe1;
	}

	public float getScroe2() {
		return scroe2;
	}

	public void setScroe2(float scroe2) {
		this.scroe2 = scroe2;
	}

	public float getScroe3() {
		return scroe3;
	}

	public void setScroe3(float scroe3) {
		this.scroe3 = scroe3;
	}

	public String getId_card() {
		return id_card;
	}

	public void setId_card(String id_card) {
		this.id_card = id_card;
	}

	public String getId_card_name() {
		return id_card_name;
	}

	public void setId_card_name(String id_card_name) {
		this.id_card_name = id_card_name;
	}

	public int getCar_type() {
		return car_type;
	}

	public void setCar_type(int car_type) {
		this.car_type = car_type;
	}

}
