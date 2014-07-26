package com.ybxiang.driver.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseListActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.SourceDetailActivity;
import com.maogousoft.logisticsmobile.driver.adapter.FriendsGroupListAdapter;
import com.maogousoft.logisticsmobile.driver.adapter.NewSourceListAdapter;
import com.maogousoft.logisticsmobile.driver.model.FriendsGroup;

public class MyFriendsActivity extends BaseListActivity implements
		OnClickListener,OnScrollListener {
	private Context mContext;
	private Button mTitleBarBack;
	private Button mTitleBarMore;
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
		mContext = MyFriendsActivity.this;
		initViews();
	}

	private void initViews() {
		
		((TextView)findViewById(R.id.titlebar_id_content)).setText("我的好友");
		// 返回按钮生效
		mTitleBarBack = (Button) findViewById(R.id.titlebar_id_back);
		mTitleBarBack.setOnClickListener(this);
		// 更多按钮隐藏
		mTitleBarMore = (Button) findViewById(R.id.titlebar_id_more);
		mTitleBarMore.setVisibility(View.GONE);

		// 数据加载中进度条
		mFootView = getLayoutInflater().inflate(R.layout.listview_footview,null);
		mFootView.setClickable(false);
		mFootProgress = (ProgressBar) mFootView.findViewById(android.R.id.progress);
		mFootMsg = (TextView) mFootView.findViewById(android.R.id.text1);
		mListView.addFooterView(mFootView);
		
		// 初始化friendsGroup的adapter
		mAdapter = new FriendsGroupListAdapter(mContext);
		setListAdapter(mAdapter);
		//list未加载数据不显示
		setListShown(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		android.util.Log.d("ybxiang", "mAdapter.isEmpty()=="+mAdapter.isEmpty());
		if (mAdapter.isEmpty()) {
			pageIndex = 1;
			getData(pageIndex);
		}
	}

	// 请求指定页数的数据
	private void getData(int page) {
//		try {
			state = ISREFRESHING;
//			final JSONObject jsonObject = new JSONObject();
//			jsonObject.put(Constants.ACTION, Constants.QUERY_FRIENDS_GROUP);
//			jsonObject.put(Constants.TOKEN, application.getToken());
//			jsonObject.put(Constants.JSON, new JSONObject().put("page", page)
//					.toString());
//
//			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
//					NewSourceInfo.class, new AjaxCallBack() {
//
//						@Override
//						public void receive(int code, Object result) {
							setListShown(true);
//							switch (code) {
//							case ResultCode.RESULT_OK:
								List<FriendsGroup> result = new ArrayList<FriendsGroup>();
								FriendsGroup group1 = new FriendsGroup("司机好友", "(3)");
								FriendsGroup group2 = new FriendsGroup("专线好友", "(3)");
								FriendsGroup group3 = new FriendsGroup("物流公司好友", "(3)");
								FriendsGroup group4 = new FriendsGroup("好友申请", "(5)");
								result.add(group1);
								result.add(group2);
								result.add(group3);
								result.add(group4);
 								if (result instanceof List) {
									List<FriendsGroup> mList = (List<FriendsGroup>) result;
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
										android.util.Log.d("ybxiang", "mList=="+mList);
										((FriendsGroupListAdapter) mAdapter).addAll(mList);
										mAdapter.notifyDataSetChanged();
									}
								}
//								break;
//							case ResultCode.RESULT_ERROR:
//								if (result instanceof String)
//									showMsg(result.toString());
//								break;
//							case ResultCode.RESULT_FAILED:
//								if (result instanceof String)
//									showMsg(result.toString());
//								break;
//
//							default:
//								break;
//							}
							if (mAdapter.isEmpty()) {
								setEmptyText("没有找到数据哦");
							}
							state = WAIT;
//						}
//					});
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(context,
				FriendsDetailActivity.class);
		intent.putExtra("friends_type",position);
		startActivity(intent);
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
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (!this.state_idle) {
			return;
		}
		if (firstVisibleItem == 0|| (firstVisibleItem + visibleItemCount) != totalItemCount) {
			return;
		}
		// 如果当前没有加载数据
		if (state != ISREFRESHING && !load_all) {
			getData(++pageIndex);
		}
	}
}
