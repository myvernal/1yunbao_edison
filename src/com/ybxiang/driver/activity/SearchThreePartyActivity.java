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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.query:
                if (citySelect.getSelectedProvince() == null || citySelect.getSelectedCity() == null) {
                    Toast.makeText(context, "请至少选择到城市一级", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (citySelect.getSelectedProvince() == null || citySelect.getSelectedProvince() == null) {
                    showMsg("请选择出发地，目的地。");
                    return;
                }
                try {
                    JSONObject params = new JSONObject()
                            .put("area", citySelect.getSelectedProvince().getName())
                            .put("city", citySelect.getSelectedCity().getName())
                            .put("distict", citySelect.getSelectedTowns() == null ? "" : citySelect.getSelectedCity().getName())
                            .put("searchKey", TextUtils.isEmpty(searchKey.getText()) ? "" : searchKey.getText());
                    Intent intent = new Intent(context, SearchDPListActivity.class);
                    intent.putExtra(Constants.COMMON_KEY, params.toString());
                    intent.putExtra(Constants.COMMON_ACTION_KEY, Constants.QUERY_THREE_PARTY);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
