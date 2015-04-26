package com.maogousoft.logisticsmobile.driver.activity.info;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseListActivity;
import com.maogousoft.logisticsmobile.driver.activity.MainActivity;
import com.maogousoft.logisticsmobile.driver.adapter.InfoListAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.NoticeInfo;

/**
 * 信息中心
 * 
 * @author lenovo
 */
public class InformationActivity extends BaseListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		getData();
	}

	// 初始化视图
	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_main_search);
		mListView.setDivider(null);
		mListView.setSelector(android.R.color.transparent);
		mAdapter = new InfoListAdapter(mContext);
		setListAdapter(mAdapter);
		setListShown(false);
	}

	// 请求指定页数的数据
	private void getData() {
		try {
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constants.ACTION, Constants.QUERY_MESSAGE);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, new JSONObject().put("page", 1)
					.toString());
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					NoticeInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							setListShown(true);
							switch (code) {
							case ResultCode.RESULT_OK:
								if (result instanceof List) {
									mAdapter.setList((List<NoticeInfo>) result);
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
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		startActivity(new Intent(this, InfoDetailActivity.class).putExtra(
				"info", ((InfoListAdapter) mAdapter).getList().get(position)));
	}

	@Override
	public void onBackPressed() {
		sendBroadcast(new Intent(MainActivity.ACTION_SWITCH_MAINACTIVITY));
	}
}
