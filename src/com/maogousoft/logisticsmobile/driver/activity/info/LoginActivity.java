package com.maogousoft.logisticsmobile.driver.activity.info;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.MainActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.UserInfo;
import com.maogousoft.logisticsmobile.driver.utils.CheckUtils;
import com.maogousoft.logisticsmobile.driver.utils.LocHelper;
import com.maogousoft.logisticsmobile.driver.utils.MD5;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录
 *
 * @author lenovo
 */
public class LoginActivity extends BaseActivity {

    private Button mRegister;
    private Button mSubmit;
    private TextView mForget;
    private EditText mUserName;
    private EditText mPassword;
    private CheckBox mAutoLogin;
    private View mEnterNoLogin;
    private InputMethodManager mInputMethodManager;
    private int mUserType = 0; // PR1.1
    private boolean mUserTypeChecked = false; // PR1.1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_login);
        initViews();
        initData();
        // application.writeAutoLogin(true); // PR112 默认自动登陆
    }

    // 初始化视图控件
    private void initViews() {
        mRegister = (Button) findViewById(R.id.register);
        mUserName = (EditText) findViewById(R.id.info_id_login_phone);
        mPassword = (EditText) findViewById(R.id.info_id_login_password);
        mSubmit = (Button) findViewById(R.id.info_id_login_loginbtn);
        mForget = (TextView) findViewById(R.id.info_id_login_forget);
        mAutoLogin = (CheckBox) findViewById(R.id.info_id_login_autologin);
        mEnterNoLogin = findViewById(R.id.no_login_enter);
        mForget.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mForget.getPaint().setAntiAlias(true);

        mRegister.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        mForget.setOnClickListener(this);
        mEnterNoLogin.setOnClickListener(this);
        mUserName.setOnFocusChangeListener(mOnFocusChangeListener);
        mPassword.setOnFocusChangeListener(mOnFocusChangeListener);
        mAutoLogin.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    // 初始化数据
    private void initData() {
        final String userName = application.getUserName();
        final String password = application.getPassword();
        if (!TextUtils.isEmpty(userName)) {
            mUserName.setText(userName);
        }
        if (!TextUtils.isEmpty(password)) {
            mPassword.setText(password);
        }
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    // checkbox选中事件
    private final OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            application.writeAutoLogin(isChecked);
        }
    };

    // edittext失去焦点事件
    private final OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v == mUserName && !hasFocus) {
                if (TextUtils.isEmpty(mUserName.getText().toString())) {
                    showMsg(R.string.tips_login_phone);
                    return;
                }
            } else if (v == mPassword && !hasFocus) {
                if (TextUtils.isEmpty(mPassword.getText())) {
                    showMsg(R.string.tips_login_password);
                    return;
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.register:
                if (!mUserTypeChecked) {
                    showMsg(R.string.user_type_not_choose);
                    return;
                }
                switch (mUserType) {
                    // 司机
                    case Constants.USER_DRIVER:
                        startActivity(new Intent(context, RegisterActivity.class));
                        break;
                    // 货主
                    case Constants.USER_SHIPPER:
                        startActivity(new Intent(context, RegisterShipperActivity.class));
                        break;
                    default:
                        break;
                }
                break;
            case R.id.info_id_login_loginbtn:
                if (!CheckUtils.checkIsEmpty(mUserName)) {
                    showMsg(R.string.tips_login_phone);
                    return;
                }
                if (!CheckUtils.checkIsEmpty(mPassword)) {
                    showMsg(R.string.tips_login_password);
                    return;
                }
                // PR1.1
                if (!mUserTypeChecked) {
                    showMsg(R.string.user_type_not_choose);
                    return;
                }
                // PR1.1
                String loginType = Constants.DRIVER_LOGIN;
                switch (mUserType) {
                    // 司机
                    case Constants.USER_DRIVER:
                        loginType = Constants.DRIVER_LOGIN;
                        break;
                    // 货主
                    case Constants.USER_SHIPPER:
                        loginType = Constants.USER_LOGIN;
                        break;
                    default:
                        break;
                }
                submit(loginType);
                break;
            case R.id.info_id_login_forget:
                startActivity(new Intent(context, ForgetActivity.class));
                break;
            case R.id.no_login_enter:
                if (!mUserTypeChecked) {
                    showMsg(R.string.user_type_not_choose);
                    return;
                }
                application.setIsAnonymous(true);
                startActivity(new Intent(context, MainActivity.class));
                break;
            default:
                break;
        }
    }

    // 登录
    private void submit(String loginType) {
        mInputMethodManager.hideSoftInputFromWindow(mPassword.getWindowToken(),
                0);
        final JSONObject jsonObject = new JSONObject();
        try {
            showProgress("正在登录...");
            jsonObject.put(Constants.ACTION, loginType);
            jsonObject.put(Constants.TOKEN, null);
            jsonObject.put(Constants.JSON,new JSONObject()
                            .put("phone", mUserName.getText().toString())
                            .put("password", MD5.encode(mPassword.getText().toString()))
                            .put("device_type", Constants.DEVICE_TYPE)
                            .toString());
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    UserInfo.class, new AjaxCallBack() {

                @Override
                public void receive(int code, Object result) {
                    dismissProgress();
                    switch (code) {
                        case ResultCode.RESULT_OK:
                            UserInfo userInfo = (UserInfo) result;
                            // 写入用户信息
                            application.writeUserInfo(mUserName.getText()
                                    .toString(), mPassword.getText()
                                    .toString(), userInfo.getDriver_id());
                            application.setToken(userInfo.getToken());
                            application.writeInfo("name",userInfo.getName());
                            application.startXMPPService();
                            startActivity(new Intent(context, MainActivity.class));
                            // startActivity(new Intent(context,
                            // SearchSourceActivity.class));
                            finish();

                            LocHelper.addAlarm(LoginActivity.this,
                                    System.currentTimeMillis(),
                                    Constants.LOC_UPDATE_TIME); // 每隔一个小时上报一次位置

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

    // PR1.1 begin
    // 选择身份登录
    public void onUserTypeClicked(View view) {
        // 用户是否选择了身份
        mUserTypeChecked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            // 司机版
            //16899669966,999666
            case R.id.driver:
                if (mUserTypeChecked)
                    mUserType = Constants.USER_DRIVER;
                application.writeUserType(Constants.USER_DRIVER);
                break;
            // 货主版
            //16888668866,888666
            case R.id.shipper:
                if (mUserTypeChecked)
                    mUserType = Constants.USER_SHIPPER;
                application.writeUserType(Constants.USER_SHIPPER);
                break;
            //无忧运力 18081089935,555888
            //物流QQ 13683427216,427216
        }
    }
    // PR1.1 end
}
