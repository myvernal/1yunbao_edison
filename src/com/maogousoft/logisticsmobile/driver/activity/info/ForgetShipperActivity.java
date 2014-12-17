package com.maogousoft.logisticsmobile.driver.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.share.ShareActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 忘记货主密码
 * Created by EdisonZhao on 14/12/16.
 * Email:zhaoliangyu@sobey.com
 */
public class ForgetShipperActivity extends BaseActivity {

    private EditText companyPhone, companyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_forget_shipper);
        initViews();
        setIsShowAnonymousActivity(false);
    }

    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_info_forget_title);
        companyPhone = (EditText) findViewById(R.id.forget_company_phone);
        companyName = (EditText) findViewById(R.id.forget_company_name);
        Button mBack = (Button) findViewById(R.id.titlebar_id_back);
        Button mSubmit = (Button) findViewById(R.id.forget_submitbtn);
        Button mShare = (Button) findViewById(R.id.titlebar_id_more);

        mBack.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.forget_submitbtn:
                submit();
                break;
            case R.id.titlebar_id_back:
                finish();
                break;
            case R.id.titlebar_id_more:
                startActivity(new Intent(context, ShareActivity.class));
                break;
            default:
                break;
        }
    }

    private void submit() {
        if (!checkInvalid()) {
            return;
        }
        try {
            showSpecialProgress();
            JSONObject json = new JSONObject();
            json.put("phone", companyPhone.getText().toString().trim());
            json.put("company", companyName.getText().toString().trim());
            JSONObject params = new JSONObject();
            params.put(Constants.ACTION, Constants.SHIPPER_FORGET_PASSWORD);
            params.put(Constants.JSON, json.toString());
            ApiClient.doWithObject(Constants.COMMON_SERVER_URL, params, null, new AjaxCallBack() {

                @Override
                public void receive(int code, Object result) {
                    dismissProgress();
                    switch (code) {
                        case ResultCode.RESULT_OK:
                            showMsg("密码将以短信形式发送到您的注册手机,请注意查收");
                            finish();
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

    private boolean checkInvalid() {
        if (TextUtils.isEmpty(companyPhone.getText())) {
            showMsg("请输入注册手机号");
            companyPhone.requestFocus();
            return false;
        }
        if (!CheckUtils.checkPhone(companyPhone.getText().toString().trim())) {
            showMsg("手机号码格式不正确");
            companyPhone.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(companyName.getText())) {
            showMsg("请输入注册公司名");
            companyName.requestFocus();
            return false;
        }
        return true;
    }
}
