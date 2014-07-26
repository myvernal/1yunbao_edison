package com.ybxiang.driver.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseListActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.CarInfoActivity;
import com.maogousoft.logisticsmobile.driver.adapter.CarInfoListAdapter;
import com.maogousoft.logisticsmobile.driver.adapter.FocusLineInfoListAdapter;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;
import com.maogousoft.logisticsmobile.driver.model.FocuseLineInfo;
import com.ybxiang.driver.model.FocusLineInfo;
/**
 * 已关注路线的Adapter
 * @author ybxiang
 *
 */
public class FocusLineInfoActivity extends BaseListActivity implements
		OnClickListener, OnScrollListener {
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
		mContext = FocusLineInfoActivity.this;
		initViews();
	}

	private void initViews() {

		((TextView) findViewById(R.id.titlebar_id_content)).setText("我的车源");
		// 返回按钮生效
		mTitleBarBack = (Button) findViewById(R.id.titlebar_id_back);
		mTitleBarBack.setOnClickListener(this);
		// 更多按钮隐藏
		mTitleBarMore = (Button) findViewById(R.id.titlebar_id_more);
		mTitleBarMore.setVisibility(View.GONE);

		// 数据加载中进度条
		mFootView = getLayoutInflater().inflate(R.layout.listview_footview,
				null);
		mFootView.setClickable(false);
		mFootProgress = (ProgressBar) mFootView
				.findViewById(android.R.id.progress);
		mFootMsg = (TextView) mFootView.findViewById(android.R.id.text1);
		mListView.addFooterView(mFootView);

		// 初始化FocusLineInfoListAdapter的adapter
		mAdapter = new FocusLineInfoListAdapter(mContext);
		setListAdapter(mAdapter);
		// list未加载数据不显示
		setListShown(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		android.util.Log.d("ybxiang",
				"mAdapter.isEmpty()==" + mAdapter.isEmpty());
		if (mAdapter.isEmpty()) {
			pageIndex = 1;
			getData(pageIndex);
		}
	}

	// 请求指定页数的数据
	private void getData(int page) {
		// try {
		state = ISREFRESHING;
		// final JSONObject jsonObject = new JSONObject();
		// jsonObject.put(Constants.ACTION, Constants.QUERY_FRIENDS_GROUP);
		// jsonObject.put(Constants.TOKEN, application.getToken());
		// jsonObject.put(Constants.JSON, new JSONObject().put("page", page)
		// .toString());
		//
		// ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
		// NewSourceInfo.class, new AjaxCallBack() {
		//
		// @Override
		// public void receive(int code, Object result) {
		setListShown(true);
		// switch (code) {
		// case ResultCode.RESULT_OK:
		List<FocuseLineInfo> result = new ArrayList<FocuseLineInfo>();
		FocuseLineInfo focus1 = new FocuseLineInfo(111111,22222);
		FocuseLineInfo focus2 = new FocuseLineInfo(333333,22222);
		FocuseLineInfo focus3 = new FocuseLineInfo(444444,22222);
		FocuseLineInfo focus4 = new FocuseLineInfo(555555,22222);
		result.add(focus1);
		result.add(focus2);
		result.add(focus3);
		result.add(focus4);
		if (result instanceof List) {
			List<FocuseLineInfo> mList = (List<FocuseLineInfo>) result;
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
				android.util.Log.d("ybxiang", "mList==" + mList);
				((FocusLineInfoListAdapter) mAdapter).addAll(mList);
				mAdapter.notifyDataSetChanged();
			}
		}
		// break;
		// case ResultCode.RESULT_ERROR:
		// if (result instanceof String)
		// showMsg(result.toString());
		// break;
		// case ResultCode.RESULT_FAILED:
		// if (result instanceof String)
		// showMsg(result.toString());
		// break;
		//
		// default:
		// break;
		// }
		if (mAdapter.isEmpty()) {
			setEmptyText("没有找到数据哦");
		}
		state = WAIT;
		// }
		// });
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(context, CarInfoDetailActivity.class);
		intent.putExtra("car_info_id", position);
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
		if (firstVisibleItem == 0
				|| (firstVisibleItem + visibleItemCount) != totalItemCount) {
			return;
		}
		// 如果当前没有加载数据
		if (state != ISREFRESHING && !load_all) {
			getData(++pageIndex);
		}
	}
}
