package com.maogousoft.logisticsmobile.driver.activity.info;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseListActivity;
import com.maogousoft.logisticsmobile.driver.adapter.AccountRecordAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.AccountRecordInfo;

/**
 * 账户记录
 * 
 * @author admin
 * 
 */
public class AccountRecordActivity extends BaseListActivity implements OnScrollListener {

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
		initViews();
	}

	private void initViews() {

		((TextView) findViewById(R.id.titlebar_id_content)).setText("账户记录");
		mFootView = getLayoutInflater().inflate(R.layout.listview_footview, null);
		mFootView.setClickable(false);
		mFootProgress = (ProgressBar) mFootView.findViewById(android.R.id.progress);
		mFootMsg = (TextView) mFootView.findViewById(android.R.id.text1);
		mListView.addFooterView(mFootView);

		mListView.setOnScrollListener(this);

		mAdapter = new AccountRecordAdapter(mContext);
		setListAdapter(mAdapter);
		setListShown(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mAdapter.isEmpty()) {
			pageIndex = 1;
			getData(pageIndex);
		}
	}

	// 请求指定页数的数据
	private void getData(int page) {
		try {
			state = ISREFRESHING;
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constants.ACTION, Constants.COMMON_GET_BUSINESS);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, new JSONObject().put("page", page).toString());
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject, AccountRecordInfo.class, new AjaxCallBack() {

				@Override
				public void receive(int code, Object result) {
					setListShown(true);
					switch (code) {
						case ResultCode.RESULT_OK:
							if (result instanceof List) {
								List<AccountRecordInfo> mList = (List<AccountRecordInfo>) result;
								if (mList == null || mList.isEmpty()) {
									load_all = true;
									mFootProgress.setVisibility(View.GONE);
									mFootMsg.setText("已加载全部");
								} else {
									if (mList.size() < 10) {
										load_all = true;
										mFootProgress.setVisibility(View.GONE);
										mFootMsg.setText("已加载全部");
									}
                                    mAdapter.addAll(mList);
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

}
