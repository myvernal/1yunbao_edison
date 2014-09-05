package com.maogousoft.logisticsmobile.driver.activity.vip;

import java.util.ArrayList;
import java.util.List;

import com.maogousoft.logisticsmobile.driver.activity.other.MapActivity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseListActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.ImagePagerActivity;
import com.maogousoft.logisticsmobile.driver.adapter.ImageGridAdapter;
import com.maogousoft.logisticsmobile.driver.adapter.ShopDetailListAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.EvaluateInfo;
import com.maogousoft.logisticsmobile.driver.model.ShopEvaluate;
import com.maogousoft.logisticsmobile.driver.model.ShopInfo;
import com.maogousoft.logisticsmobile.driver.widget.MyGridView;

/**
 * 商户信息
 * 
 * @version 1.0.0
 * @author wst
 * @date 2013-5-22 下午8:52:43
 */
public class ShopDetailActivity extends BaseListActivity implements OnScrollListener {

	private Button mBack;

	private ListView mListView;

	private RatingBar mScore;

	private TextView creditPhone;

	private TextView tvType, tvName, tvNum;

	private TextView tvScore1, tvScore2, tvScore3;

	private RatingBar ratingBarScore1, ratingBarScore2, ratingBarScore3;

	private TextView tvYouHui;

	private Button btnDianpin;

	private Button btnMobilephone, btnPhone;

	private Button btnAddr;

	private MyGridView mGridView;

	private ImageGridAdapter mImageAdapter;

	private ShopDetailListAdapter mVipShopDetailAdapter;

	private int user_id;

	private String user_phone;

	private float user_score;

	private ShopInfo shopInfo;

	// 底部更多
	private View mFootView;

	private ProgressBar mFootProgress;

	private TextView mFootMsg;

	// 当前模式
	private int state = WAIT;

	// 当前页码
	private int pageIndex = 1;

	// 滑动状态
	private boolean state_idle = false;

	// 已加载全部
	private boolean load_all = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vip_shopdetail);
		initViews();
		initListener();
		initData();
		// queryData(pageIndex);
	}

	// 初始化视图
	private void initViews() {
		mBack = (Button) findViewById(R.id.titlebar_id_back);
		((TextView) findViewById(R.id.titlebar_id_content)).setText("商户信息");
		mListView = (ListView) findViewById(android.R.id.list);

		mFootView = getLayoutInflater().inflate(R.layout.listview_footview, null);
		mFootView.setClickable(false);
		mFootProgress = (ProgressBar) mFootView.findViewById(android.R.id.progress);
		mFootMsg = (TextView) mFootView.findViewById(android.R.id.text1);
		mListView.addFooterView(mFootView);

		mListView.setOnScrollListener(this);
		mBack.setOnClickListener(this);

		tvType = (TextView) findViewById(R.id.tv_type);
		tvName = (TextView) findViewById(R.id.tv_name);
		tvName.getPaint().setFakeBoldText(true);
		tvNum = (TextView) findViewById(R.id.tv_num);

		tvScore1 = (TextView) findViewById(R.id.tv_credit_score1);
		tvScore2 = (TextView) findViewById(R.id.tv_credit_score2);
		tvScore3 = (TextView) findViewById(R.id.tv_credit_score3);

		ratingBarScore1 = (RatingBar) findViewById(R.id.credit_score1);
		ratingBarScore1.setIsIndicator(true);
		ratingBarScore2 = (RatingBar) findViewById(R.id.credit_score2);
		ratingBarScore2.setIsIndicator(true);
		ratingBarScore3 = (RatingBar) findViewById(R.id.credit_score3);
		ratingBarScore3.setIsIndicator(true);

		tvYouHui = (TextView) findViewById(R.id.tv_youhui);

		btnDianpin = (Button) findViewById(R.id.btn_dianpin);

		btnMobilephone = (Button) findViewById(R.id.btn_mobilephone);
		btnPhone = (Button) findViewById(R.id.btn_phone);

		btnAddr = (Button) findViewById(R.id.btn_addr);

		mGridView = (MyGridView) findViewById(R.id.source_id_order_gridview);

	}

	private void initListener() {
		btnDianpin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopDetailActivity.this, EvaluateActivity.class);
				intent.putExtra("ShopInfo", shopInfo);
				startActivity(intent);
			}
		});

		btnMobilephone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (shopInfo != null && !TextUtils.isEmpty(shopInfo.getVender_mobile())) {
					Uri uri = Uri.parse("tel:" + shopInfo.getVender_mobile());
					Intent intent = new Intent(Intent.ACTION_DIAL, uri);
					startActivity(intent);
				}
			}
		});
		btnPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (shopInfo != null && !TextUtils.isEmpty(shopInfo.getVender_phone())) {
					Uri uri = Uri.parse("tel:" + shopInfo.getVender_phone());
					Intent intent = new Intent(Intent.ACTION_DIAL, uri);
					startActivity(intent);
				}
			}
		});

		btnAddr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, MapActivity.class);
				intent.putExtra(Constants.COMMON_KEY, shopInfo);
				context.startActivity(intent);
			}
		});
	}

	// 初始化数据
	private void initData() {

		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			shopInfo = (ShopInfo) bundle.getSerializable("ShopInfo");
			if (shopInfo == null) {
				return;
			}
		} else {
			return;
		}

		mImageAdapter = new ImageGridAdapter(context);
		mGridView.setAdapter(mImageAdapter);
		final ArrayList<String> listImageUrl = new ArrayList<String>();
		if (!TextUtils.isEmpty(shopInfo.getPhoto1()) && !shopInfo.getPhoto1().equals("")) {
			listImageUrl.add(shopInfo.getPhoto1());
		}
		if (!TextUtils.isEmpty(shopInfo.getPhoto2()) && !shopInfo.getPhoto1().equals("")) {
			listImageUrl.add(shopInfo.getPhoto2());
		}
		if (!TextUtils.isEmpty(shopInfo.getPhoto3()) && !shopInfo.getPhoto1().equals("")) {
			listImageUrl.add(shopInfo.getPhoto3());
		}
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				startActivity(new Intent(context, ImagePagerActivity.class).putStringArrayListExtra("images", listImageUrl));
			}
		});
		mImageAdapter.setList(listImageUrl);

		String categoryStr = "";
		switch (shopInfo.getCategory()) {

		case 0:
			categoryStr = "住宿优惠";
			break;
		case 1:
			categoryStr = "加油优惠";
			break;
		case 2:
			categoryStr = "餐饮折扣";
			break;
		case 3:
			categoryStr = "休闲优惠";
			break;
		case 4:
			categoryStr = "维修保养";
			break;
		case 5:
			categoryStr = "其他";
			break;

		default:
			categoryStr = "无";
			break;
		}

		tvType.setText("类别：" + categoryStr);
		tvName.setText(shopInfo.getVender_name());
		tvNum.setText("已阅" + shopInfo.getRead_time() + "次");

		tvScore1.setText("服务态度");
		tvScore2.setText("技术水平");
		tvScore3.setText("诚信经营");

		float score1 = (float) shopInfo.getScore1();
		float score2 = (float) shopInfo.getScore2();
		float score3 = (float) shopInfo.getScore3();

		if (score1 == 0) {
			score1 = 5;
		}
		if (score2 == 0) {
			score2 = 5;
		}
		if (score3 == 0) {
			score3 = 5;
		}

		ratingBarScore1.setRating(score1);
		ratingBarScore2.setRating(score2);
		ratingBarScore3.setRating(score3);

		String normalPrice = "<STRIKE>" + shopInfo.getNormal_price() + "</STRIKE>";
		tvYouHui.setText(Html.fromHtml(shopInfo.getGoods_name() + normalPrice + "　会员" + shopInfo.getMember_price()));

		if (!TextUtils.isEmpty(shopInfo.getVender_mobile())) {
			btnMobilephone.setText(shopInfo.getVender_mobile());
		} else {
			btnMobilephone.setText("无手机号码");
		}
		if (!TextUtils.isEmpty(shopInfo.getVender_phone())) {
			btnPhone.setText(shopInfo.getVender_phone());
		} else {
			btnPhone.setText("无座机号码");
		}

		if (!TextUtils.isEmpty(shopInfo.getVender_address())) {
			btnAddr.setText("地址：" + shopInfo.getVender_address());
		} else {
			btnAddr.setText("无地址信息");
		}

		mVipShopDetailAdapter = new ShopDetailListAdapter(context);
		mListView.setAdapter(mVipShopDetailAdapter);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mVipShopDetailAdapter.isEmpty()) {
			pageIndex = 1;
			queryData(pageIndex);
		}
	}

	// 获取评价列表
	private void queryData(int page) {
        showSpecialProgress();
		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION, Constants.QUERY_VENDER_REPLY);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, new JSONObject().put("vender_id", shopInfo.getId()).toString());
			ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject, ShopEvaluate.class, new AjaxCallBack() {

				@Override
				public void receive(int code, Object result) {
					dismissProgress();
					switch (code) {
					case ResultCode.RESULT_OK:
						List<ShopEvaluate> mList = (List<ShopEvaluate>) result;
						if (mList == null || mList.isEmpty()) {
							load_all = true;
							mFootProgress.setVisibility(View.GONE);
							mFootMsg.setText("已加载全部");
						} else {
							if (mList.size() < 10) {
								load_all = true;
								mFootProgress.setVisibility(View.GONE);
								mFootMsg.setText("已加载全部");
							} else {
								load_all = false;
								mFootProgress.setVisibility(View.VISIBLE);
								mFootMsg.setText(R.string.tips_isloading);
							}

							((ShopDetailListAdapter) mVipShopDetailAdapter).addAll(mList);
						}
						break;
					case ResultCode.RESULT_ERROR:
						if (result instanceof String)
							showMsg(result.toString());
						break;
					case ResultCode.RESULT_FAILED:
						if (result instanceof String)
							showMsg(result.toString());
						break;

					default:
						break;
					}
					if (mVipShopDetailAdapter.isEmpty()) {
						setEmptyText("没有找到数据哦");
					}
					state = WAIT;
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState != OnScrollListener.SCROLL_STATE_IDLE) {
			return;
		}
		if (state != WAIT) {
			return;
		}
		this.state_idle = true;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (!this.state_idle) {
			return;
		}
		if (firstVisibleItem == 0 || (firstVisibleItem + visibleItemCount) != totalItemCount) {
			return;
		}
		// 如果当前没有加载数据
		if (state != ISREFRESHING && !load_all) {
			queryData(++pageIndex);
		}
	}
}
