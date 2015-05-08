package com.maogousoft.logisticsmobile.driver.activity.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.BaseListActivity;
import com.maogousoft.logisticsmobile.driver.adapter.EvaluateListAdapter;
import com.maogousoft.logisticsmobile.driver.adapter.NewSourceListAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.EvaluateInfo;
import com.maogousoft.logisticsmobile.driver.model.HuoZhuUserInfo;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;

// 货主信誉
public class UserCreditActivity extends BaseListActivity implements AbsListView.OnScrollListener {

	private RatingBar mScore, credit_score1, credit_score2, credit_score3;
	private int user_id;
	private String user_phone;
	private float user_score;
	private TextView tvName, tvCompanyName, tvAddr, tvPhone, tvAccountName;
	private HuoZhuUserInfo hzui = new HuoZhuUserInfo();
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
		//setContentView(R.layout.activity_home_user_credit);
		initViews();
		initData();
		queryData();
	}

	// 初始化视图
	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content)).setText("货主信誉");

        View view = LayoutInflater.from(mContext).inflate(R.layout.view_credit_header_view, null);
        mListView.addHeaderView(view);
        mAdapter = new EvaluateListAdapter(mContext);
        mListView.setOnScrollListener(this);
        mListView.setAdapter(mAdapter);
        setListShown(false);

		mScore = (RatingBar) findViewById(R.id.credit_score);
		credit_score1 = (RatingBar) findViewById(R.id.credit_score1);
		credit_score2 = (RatingBar) findViewById(R.id.credit_score2);
		credit_score3 = (RatingBar) findViewById(R.id.credit_score3);
		mScore.setIsIndicator(true);
		credit_score1.setIsIndicator(true);
		credit_score2.setIsIndicator(true);
		credit_score3.setIsIndicator(true);

		tvName = (TextView) findViewById(R.id.tv_name);
		tvCompanyName = (TextView) findViewById(R.id.tv_company_name);
		tvAddr = (TextView) findViewById(R.id.tv_addr);
		tvPhone = (TextView) findViewById(R.id.tv_phone);
		tvAccountName = (TextView) findViewById(R.id.tv_account_name);
	}

	// 初始化数据
	private void initData() {
		user_id = getIntent().getIntExtra("user_id", 0);
		user_phone = getIntent().getStringExtra("user_phone");
		user_score = getIntent().getFloatExtra("user_score", 0);
		if (user_score == 0) {
			user_score = 5;
		}
		mScore.setRating(user_score);

		float user_score1 = getIntent().getIntExtra("user_score1", 0);
		if (user_score1 == 0) {
			user_score1 = 5;
		}
		credit_score1.setRating(user_score1);

		float user_score2 = getIntent().getIntExtra("user_score2", 0);
		if (user_score2 == 0) {
			user_score2 = 5;
		}
		credit_score2.setRating(user_score2);

		float user_score3 = getIntent().getIntExtra("user_score3", 0);
		if (user_score3 == 0) {
			user_score3 = 5;
		}
		credit_score3.setRating(user_score3);
	}

	// 获取评论列表
	private void queryData() {
		final JSONObject jsonObject = new JSONObject();
		try {
            state = ISREFRESHING;
			jsonObject.put(Constants.ACTION, Constants.GET_USER_REPLY);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, new JSONObject().put("user_id", user_id).toString());
			ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject,
					EvaluateInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
                            setListShown(true);
                            mListView.setVisibility(View.VISIBLE);
							switch (code) {
							case ResultCode.RESULT_OK:
                                if(result instanceof ArrayList) {
                                    List<EvaluateInfo> list = (List<EvaluateInfo>) result;
                                    mAdapter.addAll(list);
                                    if(list.size() < 10) {
                                        load_all = true;
                                    }
                                }
								break;
							}
                            state = WAIT;
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		}
        //获取货主信誉信息
		final JSONObject jsonObject1 = new JSONObject();
		try {
			jsonObject1.put(Constants.ACTION, Constants.GET_USER_INFO);
			jsonObject1.put(Constants.TOKEN, application.getToken());
			jsonObject1.put(Constants.JSON, new JSONObject().put("user_id", user_id).toString());
			ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject1,
					HuoZhuUserInfo.class, new AjaxCallBack() {

						@Override
						public void receive(int code, Object result) {
                            setListShown(true);
							switch (code) {
							case ResultCode.RESULT_OK:
								hzui = (HuoZhuUserInfo) result;
								tvName.setText(tvName.getText() + hzui.getName());
								tvCompanyName.setText(tvCompanyName.getText() + hzui.getCompany_name());
								tvAddr.setText(tvAddr.getText() + hzui.getAddress());
								tvPhone.setText(tvPhone.getText() + hzui.getPhone());
								break;
							case ResultCode.RESULT_ERROR:
								showMsg(result.toString());
								break;
							case ResultCode.RESULT_FAILED:
								showMsg(result.toString());
								break;

							default:
								break;
							}
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
