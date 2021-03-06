package com.maogousoft.logisticsmobile.driver.activity.vip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseListActivity;
import com.maogousoft.logisticsmobile.driver.activity.other.MapActivity;
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
public class ShopListActivity extends BaseListActivity implements OnScrollListener, BDLocationListener{

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
    private LocationClient mLocClient;
	private Button titlebar_id_more;
    private boolean isFirstLoc = true;
    private JSONObject params;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
        initData();
		initListener();
//        locationAction();
	}

	private void initViews() {
		ibSearch = (ImageButton) findViewById(R.id.ib_search);
		titlebar_id_more = (Button) findViewById(R.id.titlebar_id_more);
		titlebar_id_more.setText("添加园区");

		((TextView) findViewById(R.id.titlebar_id_content)).setText("物流园区");
		mFootView = getLayoutInflater().inflate(R.layout.listview_footview, null);
		mFootView.setClickable(false);
		mFootProgress = (ProgressBar) mFootView.findViewById(android.R.id.progress);
		mFootMsg = (TextView) mFootView.findViewById(android.R.id.text1);
		mListView.addFooterView(mFootView);

		mListView.setOnScrollListener(this);

		mAdapter = new ShopListAdapter(mContext);
		setListAdapter(mAdapter);
		setListShown(false);
	}

	private void initListener() {
		titlebar_id_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                //进入添加物流园区的页面
                startActivity(new Intent(mContext, AddActivity.class));
			}
		});
	}

    private void initData() {
        String str = getIntent().getStringExtra(Constants.COMMON_KEY);
        try {
            params = new JSONObject(str);
            getData(pageIndex);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(mContext, MapActivity.class);
        intent.putExtra(Constants.MAP_TYPE, Constants.MAP_TYPE_SHOP);
        intent.putExtra(Constants.COMMON_KEY, (ShopInfo) mAdapter.getItem(position));
        mContext.startActivity(intent);
	}

	// 请求指定页数的数据
	private void getData(int page) {
		try {
			state = ISREFRESHING;
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constants.ACTION, Constants.QUERY_VENDER);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, params.put("page", page));
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
                                    mAdapter.notifyDataSetChanged();
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
									if (shopInfoList.size() < 20) {
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
            mFootProgress.setVisibility(View.VISIBLE);
            mFootMsg.setText(R.string.tips_isloading);
		}
	}

    // 定位初始化
    private void locationAction() {
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        // 定位成功后不再处理新接收的位置
        if (location == null || !isFirstLoc) {
            return;
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        getData(pageIndex);
        isFirstLoc = false;
        mLocClient.stop();
    }

    public void onReceivePoi(BDLocation poiLocation) {
    }
}
