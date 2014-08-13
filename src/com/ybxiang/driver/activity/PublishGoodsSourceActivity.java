package com.ybxiang.driver.activity;

// add this file for 发布货源

import android.text.TextUtils;
import android.widget.*;
import com.maogousoft.logisticsmobile.driver.CitySelectView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import org.json.JSONObject;

public class PublishGoodsSourceActivity extends BaseActivity implements OnClickListener {
    private Context mContext;
    private Button mBack, mRightButton;
    private CitySelectView cityselectStart, cityselectEnd;
    private TextView mTitleBarContent;
    private int selectedCarWay = 57;// 选择的车辆方式
    private EditText source_id_publish_cargo_desc, source_id_publish_car_length,
            source_id_publish_unit_price, source_id_publish_user_bond, source_id_publish_cargo_remark,
            source_id_publish_contact_name, source_id_publish_contact_phone,
            source_id_publish_validate_day, source_id_publish_validate_hour;
    private Spinner source_id_publish_cargo_type, source_id_publish_car_type,
            source_id_publish_cargo_unit, source_id_publish_cargo_tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_goods_source);
        initViews();
    }

    // 初始化视图
    private void initViews() {
        mContext = PublishGoodsSourceActivity.this;
        mTitleBarContent = (TextView) findViewById(R.id.titlebar_id_content);
        mRightButton = (Button) findViewById(R.id.titlebar_id_more);
        mBack = (Button) findViewById(R.id.titlebar_id_back);
        mTitleBarContent.setText("发布货源");
        mRightButton.setText("已发布");
        cityselectStart = (CitySelectView) findViewById(R.id.cityselect_start);
        cityselectEnd = (CitySelectView) findViewById(R.id.cityselect_end);

        source_id_publish_cargo_desc = (EditText) findViewById(R.id.source_id_publish_cargo_desc);
        source_id_publish_car_length = (EditText) findViewById(R.id.source_id_publish_car_length);
        source_id_publish_unit_price = (EditText) findViewById(R.id.source_id_publish_unit_price);
        source_id_publish_user_bond = (EditText) findViewById(R.id.source_id_publish_user_bond);
        source_id_publish_cargo_remark = (EditText) findViewById(R.id.source_id_publish_cargo_remark);
        source_id_publish_contact_name = (EditText) findViewById(R.id.source_id_publish_contact_name);
        source_id_publish_contact_phone = (EditText) findViewById(R.id.source_id_publish_contact_phone);
        source_id_publish_validate_day = (EditText) findViewById(R.id.source_id_publish_validate_day);
        source_id_publish_validate_hour = (EditText) findViewById(R.id.source_id_publish_validate_hour);

        source_id_publish_cargo_type = (Spinner) findViewById(R.id.source_id_publish_cargo_type);
        source_id_publish_car_type = (Spinner) findViewById(R.id.source_id_publish_car_type);
        source_id_publish_cargo_unit = (Spinner) findViewById(R.id.source_id_publish_cargo_unit);
        source_id_publish_cargo_tip = (Spinner) findViewById(R.id.source_id_publish_cargo_tip);
        mBack.setOnClickListener(this);
        mRightButton.setOnClickListener(this);
    }

    /**
     * 管理发布过的车源
     */
    @Override
    public void onClick(View v) {
        ((BaseActivity) mContext).setIsRightKeyIntoShare(false);
        super.onClick(v);
        switch (v.getId()) {
            case R.id.titlebar_id_more:
                Intent intent = new Intent();
                intent.setClass(mContext, MySourceActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 运输方式：零担，整车，二选其一
     *
     * @param view
     */
    public void onChooseCarWay(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        if (!checked) {
            showMsg("必须选择一种运输方式【零担，整车】");
            return;
        }
        switch (view.getId()) {
            case R.id.car_way_part:
                // 零担
                selectedCarWay = 58;
                break;
            case R.id.car_way_whole:
                selectedCarWay = 57;
                // 整车
                break;
        }
    }

    /**
     * 发布货源
     *
     * @param view
     */
    public void onPublishSource(View view) {
        if (cityselectStart.getSelectedProvince() == null || cityselectEnd.getSelectedProvince() == null) {
            showMsg("请选择出发地，目的地。");
            return;
        }
        final JSONObject jsonObject = new JSONObject();
        final JSONObject params = new JSONObject();
        try {
            jsonObject.put(Constants.ACTION, Constants.PUBLISH_SOURCE);
            jsonObject.put(Constants.TOKEN, application.getToken());
            params.put("start_province", cityselectStart.getSelectedProvince()
                    .getId());
            params.put("end_province", cityselectEnd.getSelectedProvince()
                    .getId());

            if (cityselectStart.getSelectedCity() != null) {
                params.put("start_city", cityselectStart.getSelectedCity()
                        .getId());
            }

            if (cityselectEnd.getSelectedCity() != null) {
                params.put("end_city", cityselectEnd.getSelectedCity().getId());
            }

            if (cityselectStart.getSelectedTowns() != null) {
                params.put("start_district", cityselectStart.getSelectedTowns()
                        .getId());
            }

            if (cityselectEnd.getSelectedTowns() != null) {
                params.put("end_district", cityselectEnd.getSelectedTowns()
                        .getId());
            }

            if (!TextUtils.isEmpty(source_id_publish_cargo_desc.getText().toString())) {
                params.put("cargo_desc", source_id_publish_cargo_desc.getText().toString());
            }

            params.put("cargo_type", Constants.getSourceTypeValues(source_id_publish_cargo_type.getSelectedItemPosition()));
            params.put("car_way", selectedCarWay);  // add PR1.3
            params.put("car_length", source_id_publish_car_length.getText().toString());
            params.put("car_type", Constants.getCarTypeValues(source_id_publish_car_type.getSelectedItemPosition()));
            params.put("unit_price", source_id_publish_unit_price.getText().toString());
            params.put("cargo_unit", Constants.getUnitTypeValues(source_id_publish_cargo_unit.getSelectedItemPosition()));
            params.put("user_bond", source_id_publish_user_bond.getText().toString() + "元");
            params.put("cargo_tip", ((TextView) source_id_publish_cargo_tip.getSelectedView()).getText().toString());
            params.put("cargo_remark", source_id_publish_cargo_remark.getText().toString());
            params.put("contact_name", source_id_publish_contact_name.getText().toString());
            params.put("contact_phone", source_id_publish_contact_phone.getText().toString());
            params.put("validate_day", source_id_publish_validate_day.getText().toString());
            params.put("validate_hour", source_id_publish_validate_hour.getText().toString());
            //过期字段
            params.put("cargo_number", "1");
            long day = Integer.valueOf(source_id_publish_validate_day.getText().toString()) * 24 * 60 * 60 * 1000;
            long hour = Integer.valueOf(source_id_publish_validate_hour.getText().toString()) * 60 * 60 * 1000;
            params.put("validate_time", day + hour);
            params.put("loading_time", 60*60*1000);

            jsonObject.put(Constants.JSON, params);
            showDefaultProgress();
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    null, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    Toast.makeText(mContext, "发布货源成功", Toast.LENGTH_SHORT).show();
                                    break;
                                case ResultCode.RESULT_FAILED:
                                    Toast.makeText(mContext, "发布货源失败", Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
