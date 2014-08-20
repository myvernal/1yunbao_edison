package com.maogousoft.logisticsmobile.driver;


public class Constants {

	// 位置上报间隔
	public static final int LOC_UPDATE_TIME = 60 * 60 * 1000;

	// 服务器请求参数
	public static final String ACTION = "action";

	public static final String TOKEN = "token";

	public static final String JSON = "json";

	// 车型
	public static final String CAR_TYPE = "car_type";

	// 货物类型
	public static final String CARGO_TYPE = "cargo_type";

	// 装卸方式
	public static final String DISBURDEN = "disburden";

	// 运输方式
	public static final String SHIP_TYPE = "ship_type";

	/** 版本类型，1android 2 ios,3 web **/
	public static final int DEVICE_TYPE = 1;

	public static final String SHARED_PREFERENCE_NAME = "client_preferences";

	// PR112 账户类型
	public static final String USER_TYPE = "USER_TYPE";
	// PREFERENCE KEYS

	public static final String CALLBACK_ACTIVITY_PACKAGE_NAME = "CALLBACK_ACTIVITY_PACKAGE_NAME";

	public static final String CALLBACK_ACTIVITY_CLASS_NAME = "CALLBACK_ACTIVITY_CLASS_NAME";

	public static final String API_KEY = "API_KEY";

	public static final String VERSION = "VERSION";

	public static final String XMPP_HOST = "XMPP_HOST";

	public static final String XMPP_PORT = "XMPP_PORT";

	// xmpp服务名字
	public static final String XMPP_SERVER_NAME = "@www.1yunbao.com";

	public static final String XMPP_USERNAME = "XMPP_USERNAME";

	public static final String XMPP_PASSWORD = "XMPP_PASSWORD";

	public static final String XMPP_DRIVER_ID = "XMPP_DRIVER_ID";

	public static final String XMPP_MESSAGE = "messageinfo";

	// 消息接收到的广播
	public static final String MESSAGE_RECEIVE = "receive";

	// 当前会话
	public static final String CURRENT_MESSAGE_RECEIVE = "current_message";

	// public static final String USER_KEY = "USER_KEY";

	public static final String DEVICE_ID = "DEVICE_ID";

	public static final String EMULATOR_DEVICE_ID = "EMULATOR_DEVICE_ID";

	public static final String NOTIFICATION_ICON = "NOTIFICATION_ICON";

	public static final String SETTINGS_NOTIFICATION_ENABLED = "SETTINGS_NOTIFICATION_ENABLED";

	public static final String SETTINGS_SOUND_ENABLED = "SETTINGS_SOUND_ENABLED";

	public static final String SETTINGS_VIBRATE_ENABLED = "SETTINGS_VIBRATE_ENABLED";

	public static final String SETTINGS_TOAST_ENABLED = "SETTINGS_TOAST_ENABLED";

	// NOTIFICATION FIELDS

	public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

	public static final String NOTIFICATION_API_KEY = "NOTIFICATION_API_KEY";

	public static final String NOTIFICATION_TITLE = "NOTIFICATION_TITLE";

	public static final String NOTIFICATION_MESSAGE = "NOTIFICATION_MESSAGE";

	public static final String NOTIFICATION_URI = "NOTIFICATION_URI";

	// INTENT ACTIONS

	public static final String ACTION_SHOW_NOTIFICATION = "org.androidpn.client.SHOW_NOTIFICATION";

	public static final String ACTION_NOTIFICATION_CLICKED = "org.androidpn.client.NOTIFICATION_CLICKED";

	public static final String ACTION_NOTIFICATION_CLEARED = "org.androidpn.client.NOTIFICATION_CLEARED";
	// add by ybxiang begin
	// /** 服务器地址 **/
	// // public static final String DRIVER_SERVER_URL =
	// // "http://www.1yunbao.com/service/driver";
	// public static final String DRIVER_SERVER_URL =
	// "http://www.1yunbao.com/service/driver";
	//
	// /** 公用接口地址 **/
	// public static final String COMMON_SERVER_URL =
	// "http://www.1yunbao.com/service/common";
	//
	// /** 文件上传地址 **/
	// public static final String UPLOAD_FILE_URL =
	// "http://www.1yunbao.com/service/upload/";
	//
	// /** 帮助页面地址 **/
	// public static final String HELP_SERVER_URL =
	// "http://www.1yunbao.com/service/help.html";
	/** 服务器地址 **/
	// public static final String DRIVER_SERVER_URL =
	// "http://www.1yunbao.com/service/driver";
	//public static final String BASE_URL = "http://www.1yunbao.com/service";
	public static final String BASE_URL = "http://112.124.33.14:8083/service";
	//public static final String BASE_URL = "http://192.168.1.101:8888/service";
    /**
     1yunbao.com:8083/admin
     帐号：admin
     密码：111111
     */

	/** 服务器地址 **/
	public static final String DRIVER_SERVER_URL = BASE_URL + "/driver";
	
	/** 货主端注册地址**/
	public static final String SHIPPER_SERVER_URL = BASE_URL + "/user";

	/** 公用接口地址 **/
	public static final String COMMON_SERVER_URL = BASE_URL + "/common";

	/** 文件上传地址 **/
	public static final String UPLOAD_FILE_URL = BASE_URL + "/upload/";

	/** 帮助页面地址 **/
	public static final String HELP_SERVER_URL = BASE_URL + "/help.html";
	// add by ybxiang end

	/** 司机端注册获取验证码 **/
	public static final String DRIVER_REG_GETCODE = "driver_reg_getcode";
	
	/** 货主 注册获取验证码 **/
	public static final String SHIPPER_REG_GETCODE = "user_reg_getcode";

	/** 司机端注册 **/
	public static final String DRIVER_REG = "driver_reg";

	/** 司机端完善资料 **/
	public static final String DRIVER_REG_OPTIONAL = "driver_reg_optional";

	/** 货主注册 **/
	public static final String SHIPPER_REG = "user_reg";

	/** 货主端完善资料 2 **/
	public static final String SHIPPER_REG_OPTIONAL2 = "user_reg_optional";

	/** 诚信认证 **/
	public static final String DRIVER_REG_RENZHENG = "driver_authentication";

	/** 司机端登录 **/
	public static final String DRIVER_LOGIN = "driverlogin";

    /** 货主端登录 **/
    public static final String USER_LOGIN = "userlogin";

	// /** 货源详情 **/
	public static final String GET_SOURCE_ORDER_DETAIL = "get_source_order_detail";

	/** 货源详情 2 **/
	public static final String GET_SOURCE_ORDER_DETAIL2 = "get_source_order_detail_2";

	/** 新货源抢单 **/
	public static final String PLACE_SOURCE_ORDER = "place_source_order";

	/** 搜索新货源 **/
	public static final String SEARCH_SOURCE_ORDER = "search_source_order";
	
	/** 关注此路线  **/
	public static final String FOCUS_LINE = "add_Attention_Line";

	/** 新货源关注 **/
	public static final String ATTENTION_SOURCE_ORDER = "attention_source_order";

	/** 新货源列表 **/
	public static final String QUERY_SOURCE_ORDER = "query_source_order";
	
	/** 好友組列表 **/
	public static final String QUERY_FRIENDS_GROUP = "query_friends_group";
	
	/** 司机端:已关注的所有货主好友列表 **/
	public static final String QUERY_FRIENDS = "query_attentionUserList";

	/** 待定货源列表 **/
	public static final String QUERY_PENDING_SOURCE_ORDER = "query_pending_source_order";

	/** 已完成订单 **/
	public static final String DRIVER_QUERY_FINISHED_ORDER = "driver_query_finished_order";

	/** 在途货源取消抢单 **/
	public static final String CANCEL_PLACE_SOURCE_ORDER = "cancel_place_source_order";

	/** 获取在途货源 **/
	public static final String QUERY_PENDING_SOURCE_ORDER_IN_SHIPPING = "query_pending_source_order_in_shipping";

	/** 更新在途状态 **/
	public static final String SHIPPING_ORDER_UPDATE_STATUS = "shipping_order_update_status";

	/** 更新位置 **/
	public static final String SHIPPING_ORDER_UPDATE_LOCATION = "shipping_order_update_location";

	/** 回单密码 **/
	public static final String VALIDATE_RECEIPT_PASSWORD = "validate_receipt_password";

	/** 我的ABC **/
	public static final String DRIVER_PROFILE = "driver_profile";

	/** 获取字典数据 **/
	public static final String COMMON_GET_DICT_LIST = "common_get_dict_list";

	/** 评价货主 **/
	public static final String RATING_TO_USER = "rating_to_user";

	/** 意见反馈 **/
	public static final String POST_FEEDBACK = "post_feedback";

	/** 改变密码 **/
	public static final String CHANGE_PASSWORD = "change_password";

	/** 账户余额 **/
	public static final String GET_ACCOUNT_GOLD = "get_account_gold";

	/** 信息中心 **/
	public static final String QUERY_MESSAGE = "query_message";

	/** 个人信息 **/
	public static final String GET_USER_INFO = "get_user_info";

	/** 评价列表 **/
	public static final String GET_DRIVER_REPLY = "get_driver_reply";

	/** 货主评价列表 **/
	public static final String GET_USER_REPLY = "get_user_reply";

	/** 忘记密码获取验证码 **/
	public static final String GET_PASSWORD_VCODE = "get_password_vcode";

	/** 找回密码 **/
	public static final String GET_PASSWORD = "get_password";

	/** 28充值请求接口 */
	public static final String COMMON_REQUEST_PAY = "common_request_pay";

	/** 31充值记录 */
	public static final String DRIVER_PAY_HISTORY = "driver_pay_history";

	/** 卡密充值 */
	public static final String DRIVER_COUPON = "driver_coupon";

	/** 添加商户 */
	public static final String ADD_VENDER = "add_vender";

	/** 附近商户列表 **/
	public static final String QUERY_VENDER = "query_vender";

	/** 获取商户评价 **/
	public static final String QUERY_VENDER_REPLY = "query_vender_reply";

	/** 账户记录 */
	public static final String COMMON_GET_BUSINESS = "common_get_business";

	/** 修改主营路线 */
	public static final String DRIVER_UPDATE_LINE = "driver_update_line";

	/** 商户评价 */
	public static final String ADD_VENDER_REPLY = "add_vender_reply";

	/** 分享页面提示 */
	public static final String GET_SHARE_INFO = "get_share_info";

	/** 设置 搜索货源次数 */
	public static final String ADD_SEARCH_COUNT = "add_search_count";

	/** 周边范围 2000 米 */
	public static final int DISTANCE = 2000;

	/** 百度key */
    /**
     * 百度帐号：llanchong
     * 密码：1yunbao56dp
     */
	public static final String strKey = "E109ADE1D56A36882E4C245A18B532DEC2E48DAE";
	public static final String BAIDU_APP_Key = "fGahmvRg6YiiqNTY72uoWE9W";

    /** 百度云检索tableID */
    public static final String BAIDU_LBS_TABLE_ID = "70609";

	/** 易宝 */
	public final static String CUSTOMER_NUMBER = "10012061548";

	public final static String KEY = "og779h1A30w7V637JG8SAF4gQ1g17y7d9ytu849aR6A1493Hj6V8d85H3A15";

	/** 是否已经完善了资料 */
	public static final String IS_REG_OPTIONAL = "is_reg_optional";

	/** 是否已经通过了 诚信认证 */
	public static final String IS_THROUGH_REZHENG = "is_through_rezheng";

	/** 新货源推送，是否响铃 */
	public static final String IS_RING_NEW_SOURCE = "is_ring_new_source";

    /** 货主端:发布货源 */
    public static final String PUBLISH_SOURCE = "publish_order";

    /** 司机端:关注货主 */
    public static final String ATTENTION_SOURCE_USER = "attentionSourceUser";

    /** 司机端:取消关注货主 */
    public static final String CANCEL_ATTENTION_SOURCE_USER = "cancelSourceUser";

    /** 司机端:好友货源 */
    public static final String FRIEND_ORDER_LIST = "getFriendOrderList";

    /** 司机端:关注货源 */
    public static final String QUERY_MAIN_LINE_ORDER = "query_main_line_order";

    /** 司机端:发布车源 */
    public static final String PUBLISH_DRIVER_CAR_INFO = "pulish_driverCarInfo";

    /** 司机端:已发布车源列表 */
    public static final String QUERY_DRIVER_CAR_INFO_LIST = "getPublishOptionsInfoListByDriver";

    /** 司机端:查询多有关注线路 */
    public static final String QUERY_ALL_FOCUS_LINE = "query_Attention_Line";

    /** 司机端:更新关注线路 */
    public static final String UPDATE_FOCUS_LINE = "update_Attention_Line";

    /** 司机端:删除关注线路 */
    public static final String DELETE_ALL_FOCUS_LINE = "delete_Attention_Line";

    /** 货主端:车队添加车辆 */
    public static final String ADD_MY_FLEET = "add_my_fleet";

    /** 货主端:我的车队查询 */
    public static final String QUERY_MY_FLEET = "query_my_fleet";

     /** 广告列表 */
    public static final String QUERY_ADVERT_LIST = "get_advert_list";

    public static final String COMMON_KEY = "common_key";

    public static final int ANONYMOUS_REQUEST_CODE = 1000;
    public static final int ANONYMOUS_RESULT_CODE = 1001;
    public static final int USER_DRIVER = 2; //司机
    public static final int USER_SHIPPER = 1;//货主
	/**
	 * 通过位置获取车型车型
	 */
    public static int[] carTypeValues = new int[]{43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
	public static int getCarTypeValues(int position){
        if(position > carTypeValues.length) {
            return 43;
        }
		return carTypeValues[position];
	}
    /**
     * 通过位置获取货物类型
     */
    public static int[] sourceTypeValues = new int[]{34,36,37,38,39,40,41,42};
    public static int getSourceTypeValues(int position){
        if(position > sourceTypeValues.length) {
            return 34;
        }
        return sourceTypeValues[position];
    }

    public static int[] unitTypeValues = new int[]{83,84,85};
    public static int getUnitTypeValues(int position){
        if(position > unitTypeValues.length) {
            return 34;
        }
        return unitTypeValues[position];
    }
}
