package com.maogousoft.logisticsmobile.driver.model;

/**
 * 车源信息
 * 
 * @author ybxiang 管理车源界面：线路，载重，车型 车源详细界面：线路，车牌号，车长，车型，载重，报价，位置，联系人，手机
 */
public class CarInfo {
	private String way; // 路线
	private int car_weight;// 载重
	private int car_type;// 车型
	private String car_weight_str;// 载重string
	private String car_type_str;// 车型string
	private String plate_number = "";// 车牌号
	private double car_length;// 车长
	private String price;// 报价
	private String location;// 位置
	private String name = "";// 联系人
	private String phone = "";// 手机

	private String location_time; // 定位时间==我的车队
	private int checked;// 有无证件，0 没有，1有

	public CarInfo() {
		super();
	}

	public CarInfo(String way, int car_weight, int car_type) {
		super();
		this.way = way;
		this.car_weight = car_weight;
		this.car_type = car_type;
	}

	/**
	 * 车源管理
	 * 
	 * @param way
	 * @param car_weight_str
	 * @param car_type_str
	 */
	public CarInfo(String way, String car_weight_str, String car_type_str) {
		super();
		this.way = way;
		this.car_weight_str = car_weight_str;
		this.car_type_str = car_type_str;
	}

	/**
	 * 我的车队
	 * 
	 * @param way
	 *            路线
	 * @param car_type_str
	 *            车型
	 * @param car_length
	 *            车长
	 * @param location
	 *            当前位置
	 * @param name
	 *            司机姓名
	 * @param location_time
	 *            定位时间
	 * @param checked
	 *            有无证件
	 */
	public CarInfo(String name, String plate_number, String car_type_str,
			double car_length, String way, String location,
			String location_time, int checked) {
		super();
		this.way = way;
		this.car_type_str = car_type_str;
		this.car_length = car_length;
		this.location = location;
		this.name = name;
		this.location_time = location_time;
		this.checked = checked;
		this.plate_number = plate_number;
	}

	public CarInfo(String way, int car_weight, int car_type,
			String car_weight_str, String car_type_str, String plate_number,
			double car_length, String price, String location, String name,
			String phone) {
		super();
		this.way = way;
		this.car_weight = car_weight;
		this.car_type = car_type;
		this.car_weight_str = car_weight_str;
		this.car_type_str = car_type_str;
		this.plate_number = plate_number;
		this.car_length = car_length;
		this.price = price;
		this.location = location;
		this.name = name;
		this.phone = phone;
	}

	public int getChecked() {
		return checked;
	}

	public void setChecked(int checked) {
		this.checked = checked;
	}

	public String getLocation_time() {
		return location_time;
	}

	public void setLocation_time(String location_time) {
		this.location_time = location_time;
	}

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public int getCar_weight() {
		return car_weight;
	}

	public void setCar_weight(int car_weight) {
		this.car_weight = car_weight;
	}

	public int getCar_type() {
		return car_type;
	}

	public void setCar_type(int car_type) {
		this.car_type = car_type;
	}

	public String getCar_weight_str() {
		return car_weight_str;
	}

	public void setCar_weight_str(String car_weight_str) {
		this.car_weight_str = car_weight_str;
	}

	public String getCar_type_str() {
		return car_type_str;
	}

	public void setCar_type_str(String car_type_str) {
		this.car_type_str = car_type_str;
	}

	public String getPlate_number() {
		return plate_number;
	}

	public void setPlate_number(String plate_number) {
		this.plate_number = plate_number;
	}

	public double getCar_length() {
		return car_length;
	}

	public void setCar_length(double car_length) {
		this.car_length = car_length;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "CarInfo [way=" + way + ", car_weight=" + car_weight
				+ ", car_type=" + car_type + ", car_weight_str="
				+ car_weight_str + ", car_type_str=" + car_type_str
				+ ", plate_number=" + plate_number + ", car_length="
				+ car_length + ", price=" + price + ", location=" + location
				+ ", name=" + name + ", phone=" + phone + "]";
	}

}
