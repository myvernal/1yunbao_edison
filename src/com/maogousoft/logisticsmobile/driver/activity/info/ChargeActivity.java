package com.maogousoft.logisticsmobile.driver.activity.info;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.other.RechargeCardActivity;
import com.maogousoft.logisticsmobile.driver.activity.share.ShareActivity;
import com.maogousoft.logisticsmobile.driver.adapter.CityListAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.CityInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.utils.YeepayUtils;
import com.maogousoft.logisticsmobile.driver.utils.alipay.AlixId;
import com.maogousoft.logisticsmobile.driver.utils.alipay.BaseHelper;
import com.maogousoft.logisticsmobile.driver.utils.alipay.MobileSecurePayHelper;
import com.maogousoft.logisticsmobile.driver.utils.alipay.MobileSecurePayer;
import com.maogousoft.logisticsmobile.driver.utils.alipay.PartnerConfig;
import com.maogousoft.logisticsmobile.driver.utils.alipay.ResultChecker;
import com.maogousoft.logisticsmobile.driver.utils.alipay.Rsa;
import com.yeepay.android.plugin.YeepayPlugin;

/***
 * 充值
 * 
 * @author lenovo
 */
public class ChargeActivity extends BaseActivity implements
		OnCheckedChangeListener, OnItemClickListener {

	private static final String LOGTAG = LogUtil
			.makeLogTag(ChargeActivity.class);

	private AQuery phone, gridView;

	private CityListAdapter mAdapter;

	private EditText price;

	private RadioGroup groups;

	private String OrderId;

	private int type = 0;

	private double gold1 = 0d, gold2 = 0d;

	private ProgressDialog mProgress = null;

	private String amount = "", name = "", body = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_recharge);
		initView();
		initData();
	}

	private void initView() {
		new AQuery(this).id(R.id.titlebar_id_back).clicked(this);
		new AQuery(this).id(R.id.titlebar_id_content).text(
				R.string.string_other_recharge);
		new AQuery(this).id(R.id.titlebar_id_more).clicked(this).visible();

		phone = new AQuery(this).id(R.id.recharge_phone);
		price = (EditText) findViewById(R.id.recharge_price);
		groups = (RadioGroup) findViewById(R.id.recharge_group);
		groups.setOnCheckedChangeListener(this);
		new AQuery(this).id(R.id.recharge_submitbtn).clicked(this);
		new AQuery(this).id(R.id.recharge_records).clicked(this);

		gridView = new AQuery(this).id(R.id.recharge_gridview);
		gridView.itemClicked(this);
		mAdapter = new CityListAdapter(context);
		gridView.adapter(mAdapter);
	}

	private void initData() {
		phone.text(application.getUserName());
		List<CityInfo> list = new ArrayList<CityInfo>();
		list.add(new CityInfo(1, "50元"));
		list.add(new CityInfo(2, "100元"));
		list.add(new CityInfo(4, "200元"));
		list.add(new CityInfo(10, "500元"));
		list.add(new CityInfo(20, "1000元"));
		mAdapter.setList(list);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getGold(1);
	}

	private void getGold(final int type) {
		if (!application.checkNetWork()) {
			showMsg(R.string.network_not_connected);
			return;
		}
		try {
            showSpecialProgress();
			JSONObject params = new JSONObject();
			params.put(Constants.ACTION, Constants.GET_ACCOUNT_GOLD);
			params.put(Constants.TOKEN, application.getToken());
			params.put(Constants.JSON, "");
			ApiClient.doWithObject(Constants.COMMON_SERVER_URL, params, null,
					new AjaxCallBack() {

						public void receive(int code, Object result) {
							dismissProgress();
							switch (code) {
							case ResultCode.RESULT_OK:
								try {
									if (type == 1) {
										gold1 = new JSONObject(result
												.toString()).getDouble("gold");
										LogUtil.e(LOGTAG, "gold1:" + gold1);
										showMsg("当前余额:" + gold1);
									} else {
										gold1 = new JSONObject(result
												.toString()).getDouble("gold");
										LogUtil.e(LOGTAG, "gold2:" + gold2);
										if (gold2 > gold1) {
											BaseHelper.showDialog(
													ChargeActivity.this,
													"温馨提示", "充值成功\n当前余额:"
															+ gold2,
													R.drawable.info);
										}
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							case ResultCode.RESULT_ERROR:
								showMsg(result.toString());
								break;
							case ResultCode.RESULT_FAILED:
								showMsg(result.toString());
								break;

							default:
								break;
							}
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void check() {
		if (TextUtils.isEmpty(price.getText().toString().trim())) {
			showMsg("请输入充值金额");
			price.requestFocus();
			return;
		}
		if (Double.parseDouble(price.getText().toString().trim()) <= 0) {
			showMsg("请输入有效的充值金额");
			price.requestFocus();
			return;
		}
		getOrderId();
	}

	@Override
	public void onClick(View v) {
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		super.onClick(v);
		switch (v.getId()) {
		case R.id.recharge_submitbtn:
			if (type == 2) {
				startActivity(new Intent(this, RechargeCardActivity.class));
			} else {
				check();
			}
			break;
		case R.id.recharge_records: // 账户记录
			startActivity(new Intent(this, AccountRecordActivity.class));
			break;
		case R.id.titlebar_id_more:
			String content = null;
			startActivity(new Intent(context, ShareActivity.class).putExtra(
					"share", content));
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio0:
			type = 0;
			break;
		case R.id.radio1:
			type = 1;
			break;
		case R.id.radio2:
			type = 2;
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		price.setText(String.valueOf(50 * mAdapter.getList().get(position)
				.getId()));
	}

	/** 获取订单号 */
	private void getOrderId() {
		if (!application.checkNetWork()) {
			showMsg(R.string.network_not_connected);
			return;
		}
		try {
			showProgress("生成订单中...");
			JSONObject params = new JSONObject();
			JSONObject json = new JSONObject();
			json.put("uid", application.getUserType() == Constants.USER_SHIPPER? "u" + application.getUserId():application.getDriverId());
			json.put("pay_platform", type);
			json.put("pay_channel", "CH_MOBILE");
			json.put("pay_money",
					Double.parseDouble(price.getText().toString().trim()));
			params.put(Constants.ACTION, Constants.COMMON_REQUEST_PAY);
			params.put(Constants.JSON, json);
			params.put(Constants.TOKEN, application.getToken());
			ApiClient.doWithObject(Constants.COMMON_SERVER_URL, params, null,
					new AjaxCallBack() {

						public void receive(int code, Object result) {
							dismissProgress();
							switch (code) {
							case ResultCode.RESULT_OK:
								try {
									OrderId = String.valueOf(new JSONObject(
											result.toString())
											.getInt("pay_order_id"));
								} catch (JSONException e) {
									e.printStackTrace();
								}
								LogUtil.e(LOGTAG, "OrderId:" + OrderId);
								if (type == 0) {
									startAliPay();
								} else {
									startYeePay();
								}
								break;
							case ResultCode.RESULT_ERROR:
								showMsg(result.toString());
								break;
							case ResultCode.RESULT_FAILED:
								showMsg(result.toString());
								break;

							default:
								break;
							}
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// ///////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////易宝支付/////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////
	/** 开始易宝支付 */
	private void startYeePay() {
		amount = price.getText().toString();
		name = price.getText().toString().trim() + "RMB";
		body = "";

		String time = "" + System.currentTimeMillis();

		StringBuilder builder = new StringBuilder();
		builder.append(Constants.CUSTOMER_NUMBER).append("$");
		builder.append(OrderId).append("$");
		builder.append(amount).append("$");
		builder.append(name).append("$");
		builder.append(time);

		String hmac = YeepayUtils.hmacSign(builder.toString(), Constants.KEY);
		LogUtil.d(LOGTAG, "startPay_hmac:" + hmac);

		Intent intent = new Intent(getBaseContext(), YeepayPlugin.class);
		intent.putExtra("customerNumber", Constants.CUSTOMER_NUMBER); // *商家编号*
		intent.putExtra("requestId", OrderId); // *自定义订单id*
		intent.putExtra("amount", amount); // *金额总价*
		intent.putExtra("productName", name); // *商品名*
		intent.putExtra("time", time); // *时间戳*
		intent.putExtra("productDesc", ""); // 描述
		intent.putExtra("support", "CH_MOBILE"); // 支付渠道 "" 表示全部
		intent.putExtra("environment", "CH_MOBILE"); // 环境，测试|正式
		intent.putExtra("hmac", hmac); // *规则*
		startActivityForResult(intent, 200);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			Bundle params = data.getExtras();
			String amount = params.getString("amount") == null ? "" : params
					.getString("amount") + "元";
			String message = params.getString("errorMessage") == null ? ""
					: params.getString("errorMessage");
			String requestId = params.getString("requestId") == null ? ""
					: params.getString("requestId");

			StringBuilder builder = new StringBuilder();
			builder.append(params.getString("returnCode"));
			builder.append("$").append(params.getString("customerNumber"));
			builder.append("$").append(params.getString("requestId"));
			builder.append("$").append(params.getString("amount"));
			builder.append("$").append(params.getString("appId"));
			builder.append("$").append(message);
			builder.append("$").append(params.getString("time"));

			String hmac = YeepayUtils.hmacSign(builder.toString(),
					Constants.KEY);
			LogUtil.d(LOGTAG, "result_hmac:" + hmac);

			if (TextUtils.isEmpty(requestId) && TextUtils.isEmpty(amount)) {
				showMsg("支付失败");
			} else {
				// showMsg("支付成功!\n支付金额：" + amount);
				LogUtil.e(LOGTAG, "支付成功!\n\n订单号：" + requestId + "\n支付金额："
						+ amount);
				BaseHelper.showDialog(ChargeActivity.this, "温馨提示",
						"支付成功!\n由于易宝充值后台处理较慢，所以请5分钟后刷新余额查看", R.drawable.info);
				// getGold(2);
			}
		} else {
			showMsg("支付失败");
		}
	}

	// ///////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////支付宝支付///////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////

	private void startAliPay() {
		amount = price.getText().toString();
		name = "物流币:" + price.getText().toString().trim();
		body = "充值物流币" + price.getText().toString().trim() + ",充值多多，优惠多多";
		// 检测安全支付服务是否安装
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
		boolean isMobile_spExist = mspHelper.detectMobile_sp();
		if (!isMobile_spExist)
			return;

		// 检测配置信息
		if (!checkInfo()) {
			BaseHelper.showDialog(this, "提示", "缺少partner或者seller",
					R.drawable.infoicon);
			return;
		}

		// 根据订单信息开始进行支付
		try {
			// 准备订单信息
			String orderInfo = getOrderInfo();
			// 这里根据签名方式对订单信息进行签名
			String signType = getSignType();
			String strsign = sign(signType, orderInfo);
			Log.v("sign:", strsign);
			// 对签名进行编码
			strsign = URLEncoder.encode(strsign);
			// 组装好参数
			String info = orderInfo + "&sign=" + "\"" + strsign + "\"" + "&"
					+ getSignType();
			Log.v("orderInfo:", info);
			// 调用pay方法进行支付
			MobileSecurePayer msp = new MobileSecurePayer();
			boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY, this);

			if (bRet) {
				// 显示“正在支付”进度条
				closeProgress();
				mProgress = BaseHelper.showProgress(this, null, "正在支付", false,
						true);
			}
		} catch (Exception ex) {
			Toast.makeText(ChargeActivity.this, R.string.remote_call_failed,
					Toast.LENGTH_SHORT).show();
		}
	}

	// 这里接收支付结果，支付宝手机端同步通知
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			try {
				String strRet = (String) msg.obj;

				Log.e(LOGTAG, strRet); // strRet范例：resultStatus={9000};memo={};result={partner="2088201564809153"&seller="2088201564809153"&out_trade_no="050917083121576"&subject="123456"&body="2010新款NIKE 耐克902第三代板鞋 耐克男女鞋 386201 白红"&total_fee="0.01"&notify_url="http://notify.java.jpxx.org/index.jsp"&success="true"&sign_type="RSA"&sign="d9pdkfy75G997NiPS1yZoYNCmtRbdOP0usZIMmKCCMVqbSG1P44ohvqMYRztrB6ErgEecIiPj9UldV5nSy9CrBVjV54rBGoT6VSUF/ufjJeCSuL510JwaRpHtRPeURS1LXnSrbwtdkDOktXubQKnIMg2W0PreT1mRXDSaeEECzc="}
				switch (msg.what) {
				case AlixId.RQF_PAY: {
					closeProgress();
					BaseHelper.log(LOGTAG, strRet);

					// 处理交易结果
					try {
						// 获取交易状态码，具体状态代码请参看文档
						String tradeStatus = "resultStatus={";
						int imemoStart = strRet.indexOf("resultStatus=");
						imemoStart += tradeStatus.length();
						int imemoEnd = strRet.indexOf("};memo=");
						tradeStatus = strRet.substring(imemoStart, imemoEnd);

						// 先验签通知
						ResultChecker resultChecker = new ResultChecker(strRet);
						int retVal = resultChecker.checkSign();
						// 验签失败
						if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
							BaseHelper.showDialog(
									ChargeActivity.this,
									"提示",
									getResources().getString(
											R.string.check_sign_failed),
									android.R.drawable.ic_dialog_alert);
						} else {// 验签成功。验签成功后再判断交易状态码
							if (tradeStatus.equals("9000"))// 判断交易状态码，只有9000表示交易成功
								BaseHelper.showDialog(ChargeActivity.this,
										"提示", "支付成功。交易状态码：" + tradeStatus,
										R.drawable.infoicon);
							else
								BaseHelper.showDialog(ChargeActivity.this,
										"提示", "支付失败。交易状态码:" + tradeStatus,
										R.drawable.infoicon);
						}

					} catch (Exception e) {
						e.printStackTrace();
						BaseHelper.showDialog(ChargeActivity.this, "提示",
								strRet, R.drawable.infoicon);
					}
				}
					break;
				}
				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 获取商品订单信息 position 商品在列表中的位置
	 */
	String getOrderInfo() {
		String strOrderInfo = "partner=" + "\"" + PartnerConfig.PARTNER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "seller=" + "\"" + PartnerConfig.SELLER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "out_trade_no=" + "\"" + OrderId + "\"";
		strOrderInfo += "&";
		strOrderInfo += "subject=" + "\"" + name + "\"";
		strOrderInfo += "&";
		strOrderInfo += "body=" + "\"" + body + "\"";
		strOrderInfo += "&";
		strOrderInfo += "total_fee=" + "\"" + amount + "\"";
		strOrderInfo += "&";
		strOrderInfo += "notify_url=" + "\""
				+ "http://notify.java.jpxx.org/index.jsp" + "\"";

		return strOrderInfo;
	}

	/**
	 * lephone系统使用到的取消dialog监听
	 */
	public static class AlixOnCancelListener implements
			DialogInterface.OnCancelListener {

		Activity mcontext;

		public AlixOnCancelListener(Activity context) {
			mcontext = context;
		}

		public void onCancel(DialogInterface dialog) {
			mcontext.onKeyDown(KeyEvent.KEYCODE_BACK, null);
		}
	}

	/**
	 * 获取签名方式
	 */
	String getSignType() {
		String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
		return getSignType;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param signType
	 *            签名方式
	 * @param content
	 *            待签名订单信息
	 */
	String sign(String signType, String content) {
		return Rsa.sign(content, PartnerConfig.RSA_PRIVATE);
	}

	/**
	 * 关闭进度框
	 */
	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检测配置信息 partnerid商户id，seller收款帐号不能为空
	 */
	private boolean checkInfo() {
		String partner = PartnerConfig.PARTNER;
		String seller = PartnerConfig.SELLER;
		if (partner == null || partner.length() <= 0 || seller == null
				|| seller.length() <= 0)
			return false;
		return true;
	}

}