package com.ybxiang.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.ChargeActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.HuoZhuUserInfo;
import com.maogousoft.logisticsmobile.driver.model.SafeSeaInfo;
import com.ybxiang.driver.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aliang on 2014/8/10.
 */
public class SafeSeaActivity extends BaseActivity {

    private Button mTitleBarBack, mTitleBarMore;
    private Spinner safe_type_spinner;
    private TextView safe_type_desc;
    private EditText safe_percent, safe_money, safe_all_money;
    private TextView user_money;
    private CheckBox safe_check_box;
    private double userGold = -1;
    private double ratio = 0.0f;
    private HuoZhuUserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safe_sea_layout);
        initView();
        initData();
    }

    private void initView() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("太平洋保险");
        // 返回按钮生效
        mTitleBarBack = (Button) findViewById(R.id.titlebar_id_back);
        mTitleBarBack.setOnClickListener(this);
        // 更多按钮隐藏
        mTitleBarMore = (Button) findViewById(R.id.titlebar_id_more);
        mTitleBarMore.setText("保险记录");
        mTitleBarMore.setOnClickListener(this);

        safe_check_box = (CheckBox) findViewById(R.id.safe_check_box);
        user_money = (TextView) findViewById(R.id.user_money);
        safe_all_money = (EditText) findViewById(R.id.safe_all_money);
        safe_money = (EditText) findViewById(R.id.safe_money);
        safe_percent = (EditText) findViewById(R.id.safe_percent);
        safe_type_desc = (TextView) findViewById(R.id.safe_type_desc);
        safe_type_spinner = (Spinner) findViewById(R.id.safe_type_spinner);
        //根据险种不同,保险说明也不同,费率也不同
        safe_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                changeRatioBySpinnerSelect(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        //保费由费率和保额相乘
        safe_all_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > 0) {
                    double allMoney = (Float.valueOf(safe_all_money.getText().toString()) * 10000) * ratio / 100;
                    safe_money.setText(allMoney + "");
                } else {
                    safe_money.setText("0");
                }
            }
        });
    }

    private void initData() {
        getUserInfo();
        getBalance();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.titlebar_id_more:
                startActivity(new Intent(context, SafeListActivity.class));
                break;
        }
    }

    /**
     * 根据险种使用不同的费率
     * @param i
     */
    private void changeRatioBySpinnerSelect(int i) {
        if(userInfo != null) {
            switch (i) {
                case 0:
                    ratio = userInfo.getTpy_1();
                    safe_type_desc.setText(getString(R.string.safe_text_one_1, ratio + "%"));
                    break;
                case 1:
                    ratio = userInfo.getTpy_2();
                    safe_type_desc.setText(getString(R.string.safe_text_one_1, ratio + "%"));
                    break;
                case 2:
                    ratio = userInfo.getTpy_3();
                    safe_type_desc.setText(getString(R.string.safe_text_one_1, ratio + "%"));
                    break;
                case 3:
                    ratio = userInfo.getTpy_4();
                    safe_type_desc.setText(getString(R.string.safe_text_one_2, ratio + "%"));
                    break;
                case 4:
                    ratio = userInfo.getTpy_5();
                    safe_type_desc.setText(getString(R.string.safe_text_one_2, ratio + "%"));
                    break;
                case 5:
                    ratio = userInfo.getTpy_6();
                    safe_type_desc.setText(getString(R.string.safe_text_one_3, ratio + "%"));
                    break;
            }
            safe_percent.setText(ratio + "");
            if(!TextUtils.isEmpty(safe_all_money.getText())) {
                double allMoney = (Float.valueOf(safe_all_money.getText().toString()) * 10000) * ratio / 100;
                safe_money.setText(allMoney + "");
            }
        }
    }

    // 获取账户余额
    private void getBalance() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.ACTION, Constants.GET_ACCOUNT_GOLD);
            jsonObject.put(Constants.TOKEN, application.getToken());
            ApiClient.doWithObject(Constants.COMMON_SERVER_URL, jsonObject,
                    null, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    JSONObject object = (JSONObject) result;
                                    userGold = object.optDouble("gold");
                                    String gold = String.format(getString(R.string.safe_user_money), userGold);
                                    user_money.setText(Html.fromHtml(Utils.textFormatRed(gold)));
                                    break;
                                case ResultCode.RESULT_FAILED:
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    break;

                                default:
                                    break;
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取用户信息中的费率
    private void getUserInfo() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.ACTION, Constants.GET_USER_INFO);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("user_id", application.getUserId()));
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    HuoZhuUserInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result != null) {
                                        userInfo = (HuoZhuUserInfo) result;
                                        //选择对应的费率
                                        changeRatioBySpinnerSelect(safe_type_spinner.getSelectedItemPosition());
                                        if(ratio > 0) {
                                            if(!TextUtils.isEmpty(safe_all_money.getText())) {
                                                double allMoney = (Float.valueOf(safe_all_money.getText().toString()) * 10000) * ratio / 100;
                                                safe_money.setText(allMoney + "");
                                            }
                                        } else {
                                            Toast.makeText(context, "获取保险费率出错,请联系客服...", Toast.LENGTH_SHORT).show();
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

    /**
     * 帐号充值
     *
     * @param view
     */
    public void onCharge(View view) {
        startActivity(new Intent(context, ChargeActivity.class));
    }

    /**
     * 返回
     *
     * @param view
     */
    public void onClickBack(View view) {
        onBackPressed();
    }

    /**
     * 下一步
     *
     * @param view
     */
    public void onClickNext(View view) {
        if(ratio <= 0) {
            Toast.makeText(context, "正在获取保险费率,请稍后...", Toast.LENGTH_SHORT).show();
        } else if(safe_all_money.getText().length() <= 0) {
            Toast.makeText(context, "请填写保险金额!", Toast.LENGTH_SHORT).show();
        } else if (!safe_check_box.isChecked()) {
            Toast.makeText(context, "请勾选同意保险协议", Toast.LENGTH_SHORT).show();
        } else if(userGold == -1) {
            Toast.makeText(context, "正在获取账户余额,请稍后...", Toast.LENGTH_SHORT).show();
        } else if(userGold <= Double.valueOf(safe_money.getText().toString())) {
            Toast.makeText(context, "账户余额不足,请充值...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(context, ChargeActivity.class));
        } else {
            Intent intent = new Intent(context, SafeSeaEditActivity.class);
            SafeSeaInfo safeSeaInfo = new SafeSeaInfo();
            safeSeaInfo.setAmount_covered(Double.valueOf(safe_all_money.getText().toString()));//保险金额
            safeSeaInfo.setInsurance_type("" + Constants.getSeaSafeTypeValues(safe_type_spinner.getSelectedItemPosition()));//保险类型
            safeSeaInfo.setRatio(Double.valueOf(safe_percent.getText().toString()));//保险费率
            safeSeaInfo.setInsurance_charge(Double.valueOf(safe_money.getText().toString()));//保险费用
            intent.putExtra(Constants.COMMON_KEY, safeSeaInfo);
            startActivity(intent);
        }
    }
}
