package com.maogousoft.logisticsmobile.driver.activity.vip;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.EvaluateInfo;
import com.maogousoft.logisticsmobile.driver.model.ShopInfo;

/**
 * 评价
 * 
 * @version 1.0.0
 * @author wst
 * @date 2013-5-18 下午9:54:45
 */

public class EvaluateActivity extends BaseActivity {

	private Button mBack;
	private ShopInfo shopInfo;

	private RatingBar ratingBar, ratingBar1, ratingBar2, ratingBar3;
	private EditText edtRemark;
	private CheckBox checkboxIstrue;
	private Button btnSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		initListener();
		initData();

	}

	public void initView() {
		setContentView(R.layout.activity_vip_evaluate);
		mBack = (Button) findViewById(R.id.titlebar_id_back);
		((TextView) findViewById(R.id.titlebar_id_content)).setText("点评");
		
		

		ratingBar = (RatingBar) findViewById(R.id.evaluate_id_score);
		ratingBar.setIsIndicator(true);

		ratingBar1 = (RatingBar) findViewById(R.id.evaluate_id_score_true);
		ratingBar2 = (RatingBar) findViewById(R.id.evaluate_id_score_integrity);
		ratingBar3 = (RatingBar) findViewById(R.id.evaluate_id_score_timely);
		
		ratingBar1.setRating(5);
		ratingBar2.setRating(5);
		ratingBar3.setRating(5);

		edtRemark = (EditText) findViewById(R.id.evaluate_id_remark);

		checkboxIstrue = (CheckBox) findViewById(R.id.checkbox_istrue);

		btnSubmit = (Button) findViewById(R.id.evaluate_id_submit);

	}

	public void initListener() {
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				submitData();
			}
		});

	}

	public void initData() {

		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			shopInfo = (ShopInfo) bundle.getSerializable("ShopInfo");
			if (shopInfo == null) {
				return;
			}
		} else {
			return;
		}

		double score = shopInfo.getScore();

		if (score == 0) {
			score = 5;
		}

		ratingBar.setRating((float) score);

	}

	private void submitData() {
		showDefaultProgress();
		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(Constants.ACTION, Constants.ADD_VENDER_REPLY);
			jsonObject.put(Constants.TOKEN, application.getToken());

			// vender_id int 是 商户编号
			// score1 int 是 评分1
			// score2 int 是 评分2
			// score3 int 是 评分3
			// reply_content String 评价内容
			// is_true int 是 是否如实 0：否，1：是

			int is_true = 0;
			if (checkboxIstrue.isChecked()) {
				is_true = 1;
			}

			String reply_content = "";
			if (!TextUtils.isEmpty(edtRemark.getText().toString())) {
				reply_content = edtRemark.getText().toString();
			}

			jsonObject.put(
					Constants.JSON,
					new JSONObject().put("vender_id", shopInfo.getId()).put("score1", ratingBar1.getRating())
							.put("score2", ratingBar2.getRating()).put("score3", ratingBar3.getRating())
							.put("is_true", is_true).put("reply_content", reply_content).toString());
			ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject, EvaluateInfo.class, new AjaxCallBack() {

				@Override
				public void receive(int code, Object result) {
					dismissProgress();
					switch (code) {
						case ResultCode.RESULT_OK:
							showMsg("评价成功！");
							finish();
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
				}
			});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
