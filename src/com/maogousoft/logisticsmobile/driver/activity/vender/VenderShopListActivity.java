package com.maogousoft.logisticsmobile.driver.activity.vender;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseListActivity;
import com.maogousoft.logisticsmobile.driver.adapter.ShopListAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.ShopInfo;
import com.maogousoft.logisticsmobile.driver.model.UserInfo;
import com.maogousoft.logisticsmobile.driver.utils.GrabDialog;
import com.maogousoft.logisticsmobile.driver.utils.MD5;

/**
 * 商户列表
 * 
 * @author lenovo
 */
public class VenderShopListActivity extends BaseListActivity implements OnScrollListener, BDLocationListener {

	private Button mBack;
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

	private ImageButton ibSearch;
	private EditText edtSearch;

	/** 定位相关 控制器 */
	private LocationClient mLocClient;

	private double longitude;
	private double latitude;

	private int shopType = -1;

	private RadioButton radioNear, radioCategory;

	private RadioGroup radioGroup;

	private Button titlebar_id_more;

	private View viewSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		initLinstener();
		initData();
		initMap();

		uploadLocation();
	}

	private void initViews() {
		LinearLayout viewContainer = (LinearLayout) findViewById(R.id.home_list_container);
		viewSearch = LayoutInflater.from(this).inflate(R.layout.activity_vip_shoplist, null);
		viewContainer.addView(viewSearch, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		ibSearch = (ImageButton) findViewById(R.id.ib_search);
		edtSearch = (EditText) findViewById(R.id.edt_search);

		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radioNear = (RadioButton) findViewById(R.id.radio_near);
		radioCategory = (RadioButton) findViewById(R.id.radio_category);

		titlebar_id_more = (Button) findViewById(R.id.titlebar_id_more);
		titlebar_id_more.setText("添加商户");

		mBack = (Button) findViewById(R.id.titlebar_id_back);
		((TextView) findViewById(R.id.titlebar_id_content)).setText("商户列表");
		mFootView = getLayoutInflater().inflate(R.layout.listview_footview, null);
		mFootView.setClickable(false);
		mFootProgress = (ProgressBar) mFootView.findViewById(android.R.id.progress);
		mFootMsg = (TextView) mFootView.findViewById(android.R.id.text1);
		mListView.addFooterView(mFootView);

		mListView.setOnScrollListener(this);
		mBack.setOnClickListener(this);

		mAdapter = new ShopListAdapter(context);
		setListAdapter(mAdapter);
		setListShown(false);
	}

	private void initLinstener() {
		ibSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (TextUtils.isEmpty(edtSearch.getText().toString())) {
					return;
				}

				searchData(edtSearch.getText().toString());

			}
		});

		radioCategory.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					finish();
					startActivity(new Intent(context, VenderIndexGridActivity.class));
				}
			}
		});

		titlebar_id_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final GrabDialog dialog = new GrabDialog(context);
				dialog.show();
				final EditText mInput = (EditText) dialog.findViewById(android.R.id.text1);
				dialog.setTitle("提示");
				dialog.setMessage("请先输入权限密码");
				dialog.setLeftButton("确定", new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						dialog.dismiss();

						if (TextUtils.isEmpty(mInput.getText().toString())) {
							Toast.makeText(VenderShopListActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
							return;
						}

						// mInput
						// 登录
						final String username = "11111111111";
						final String password = mInput.getText().toString();

						final JSONObject jsonObject = new JSONObject();
						try {
							showDefaultProgress();
							jsonObject.put(Constants.ACTION, Constants.DRIVER_LOGIN);
							jsonObject.put(Constants.TOKEN, null);
							jsonObject.put(
									Constants.JSON,
									new JSONObject().put("phone", username).put("password", MD5.encode(password))
											.put("device_type", Constants.DEVICE_TYPE).toString());
							ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject, UserInfo.class,
									new AjaxCallBack() {

										@Override
										public void receive(int code, Object result) {
											dismissProgress();
											switch (code) {
												case ResultCode.RESULT_OK:
													startActivity(new Intent(context, com.maogousoft.logisticsmobile.driver.activity.vip.AddActivity.class));
													break;

												default:
													Toast.makeText(VenderShopListActivity.this, "密码错误", Toast.LENGTH_SHORT)
															.show();
													break;

											}
										}
									});
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
				dialog.setRightButton("取消", new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

			}
		});
	}

	private void initData() {

		if (getIntent().hasExtra("type")) {
			shopType = getIntent().getIntExtra("type", -1);
			if (radioGroup != null) {
				radioGroup.setVisibility(View.GONE);
			}
		}

	}

	private void initMap() {
		/*if (application.getBMapManager() == null) {
			application.initBMapManager();
		}*/

		LocationClientOption option = new LocationClientOption();
		// option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(200); // 定位时间，毫秒 小于1秒则一次定位;大于等于1秒则定时定位
		mLocClient = new LocationClient(this);
		mLocClient.setLocOption(option);

	}

	private void uploadLocation() {
		mLocClient.registerLocationListener(this);
		mLocClient.start();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(context, VenderShopDetailActivity.class);
		intent.putExtra("ShopInfo", (ShopInfo) mAdapter.getItem(position));
		startActivityForResult(intent, 1000);
	}

	// 请求指定页数的数据
	private void getData(int page) {

		if (latitude == 0 || longitude == 0) {
			showMsg("请等待获取位置");
			return;
		}

		try {
			state = ISREFRESHING;
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constants.ACTION, Constants.QUERY_VENDER_OLD);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON,
							new JSONObject().put("page", page).put("latitude", latitude).put("longitude", longitude)
									.toString());
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject, ShopInfo.class, new AjaxCallBack() {

				@Override
				public void receive(int code, Object result) {
					setListShown(true);
					switch (code) {
						case ResultCode.RESULT_OK:

							if (result instanceof List) {

								List<ShopInfo> shopInfos = (List<ShopInfo>) result;

								if (shopInfos == null || shopInfos.isEmpty()) {
									load_all = true;
									mFootProgress.setVisibility(View.GONE);
									mFootMsg.setText("已加载全部");
								} else {
									if (shopInfos.size() < 10) {
										load_all = true;
										mFootProgress.setVisibility(View.GONE);
										mFootMsg.setText("已加载全部");
									} else {
										load_all = false;
										mFootProgress.setVisibility(View.VISIBLE);
										mFootMsg.setText(R.string.tips_isloading);
									}

									ArrayList<ShopInfo> shopInfosTemp = new ArrayList<ShopInfo>();

									if (shopType != -1) {
										for (ShopInfo shop : shopInfos) {
											if (shop.getCategory() == shopType) {
												shopInfosTemp.add(shop);
											}
										}
										mAdapter.addAll(shopInfosTemp);
									} else {
										mAdapter.addAll(shopInfos);
									}
								}
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
					if (mAdapter.isEmpty()) {
						setEmptyText("没有找到数据哦");
					}
					state = WAIT;
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 搜索数据
	private void searchData(String keyword) {

		if (latitude == 0 || longitude == 0) {
			showMsg("请等待获取位置");
			return;
		}

		try {
			state = ISREFRESHING;
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constants.ACTION, Constants.QUERY_VENDER);
			jsonObject.put(Constants.TOKEN, application.getToken());

			// Keyword String 否 查询关键字 按商家名称和服务名称查询

			jsonObject.put(Constants.JSON, new JSONObject().put("keyword", keyword).put("latitude", latitude).put("longitude", longitude).toString());
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject, ShopInfo.class, new AjaxCallBack() {

				@Override
				public void receive(int code, Object result) {
					setListShown(true);
					switch (code) {
						case ResultCode.RESULT_OK:

							if (result instanceof List) {

								List<ShopInfo> shopInfos = (List<ShopInfo>) result;

								if (shopInfos == null || shopInfos.isEmpty()) {
									load_all = true;
									mFootProgress.setVisibility(View.GONE);
									mFootMsg.setText("已加载全部");
								} else {
									if (shopInfos.size() < 10) {
										load_all = true;
										mFootProgress.setVisibility(View.GONE);
										mFootMsg.setText("已加载全部");
									} else {
										load_all = false;
										mFootProgress.setVisibility(View.VISIBLE);
										mFootMsg.setText(R.string.tips_isloading);
									}

									mAdapter.removeAll();
									mAdapter.addAll(shopInfos);
								}
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
					if (mAdapter.isEmpty()) {
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
			getData(++pageIndex);
		}
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// if (resultCode == RESULT_OK && requestCode == 1000) {
	// System.out.println("执行onActivityResult");
	// if (data.hasExtra("sourceInfo")) {
	// NewSourceInfo sourceInfo = (NewSourceInfo) data.getSerializableExtra("sourceInfo");
	// try {
	// for (int i = 0; i < mAdapter.getList().size(); i++) {
	// if (sourceInfo.getId().intValue() == ((NewSourceInfo) mAdapter.getList().get(i)).getId()
	// .intValue()) {
	// ((NewSourceInfo) mAdapter.getList().get(i)).setFavorite_status(sourceInfo
	// .getFavorite_status());
	// mAdapter.notifyDataSetChanged();
	// break;
	// }
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }
	// }

	@Override
	public void onReceiveLocation(BDLocation arg0) {
		mLocClient.unRegisterLocationListener(this);
		dismissProgress();
		if (arg0 == null) {
			return;
		}
		longitude = arg0.getLongitude();
		latitude = arg0.getLatitude();

		if (mAdapter.isEmpty()) {
			pageIndex = 1;
			getData(pageIndex);
		}
	}

	@Override
	public void onReceivePoi(BDLocation arg0) {

	}

	@Override
	protected void onResume() {
		radioNear.setChecked(true);

		super.onResume();
	}

}
