package com.maogousoft.logisticsmobile.driver.activity.other;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;

/***
 * 意见反馈
 * 
 * @author lenovo
 */
public class FeedBackActivity extends BaseActivity {

	private EditText mContent;
	private Button mSubmit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_others_feedback);
		initViews();
	}
	
	private void initViews(){
		findViewById(R.id.titlebar_id_back).setOnClickListener(this);
		((TextView)findViewById(R.id.titlebar_id_content)).setText(R.string.string_others_feedback_title);
		mContent=(EditText)findViewById(android.R.id.edit);
		mSubmit=(Button)findViewById(android.R.id.button1);
		
		mSubmit.setOnClickListener(this);
	}
	
	//提交意见
	private void submit(){
		if (!CheckUtils.checkIsEmpty(mContent)) {
			showMsg(R.string.tips_feedback_empty);
			return;
		}
		try {
			showProgress("正在提交,请稍候");
			JSONObject jsonObject=new JSONObject();
			jsonObject.put(Constants.ACTION, Constants.POST_FEEDBACK);
			jsonObject.put(Constants.TOKEN, application.getToken());
			jsonObject.put(Constants.JSON, new JSONObject().put("phone", application.getUserName()).put("suggest_content", mContent.getText().toString()).toString());
			ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject, null, new AjaxCallBack() {
				
				@Override
				public void receive(int code, Object result) {
					dismissProgress();
					switch (code) {
					case ResultCode.RESULT_OK:
						showMsg(R.string.tips_feedback_success);
						finish();
						break;
					case ResultCode.RESULT_ERROR:
						showMsg(R.string.tips_feedback_failed);
						break;
					case ResultCode.RESULT_FAILED:
						showMsg(R.string.tips_feedback_failed);
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
	public void onClick(View v) {
		super.onClick(v);
		if (v==mSubmit) {
			submit();
		}
	}
}
