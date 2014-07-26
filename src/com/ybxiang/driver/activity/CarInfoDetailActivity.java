package com.ybxiang.driver.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;

/**
 * 车源详细界面
 * 
 * @author ybxiang
 * 
 */
public class CarInfoDetailActivity extends BaseActivity{
	private Context mContext;
	private Button mTitleBarBack;
	private Button mTitleBarMore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = CarInfoDetailActivity.this;
		setContentView(R.layout.car_info_detail);
		initViews();
	}

	private void initViews() {
		Intent fromIntent = getIntent();
		if (fromIntent != null) {
			switch (fromIntent.getIntExtra("car_info_id", 0)) {


			default:
				break;
			}
		}
		((TextView)findViewById(R.id.titlebar_id_content)).setText("车源详细");
		// 返回按钮生效
		mTitleBarBack = (Button) findViewById(R.id.titlebar_id_back);
		mTitleBarBack.setOnClickListener(this);
		// 更多按钮隐藏
		mTitleBarMore = (Button) findViewById(R.id.titlebar_id_more);
		mTitleBarMore.setVisibility(View.GONE);
	}

	// 请求指定页数的数据
	private void getData(int page) {
//		// try {
//		state = ISREFRESHING;
//		// final JSONObject jsonObject = new JSONObject();
//		// jsonObject.put(Constants.ACTION, Constants.QUERY_FRIENDS_CHILD);
//		// jsonObject.put(Constants.TOKEN, application.getToken());
//		// jsonObject.put(Constants.JSON, new JSONObject().put("page", page)
//		// .toString());
//		//
//		// ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
//		// NewSourceInfo.class, new AjaxCallBack() {
//		//
//		// @Override
//		// public void receive(int code, Object result) {
//		setListShown(true);
//		// switch (code) {
//		// case ResultCode.RESULT_OK:
//		List<Friends> result = new ArrayList<Friends>();
//		Friends f1 = new Friends("陈宝珠", "1312267222");
//		Friends f2 = new Friends("张学友", "1383774444");
//		Friends f3 = new Friends("刘德华", "1323432432");
//		Friends f4 = new Friends("郭富城", "1333444444");
//		result.add(f1);
//		result.add(f2);
//		result.add(f3);
//		result.add(f4);
//		if (result instanceof List) {
//			List<Friends> mList = (List<Friends>) result;
//			if (mList == null || mList.isEmpty()) {
//				load_all = true;
//				mFootProgress.setVisibility(View.GONE);
//				mFootMsg.setText("已加载全部");
//			} else {
//				if (mList.size() < 10) {
//					load_all = true;
//					mFootProgress.setVisibility(View.GONE);
//					mFootMsg.setText("已加载全部");
//				} else {
//					load_all = false;
//					mFootProgress.setVisibility(View.VISIBLE);
//					mFootMsg.setText(R.string.tips_isloading);
//				}
//				android.util.Log.d("ybxiang", "mList==" + mList);
//				((FriendsDetailListAdapter) mAdapter).addAll(mList);
//				mAdapter.notifyDataSetChanged();
//			}
//		}
//		// break;
//		// case ResultCode.RESULT_ERROR:
//		// if (result instanceof String)
//		// showMsg(result.toString());
//		// break;
//		// case ResultCode.RESULT_FAILED:
//		// if (result instanceof String)
//		// showMsg(result.toString());
//		// break;
//		//
//		// default:
//		// break;
//		// }
//		if (mAdapter.isEmpty()) {
//			setEmptyText("没有找到数据哦");
//		}
//		state = WAIT;
//		// }
//		// });
//		// } catch (JSONException e) {
//		// e.printStackTrace();
//		// }
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}

}
