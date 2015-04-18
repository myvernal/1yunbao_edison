package com.maogousoft.logisticsmobile.driver.activity.home;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseListActivity;
import com.maogousoft.logisticsmobile.driver.adapter.UnMatchedSourceListAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;

/**
 * 待定货源
 * 
 * @author lenovo
 */
public class UnMatchedSourceActivity extends BaseListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
	}

	// 初始化视图
	private void initViews() {

		((TextView) findViewById(R.id.titlebar_id_content))
				.setText(R.string.string_home_unmatched_title);

		mAdapter = new UnMatchedSourceListAdapter(context);
		setListAdapter(mAdapter);
		setListShown(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mAdapter.isEmpty()) {
			getData(1);
		}
	}

	// 请求指定页数的数据
	private void getData(int page) {
		try {
			final JSONObject jsonObject = new JSONObject();
			jsonObject.put(Constants.ACTION,
					Constants.QUERY_PENDING_SOURCE_ORDER);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, new JSONObject().put("page", page)
					.toString());
			ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
					NewSourceInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
							setListShown(true);
							switch (code) {
							case ResultCode.RESULT_OK:
								if (result instanceof List) {
									mAdapter.setList((List<NewSourceInfo>) result);
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
		final Intent intent = new Intent(context, SourceDetailActivity.class);
		intent.putExtra(SourceDetailActivity.ORDER_ID,
				((UnMatchedSourceListAdapter) mAdapter).getList().get(position)
						.getId());
		intent.putExtra("type", "UnMatchedSourceActivity");
		startActivity(intent);
	}
}
