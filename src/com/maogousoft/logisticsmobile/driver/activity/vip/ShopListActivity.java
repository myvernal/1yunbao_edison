package com.maogousoft.logisticsmobile.driver.activity.vip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseListActivity;
import com.maogousoft.logisticsmobile.driver.adapter.ShopListAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.ShopInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 商户列表
 * 
 * @author lenovo
 */
public class ShopListActivity extends BaseListActivity implements OnScrollListener{

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

	private double longitude;
	private double latitude;

	private int shopType = -1;

	private Button titlebar_id_more;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initViews();
		initListener();
	}

	private void initViews() {

		ibSearch = (ImageButton) findViewById(R.id.ib_search);

		titlebar_id_more = (Button) findViewById(R.id.titlebar_id_more);
		titlebar_id_more.setText("添加园区");
		mBack = (Button) findViewById(R.id.titlebar_id_back);
		((TextView) findViewById(R.id.titlebar_id_content)).setText("物流园区");
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

	private void initListener() {

		titlebar_id_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                //进入添加物流园区的页面
                startActivity(new Intent(context, AddActivity.class));
//				final GrabDialog dialog = new GrabDialog(context);
//				dialog.show();
//				final EditText mInput = (EditText) dialog.findViewById(android.R.id.text1);
//				dialog.setTitle("提示");
//				dialog.setMessage("请先输入权限密码");
//				dialog.setLeftButton("确定", new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//
//						dialog.dismiss();
//
//						if (TextUtils.isEmpty(mInput.getText().toString())) {
//							Toast.makeText(ShopListActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
//							return;
//						}
//
//						// mInput
//						// 登录
//						final String username = "11111111111";
//						final String password = mInput.getText().toString();
//
//						final JSONObject jsonObject = new JSONObject();
//						try {
//							showDefaultProgress();
//							jsonObject.put(Constants.ACTION, Constants.DRIVER_LOGIN);
//							jsonObject.put(Constants.TOKEN, null);
//							jsonObject.put(
//									Constants.JSON,
//									new JSONObject().put("phone", username).put("password", MD5.encode(password))
//											.put("device_type", Constants.DEVICE_TYPE).toString());
//							ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject, UserInfo.class,
//									new AjaxCallBack() {
//
//										@Override
//										public void receive(int code, Object result) {
//											dismissProgress();
//											switch (code) {
//												case ResultCode.RESULT_OK:
//													startActivity(new Intent(context, AddActivity.class));
//													break;
//
//												default:
//													Toast.makeText(ShopListActivity.this, "密码错误", Toast.LENGTH_SHORT)
//															.show();
//													break;
//
//											}
//										}
//									});
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//
//					}
//				});
//				dialog.setRightButton("取消", new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						dialog.dismiss();
//					}
//				});

			}
		});
	}


	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
//		Intent intent = new Intent(context, ShopDetailActivity.class);
//		intent.putExtra("ShopInfo", (ShopInfo) mAdapter.getItem(position));
//		startActivityForResult(intent, 1000);
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
			jsonObject.put(Constants.ACTION, Constants.QUERY_VENDER);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, new JSONObject().put("page", page)
                    .put("latitude", latitude)
                    .put("longitude", longitude).toString());
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

			jsonObject.put(Constants.JSON,
					new JSONObject().put("keyword", keyword).put("latitude", latitude).put("longitude", longitude)
							.toString());
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject, ShopInfo.class, new AjaxCallBack() {

				@Override
				public void receive(int code, Object result) {
					setListShown(true);
					switch (code) {
						case ResultCode.RESULT_OK:

							if (result instanceof List) {

								List<ShopInfo> shopInfoList = (List<ShopInfo>) result;

								if (shopInfoList == null || shopInfoList.isEmpty()) {
									load_all = true;
									mFootProgress.setVisibility(View.GONE);
									mFootMsg.setText("已加载全部");
								} else {
									if (shopInfoList.size() < 10) {
										load_all = true;
										mFootProgress.setVisibility(View.GONE);
										mFootMsg.setText("已加载全部");
									} else {
										load_all = false;
										mFootProgress.setVisibility(View.VISIBLE);
										mFootMsg.setText(R.string.tips_isloading);
									}

									mAdapter.removeAll();
									mAdapter.addAll(shopInfoList);
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

}
