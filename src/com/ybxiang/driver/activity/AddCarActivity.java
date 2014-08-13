package com.ybxiang.driver.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.maogousoft.logisticsmobile.driver.CitySelectView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.CarInfo;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by aliang on 2014/8/14.
 */
public class AddCarActivity extends BaseActivity {

    private EditText mCarNum, mCarlength, car_weight, description, ower_name, ower_phone;
    private CitySelectView cityselectStart; // 出发地
    private CitySelectView cityselectEnd1, cityselectEnd2, cityselectEnd3; // 目的地
    private Spinner search_car_type;
    private Button mTitleBarBack;
    private Button mTitleBarMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_cars);
        initViews();
    }

    // 初始化视图
    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("添加车辆");
        mTitleBarBack = (Button) findViewById(R.id.titlebar_id_back);
        mTitleBarMore = (Button) findViewById(R.id.titlebar_id_more);
        mTitleBarMore.setVisibility(View.GONE);

        mTitleBarMore.setOnClickListener(this);
        mTitleBarBack.setOnClickListener(this);
        cityselectStart = (CitySelectView) findViewById(R.id.cityselect_start);
        cityselectEnd1 = (CitySelectView) findViewById(R.id.cityselect_end1);
        cityselectEnd2 = (CitySelectView) findViewById(R.id.cityselect_end2);
        cityselectEnd3 = (CitySelectView) findViewById(R.id.cityselect_end3);

        mCarNum = (EditText) findViewById(R.id.mCarNum); //车牌号
        mCarlength = (EditText) findViewById(R.id.mCarlength); //车长
        car_weight = (EditText) findViewById(R.id.car_weight); //车重
        ower_name = (EditText) findViewById(R.id.ower_name); //联系人
        ower_phone = (EditText) findViewById(R.id.ower_phone); //联系人手机号码
        description = (EditText) findViewById(R.id.description); //补充说明
        search_car_type = (Spinner) findViewById(R.id.search_car_type);//车型
    }

    // 请求指定页数的数据
    public void addToMyCard(View view) {
        try {
            final JSONObject jsonObject = new JSONObject();
            JSONObject params = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.ADD_MY_FLEET);
            jsonObject.put(Constants.TOKEN, application.getToken());
            //组装参数
            params.put("driver_name", ower_name.getText());
            params.put("phone", ower_phone.getText());
            if (cityselectStart.getSelectedProvince() != null) {
                params.put("start_province", cityselectStart.getSelectedProvince().getId());
            }
            if (cityselectStart.getSelectedCity() != null) {
                params.put("start_city", cityselectStart.getSelectedCity().getId());
            }
            if (cityselectStart.getSelectedTowns() != null) {
                params.put("start_district", cityselectStart.getSelectedTowns().getId());
            }
            if (cityselectEnd1.getSelectedProvince() != null) {
                params.put("end_province1", cityselectEnd1.getSelectedProvince().getId());
            }
            if (cityselectEnd1.getSelectedCity() != null) {
                params.put("end_city1", cityselectEnd1.getSelectedCity().getId());
            }
            if (cityselectEnd1.getSelectedTowns() != null) {
                params.put("end_district1", cityselectEnd1.getSelectedTowns().getId());
            }
            if (cityselectEnd2.getSelectedProvince() != null) {
                params.put("end_province2", cityselectEnd2.getSelectedProvince().getId());
            }
            if (cityselectEnd2.getSelectedCity() != null) {
                params.put("end_city2", cityselectEnd2.getSelectedCity().getId());
            }
            if (cityselectEnd2.getSelectedTowns() != null) {
                params.put("end_district2", cityselectEnd2.getSelectedTowns().getId());
            }
            if (cityselectEnd3.getSelectedProvince() != null) {
                params.put("end_province3", cityselectEnd3.getSelectedProvince().getId());
            }
            if (cityselectEnd3.getSelectedCity() != null) {
                params.put("end_city3", cityselectEnd3.getSelectedCity().getId());
            }
            if (cityselectEnd3.getSelectedTowns() != null) {
                params.put("end_district3", cityselectEnd3.getSelectedTowns().getId());
            }
            params.put("car_length", mCarlength.getText());
            params.put("car_weight", car_weight.getText());
            params.put("plate_number", mCarNum.getText());
            params.put("car_type", Constants.getCarTypeValues(search_car_type.getSelectedItemPosition()));
            params.put("remark", description.getText());
            //组装参数结束
            jsonObject.put(Constants.JSON, params.toString());
            showDefaultProgress();
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    CarInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    Toast.makeText(context, "添加车辆成功!", Toast.LENGTH_SHORT).show();
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
