package com.ybxiang.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.db.CityDBUtils;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aliang on 2014/8/24.
 * 已发布货源详情
 */
public class MySourceDetailActivity extends BaseActivity {

    private static final String TAG = "MySourceDetailActivity";
    private CityDBUtils dbUtils;
    private NewSourceInfo mSourceInfo;
    private Button mBack, mShare, source_detail_edit, source_detail_republic, source_detail_delete;
    private TextView source_detail_way, source_detail_content, source_detail_time, source_detail_tip,
            source_detail_contact, source_detail_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_source_detail);
        dbUtils = new CityDBUtils(application.getCitySDB());
        initViews();
        initData();
    }

    // 初始化视图
    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("已发布货源详情");
        mBack = (Button) findViewById(R.id.titlebar_id_back);
        mBack.setOnClickListener(this);
        mShare = (Button) findViewById(R.id.titlebar_id_more);
        mShare.setText("分享");
        mShare.setOnClickListener(this);

        source_detail_way = (TextView) findViewById(R.id.source_detail_way);
        source_detail_content = (TextView) findViewById(R.id.source_detail_content);
        source_detail_time = (TextView) findViewById(R.id.source_detail_time);
        source_detail_tip = (TextView) findViewById(R.id.source_detail_tip);
        source_detail_contact = (TextView) findViewById(R.id.source_detail_contact);
        source_detail_phone = (TextView) findViewById(R.id.source_detail_phone);

        source_detail_delete = (Button) findViewById(R.id.source_detail_delete);
        source_detail_delete.setOnClickListener(this);
        source_detail_republic = (Button) findViewById(R.id.source_detail_republic);
        source_detail_republic.setOnClickListener(this);
        source_detail_edit = (Button) findViewById(R.id.source_detail_edit);
        source_detail_edit.setOnClickListener(this);
    }

    private void initData() {
        mSourceInfo = (NewSourceInfo) getIntent().getSerializableExtra(Constants.COMMON_KEY);
        String wayStart = dbUtils.getCityInfo(mSourceInfo.getStart_province(), mSourceInfo.getStart_city(), mSourceInfo.getStart_district());
        String wayEnd = dbUtils.getCityInfo(mSourceInfo.getEnd_province(), mSourceInfo.getEnd_city(), mSourceInfo.getEnd_district());
        source_detail_way.setText(source_detail_way.getText() + wayStart + "-->" + wayEnd);
        if (mSourceInfo.getCreate_time() > 0) {
            Date date = new Date(Long.valueOf(mSourceInfo.getCreate_time()));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String locationTime = simpleDateFormat.format(date);
            source_detail_time.setText(source_detail_time.getText() + locationTime);
        }
        StringBuffer sb = new StringBuffer();
        sb.append(mSourceInfo.getCargo_desc());
        //运输方式
        Integer shipType = mSourceInfo.getShip_type();
        if (shipType != null && shipType > 0) {
            String[] shipTypeStr = context.getResources().getStringArray(R.array.ship_type);
            for (int i = 0; i < Constants.shipTypeValues.length; i++) {
                if (Constants.shipTypeValues[i] == shipType) {
                    sb.append("  " + shipTypeStr[i]);
                }
            }
        }
        //车型
        Integer carTypeValue = mSourceInfo.getCar_type();
        if (carTypeValue != null && carTypeValue > 0) {
            String[] carTypeStr = context.getResources().getStringArray(R.array.car_types_name);
            for (int i = 0; i < Constants.carTypeValues.length; i++) {
                if (Constants.carTypeValues[i] == carTypeValue) {
                    sb.append("  " + carTypeStr[i]);
                }
            }
        }
        //车长
        sb.append("  " + mSourceInfo.getCar_length() + "米");
        //报价单位
        Integer unitPrice = mSourceInfo.getCargo_unit();
        if (unitPrice != null) {
            String[] unitPriceStr = context.getResources().getStringArray(R.array.car_price_unit);
            for (int i = 0; i < Constants.unitTypeValues.length; i++) {
                if (Constants.unitTypeValues[i] == unitPrice) {
                    sb.append("  " + mSourceInfo.getUnit_price() + "元/" + unitPriceStr[i]);
                }
            }
        }
        //保证金
//        sb.append("  " + mSourceInfo.getUser_bond() + "元");
        source_detail_content.setText(source_detail_content.getText() + sb.toString());
        source_detail_tip.setText(mSourceInfo.getCargo_tip() + "\n" + mSourceInfo.getCargo_remark());
        source_detail_contact.setText(mSourceInfo.getCargo_user_name());
        source_detail_phone.setText(mSourceInfo.getCargo_user_phone());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.titlebar_id_more:
                share();
                break;
            case R.id.titlebar_id_back:
                finish();
                break;
            case R.id.source_detail_edit:
                Intent intent = new Intent(context, PublishGoodsSourceActivity.class);
                intent.putExtra(Constants.COMMON_KEY, mSourceInfo);
                startActivity(intent);
                finish();
                break;
            case R.id.source_detail_republic:
                deleteOrRepublishOrder(Constants.REPUBLISH_ORDER);
                break;
            case R.id.source_detail_delete:
                deleteOrRepublishOrder(Constants.DELETE_PUBLISH_ORDER);
                break;
        }
    }

    // 删除或者重发货源

    private void deleteOrRepublishOrder(String action) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.ACTION, action);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("id", mSourceInfo.getId()).toString());
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    NewSourceInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    Toast.makeText(context, "操作成功", Toast.LENGTH_SHORT).show();
                                    finish();
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
}
