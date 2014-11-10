package com.maogousoft.logisticsmobile.driver.activity.info;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.maogousoft.logisticsmobile.driver.model.DriverInfo;
import com.maogousoft.logisticsmobile.driver.model.ShipperInfo;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aliang on 2014/11/2.
 */
public class SettingPayPasswordActivity extends BaseActivity {
    private EditText mDefaultPwd, mDefaultPwd2;
    private EditText mOldPwd, mNewPwd, mNewPwd2;
    private View settingLayout, changeLayout;
    private Button mSubmit;
    private boolean isSetDefaultPwd = true;
    private boolean isNeedReturn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_pay_password);
        initViews();
        initData();
    }

    // 初始化视图
    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_update_pay_title);
        mDefaultPwd = (EditText) findViewById(R.id.default_pay_password);
        mDefaultPwd2 = (EditText) findViewById(R.id.confirm_default_pay_password);
        settingLayout = findViewById(R.id.pay_password_setting_layout);
        mOldPwd = (EditText) findViewById(R.id.info_id_update_oldpwd);
        mNewPwd = (EditText) findViewById(R.id.info_id_update_newpwd);
        mNewPwd2 = (EditText) findViewById(R.id.info_id_update_newpwd2);
        changeLayout = findViewById(R.id.pay_password_change_layout);

        mDefaultPwd.setOnFocusChangeListener(mDefaultOnFocusChangeListener);
        mDefaultPwd2.setOnFocusChangeListener(mDefaultOnFocusChangeListener);
        mOldPwd.setOnFocusChangeListener(mOnFocusChangeListener);
        mNewPwd.setOnFocusChangeListener(mOnFocusChangeListener);
        mNewPwd2.setOnFocusChangeListener(mOnFocusChangeListener);

        mSubmit = (Button) findViewById(R.id.confirm);
        mSubmit.setOnClickListener(this);
    }

    private void initData() {
        isNeedReturn = getIntent().getBooleanExtra(Constants.COMMON_KEY, false);
        getAbcInfo();
    }

    // 获取我的abc信息
    private void getAbcInfo() {
        final JSONObject jsonObject = new JSONObject();
        try {
            showDefaultProgress();
            if (application.getUserType() == Constants.USER_DRIVER) {
                jsonObject.put(Constants.ACTION, Constants.GET_DRIVER_INFO);
            } else {
                jsonObject.put(Constants.ACTION, Constants.GET_USER_INFO);
            }
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("user_id", application.getUserId()));
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    application.getUserType() == Constants.USER_DRIVER ? DriverInfo.class : ShipperInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result != null) {
                                        if (result instanceof ShipperInfo) {
                                            ShipperInfo userInfo = (ShipperInfo) result;
                                            if (TextUtils.isEmpty(userInfo.getYunbao_pay())) {
                                                //支付密码为空,显示设置密码页面
                                                settingLayout.setVisibility(View.VISIBLE);
                                            } else {
                                                // 显示修改密码页面
                                                changeLayout.setVisibility(View.VISIBLE);
                                                isSetDefaultPwd = false;
                                            }
                                        } else if (result instanceof DriverInfo) {
                                            DriverInfo userInfo = (DriverInfo) result;
                                            if (TextUtils.isEmpty(userInfo.getYunbao_pay())) {
                                                //支付密码为空,显示设置密码页面
                                                settingLayout.setVisibility(View.VISIBLE);
                                            } else {
                                                // 显示修改密码页面
                                                changeLayout.setVisibility(View.VISIBLE);
                                                isSetDefaultPwd = false;
                                            }
                                        }
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

    private final View.OnFocusChangeListener mDefaultOnFocusChangeListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v == mDefaultPwd && !hasFocus) {
                if (!CheckUtils.checkIsEmpty(mDefaultPwd)) {
                    showMsg(R.string.tips_updatepwd_oldpwd);
                }
            } else if (v == mDefaultPwd2 && !hasFocus) {
                if (!CheckUtils.checkIsEmpty(mDefaultPwd2)) {
                    showMsg(R.string.tips_updatepwd_newpwd2);
                }
            }
        }
    };

    private final View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v == mOldPwd && !hasFocus) {
                if (!CheckUtils.checkIsEmpty(mOldPwd)) {
                    showMsg(R.string.tips_updatepwd_oldpwd);
                }
            } else if (v == mNewPwd && !hasFocus) {
                if (!CheckUtils.checkIsEmpty(mNewPwd)) {
                    showMsg(R.string.tips_updatepwd_newpwd);
                }
            } else if (v == mNewPwd2 && !hasFocus) {
                if (!CheckUtils.checkIsEmpty(mNewPwd2)) {
                    showMsg(R.string.tips_updatepwd_newpwd2);
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == mSubmit) {
            if (isSetDefaultPwd)
                if (mDefaultPwd.length() < 6) {
                    showMsg(R.string.tips_pwd_length_error);
                    return;
                }
            if (!TextUtils.equals(mDefaultPwd.getText(), mDefaultPwd2.getText())) {
                showMsg(R.string.tips_updatepwd_newpwd2);
                return;
            }
        } else {
            if (mOldPwd.length() < 6 || mNewPwd.length() < 6) {
                showMsg(R.string.tips_pwd_length_error);
                return;
            }
            if (!CheckUtils.checkIsEmpty(mOldPwd)) {
                showMsg(R.string.tips_updatepwd_oldpwd);
                return;
            }
            if (!CheckUtils.checkIsEmpty(mNewPwd)) {
                showMsg(R.string.tips_updatepwd_newpwd);
                return;
            }
            if (!CheckUtils.checkIsEmpty(mNewPwd2)) {
                showMsg(R.string.tips_updatepwd_newpwd2);
                return;
            }
            if (!mNewPwd.getText().toString().equals(mNewPwd2.getText().toString())) {
                showMsg(R.string.tips_updatepwd_newpwd2);
                return;
            }
        }
        submit();
    }

    //提交修改支付密码的请求
    private void submit() {
        final JSONObject jsonObject = new JSONObject();
        try {
            showProgress("正在操作,请稍后...");
            jsonObject.put(Constants.ACTION, Constants.CHANGE_PAY_PASSWORD);
            jsonObject.put(Constants.TOKEN, application.getToken());
            JSONObject params = new JSONObject();
            if (isSetDefaultPwd) {
                //params.put("new_pay_password", MD5.encode(mDefaultPwd.getText().toString()));
                params.put("new_pay_password", mDefaultPwd.getText().toString());
            } else {
                params.put("old_pay_password", mOldPwd.getText().toString());
                params.put("new_pay_password", mNewPwd.getText().toString());
            }
            jsonObject.put(Constants.JSON, params.toString());
            ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject, null, new AjaxCallBack() {

                @Override
                public void receive(int code, Object result) {
                    dismissProgress();
                    switch (code) {
                        case ResultCode.RESULT_OK:
                            application.writeInfo(Constants.XMPP_PASSWORD, mNewPwd2.getText().toString());
                            showMsg(R.string.tips_updatepwd_success);
                            if (isNeedReturn) {
                                finish();
                            }
                            break;
                        case ResultCode.RESULT_ERROR:
                            if (result instanceof String)
                                showMsg(result.toString());
                            else
                                showMsg(R.string.tips_updatepwd_failed);
                            break;
                        case ResultCode.RESULT_FAILED:
                            if (result instanceof String)
                                showMsg(result.toString());
                            else
                                showMsg(R.string.tips_updatepwd_failed);
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
