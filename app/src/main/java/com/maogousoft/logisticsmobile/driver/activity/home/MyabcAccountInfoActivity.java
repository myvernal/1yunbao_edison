package com.maogousoft.logisticsmobile.driver.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.UpdatePwdActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.AbcInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybxiang.driver.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Myabc 我的账户
 * 
 * @author ybxiang
 */
public class MyabcAccountInfoActivity extends BaseActivity {
    private ImageView mPhoto;
    private TextView mName;
    // 个人abc信息
    private AbcInfo mAbcInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myabc_account);
		initViews();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initViews() {
		((TextView) findViewById(R.id.titlebar_id_content)).setText("我的账户");

        mPhoto = (ImageView) findViewById(R.id.account_photo);
        mName = (TextView) findViewById(R.id.myabc_id_name);
	}

	private void initData() {
        getABCInfo();
	}

    // 获取我的abc信息
    private void getABCInfo() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.ACTION, Constants.DRIVER_PROFILE);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, "");
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    AbcInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result != null) {
                                        mAbcInfo = (AbcInfo) result;
                                        mName.setText(mAbcInfo.getName());
                                        ImageLoader.getInstance().displayImage(mAbcInfo.getId_card_photo(), mPhoto, options,
                                                new Utils.MyImageLoadingListener(mContext, mPhoto));
                                    }
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    if (result != null)
                                        showMsg(result.toString());
                                    break;
                                case ResultCode.RESULT_FAILED:
                                    if (result != null)
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
	public void onClick(View v) {
		super.onClick(v);
        switch (v.getId()) {
            case R.id.myabc_id_updatepwd:
                onChangePasswd(v);
                break;
        }
	}

	/**
	 * 修改密码
	 * 
	 * @param view
	 */
	public void onChangePasswd(View view) {
		startActivity(new Intent(mContext, UpdatePwdActivity.class));
	}
}
