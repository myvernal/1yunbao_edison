package com.ybxiang.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.SafePinanInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by aliang on 2014/9/1.
 */
public class SafePinanDetailActivity extends BaseActivity {
    private Button mTitleBarBack, mTitleBarMore;
    private TextView start_date, insured_name, insured_phone, shiping_number,
            plate_number, guache_Num, lianxiren_name, peichang_area, package_type, start_area, start_city,
            end_area, end_city, citySelectView;
    private TextView amount_covered, insurance_charge;
    private SafePinanInfo safePinanInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safe_pingan_comfirm_layout);
        initViews();
        initData();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("中国平安国内公路货运险确认订单");
        // 返回按钮生效

        // 更多按钮隐藏
        // 更多按钮隐藏
        findViewById(R.id.titlebar_id_more).setVisibility(View.GONE);

        insurance_charge = (TextView) findViewById(R.id.insurance_charge);
        amount_covered = (TextView) findViewById(R.id.amount_covered);
        lianxiren_name = (TextView) findViewById(R.id.lianxiren_name);
        guache_Num = (TextView) findViewById(R.id.guache_Num);
        start_date = (TextView) findViewById(R.id.start_date);
        insured_name = (TextView) findViewById(R.id.insured_name);
        insured_phone = (TextView) findViewById(R.id.insured_phone);
        shiping_number = (TextView) findViewById(R.id.shiping_number);
        plate_number = (TextView) findViewById(R.id.plate_number);
        start_date = (TextView) findViewById(R.id.start_date);
        peichang_area = (TextView) findViewById(R.id.peichang_area);
        package_type = (TextView) findViewById(R.id.package_type);
        start_area = (TextView) findViewById(R.id.start_area);
        start_city = (TextView) findViewById(R.id.start_city);
        end_area = (TextView) findViewById(R.id.end_area);
        end_city = (TextView) findViewById(R.id.end_city);
        citySelectView = (TextView) findViewById(R.id.citySelectView);
    }

    private void initData() {
        Serializable serializable = getIntent().getSerializableExtra(Constants.COMMON_KEY);
        if (serializable != null) {
            safePinanInfo = (SafePinanInfo) serializable;
            amount_covered.setText(amount_covered.getText().toString() + safePinanInfo.getAmount_covered() + "万元(人民币)");
            insurance_charge.setText(insurance_charge.getText().toString() + safePinanInfo.getInsurance_charge() + "元");
            start_date.setText(safePinanInfo.getStart_date());
            insured_name.setText(safePinanInfo.getInsured_name());
            insured_phone.setText(safePinanInfo.getInsured_phone());
            shiping_number.setText(safePinanInfo.getShiping_number());
            plate_number.setText(safePinanInfo.getPlate_number());
            guache_Num.setText(safePinanInfo.getGuache_Num());
            lianxiren_name.setText(safePinanInfo.getLianxiren_name());
            peichang_area.setText(safePinanInfo.getPeichang_area());
            package_type.setText(safePinanInfo.getPackage_type_str());
            start_area.setText(safePinanInfo.getStart_area_str());
            start_city.setText(safePinanInfo.getStart_city_str());
            end_area.setText(safePinanInfo.getEnd_area_str());
            end_city.setText(safePinanInfo.getEnd_city_str());
            citySelectView.setText(safePinanInfo.getProvinceListArea() + safePinanInfo.getProvinceListCity() + safePinanInfo.getDistrictList());
        }
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
        try {
            showSpecialProgress("正在投保,请稍后");
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.SAVE_PINGAN);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject()
                    .put("type", safePinanInfo.getType())
                    .put("insured_name", safePinanInfo.getInsured_name())
                    .put("insured_phone", safePinanInfo.getInsured_phone())
                    .put("shiping_number", safePinanInfo.getShiping_number())
                    .put("packet_number", safePinanInfo.getPacket_number())
                    .put("plate_number", safePinanInfo.getPlate_number())
                    .put("start_date", safePinanInfo.getStart_date())
                    .put("amount_covered", Double.valueOf(safePinanInfo.getAmount_covered()) * 10000)
                    .put("start_area", safePinanInfo.getStart_area())
                    .put("start_city", safePinanInfo.getStart_city())
                    .put("end_area", safePinanInfo.getEnd_area())
                    .put("end_city", safePinanInfo.getEnd_city())
                    .put("provinceListArea", safePinanInfo.getProvinceListArea())
                    .put("provinceListCity", safePinanInfo.getProvinceListCity())
                    .put("districtList", safePinanInfo.getDistrictList())
                    .put("lianxiren_name", safePinanInfo.getLianxiren_name())
                    .put("package_type", safePinanInfo.getPackage_type()));
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    SafePinanInfo.class, new AjaxCallBack() {
                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result instanceof String) {
                                        showMsg(result.toString());
                                    }
                                    Intent intent = new Intent(mContext, SafeListActivity.class);
                                    intent.putExtra(Constants.COMMON_KEY, Constants.SAFE_PINGAN);
                                    startActivity(intent);
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
