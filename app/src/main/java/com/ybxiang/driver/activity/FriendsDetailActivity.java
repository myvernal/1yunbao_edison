package com.ybxiang.driver.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseListActivity;
import com.maogousoft.logisticsmobile.driver.adapter.FriendsDetailListAdapter;
import com.maogousoft.logisticsmobile.driver.model.Friends;
import com.maogousoft.logisticsmobile.driver.utils.MyAlertDialog;

/**
 * 我的好友列表
 * 
 * @author ybxiang
 * 
 */
public class FriendsDetailActivity extends BaseListActivity implements
		OnClickListener, OnScrollListener {
	private Context mContext;
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
		mContext = FriendsDetailActivity.this;
		initViews();
	}

	private void initViews() {
		Intent fromIntent = getIntent();
		if (fromIntent != null) {
			switch (fromIntent.getIntExtra("friends_type", 0)) {
			case 0:
				((TextView) findViewById(R.id.titlebar_id_content))
						.setText("司机好友");
				break;
			case 1:
				((TextView) findViewById(R.id.titlebar_id_content))
						.setText("专线好友");
				break;
			case 2:
				((TextView) findViewById(R.id.titlebar_id_content))
						.setText("物流公司好友");
				break;
			case 3:
				((TextView) findViewById(R.id.titlebar_id_content))
						.setText("好友申请");
				break;

			default:
				break;
			}
		}

		// 返回按钮生效

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

		// 初始化friendsGroup的adapter
		mAdapter = new FriendsDetailListAdapter(mContext);
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
		// jsonObject.put(Constants.ACTION, Constants.QUERY_FRIENDS_CHILD);
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
		List<Friends> result = new ArrayList<Friends>();
		Friends f1 = new Friends("陈宝珠", "1312267222");
		Friends f2 = new Friends("张学友", "1383774444");
		Friends f3 = new Friends("刘德华", "1323432432");
		Friends f4 = new Friends("郭富城", "1333444444");
		result.add(f1);
		result.add(f2);
		result.add(f3);
		result.add(f4);
		if (result instanceof List) {
			List<Friends> mList = (List<Friends>) result;
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
				((FriendsDetailListAdapter) mAdapter).addAll(mList);
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
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setItems(R.array.menu_friends_operation,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								switch (arg1) {
								case 0:
									Toast.makeText(mContext, "查看他的车源",
											Toast.LENGTH_SHORT).show();
									break;
								case 1:
									Toast.makeText(mContext, "发送我的定位",
											Toast.LENGTH_SHORT).show();
									break;

								default:
									Toast.makeText(mContext, "发送我的定位=" + arg1,
											Toast.LENGTH_SHORT).show();
									break;
								}
							}

						});
				builder.create().show();
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
