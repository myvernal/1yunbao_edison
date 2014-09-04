package com.ybxiang.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.maogousoft.logisticsmobile.driver.CitySelectView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.CardInfo;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aliang on 2014/9/5.
 */
public class SearchThreePartyActivity extends BaseActivity {

    private Button mBack;
    private Button mQuery;
    private CitySelectView citySelect;
    private EditText searchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_three_party_layout);
        initViews();
        initUtils();
        initData();
    }

    // 初始化视图
    private void initViews() {
        mBack = (Button) findViewById(R.id.titlebar_id_back);
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("三方物流");
        findViewById(R.id.titlebar_id_more).setVisibility(View.GONE);
        mQuery = (Button) findViewById(R.id.query);

        citySelect = (CitySelectView) findViewById(R.id.cityselect_start);
        searchKey = (EditText) findViewById(R.id.searchKey);
        mBack.setOnClickListener(this);
        mQuery.setOnClickListener(this);
    }

    private void initUtils() {

    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.query:
                if (citySelect.getSelectedProvince() == null || citySelect.getSelectedCity() == null) {
                    Toast.makeText(context, "请至少选择到城市一级", Toast.LENGTH_SHORT).show();
                    return;
                }
                queryData();
                break;
        }
    }

    /**
     * 开始查询
     */
    private void queryData() {
        try {
            showSpecialProgress("正在查询,请稍后");
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, Constants.QUERY_THREE_PARTY);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject()
                    .put("area", citySelect.getSelectedProvince().getId())
                    .put("city", citySelect.getSelectedCity().getId())
                    .put("distict", citySelect.getSelectedTowns() == null ? "" : citySelect.getSelectedCity().getId())
                    .put("searchKey", TextUtils.isEmpty(searchKey.getText()) ? "":searchKey.getText()).toString());
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    CardInfo.class, new AjaxCallBack() {
                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    Toast.makeText(context, "没有找到数据,请扩大搜索条件", Toast.LENGTH_SHORT).show();
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
