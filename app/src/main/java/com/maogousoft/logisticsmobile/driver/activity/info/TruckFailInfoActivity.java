package com.maogousoft.logisticsmobile.driver.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.TruckFailInfo;
import com.maogousoft.logisticsmobile.driver.widget.HeaderView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aliang on 2015/5/24.
 */
public class TruckFailInfoActivity extends BaseActivity {

    private HeaderView mHeaderView;
    private TextView desc;
    private TruckFailInfo truckFailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_truck_loading_fail_info);

        initView();
        initData();
    }

    private void initView() {
        mHeaderView = (HeaderView) findViewById(R.id.headerView);
        mHeaderView.setTitle("装车不成功");
        desc = (TextView) findViewById(R.id.desc);
    }

    private void initData() {
        int orderId = getIntent().getIntExtra(Constants.ORDER_ID, -1);
        getData(orderId);
    }

    private void getData(int orderId) {
        try {
            showDefaultProgress();
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.GET_TRUCK_LOADING_FAIL_INFO);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("order_id", orderId).toString());

            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    TruckFailInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if(result instanceof TruckFailInfo) {
                                        truckFailInfo = (TruckFailInfo) result;
                                        //责任人 1托运方、2承运方 3配载方
                                        String cause = "";
                                        switch (truckFailInfo.getResponsible_people()) {
                                            case 1:
                                                cause = "托运方";
                                                break;
                                            case 2:
                                                cause = "承运方";
                                                break;
                                            case 3:
                                                cause = "配载方";
                                                break;
                                        }
                                        desc.setText(Html.fromHtml(getString(R.string.truck_loading_fail, cause)));
                                    }
                                    break;
                                default:
                                    showMsg(result.toString());
                                    break;
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onConfirm(View view) {
        Toast.makeText(mContext, "订单确认！", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onError(View view) {
        Intent intent = new Intent(mContext, TruckFailedReasonActivity.class);
        intent.putExtra(Constants.ORDER_ID, truckFailInfo.getOrder_id());
        startActivity(intent);
        finish();
    }
}
