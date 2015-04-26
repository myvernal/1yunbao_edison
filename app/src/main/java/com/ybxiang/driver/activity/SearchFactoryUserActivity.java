package com.ybxiang.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.CitySelectView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aliang on 2014/9/5.
 */
public class SearchFactoryUserActivity extends BaseActivity {

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

        ((TextView) findViewById(R.id.titlebar_id_content)).setText("工厂货主");
        findViewById(R.id.titlebar_id_more).setVisibility(View.GONE);
        findViewById(R.id.label).setVisibility(View.GONE);
        mQuery = (Button) findViewById(R.id.query);

        citySelect = (CitySelectView) findViewById(R.id.cityselect_start);
        citySelect.setVisibility(View.GONE);
        searchKey = (EditText) findViewById(R.id.searchKey);
        mQuery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.query:
                try {
                    JSONObject params = new JSONObject()
//                            .put("area", citySelect.getSelectedProvince() == null ? "" : citySelect.getSelectedProvince().getName())
//                            .put("city", citySelect.getSelectedCity() == null ? "" : citySelect.getSelectedCity().getName())
//                            .put("distict", citySelect.getSelectedTowns() == null ? "" : citySelect.getSelectedTowns().getName())
                            .put("searchKey", TextUtils.isEmpty(searchKey.getText()) ? "" : searchKey.getText());
                    Intent intent = new Intent(mContext, SearchDPListActivity.class);
                    intent.putExtra(Constants.COMMON_KEY, params.toString());
                    intent.putExtra(Constants.COMMON_ACTION_KEY, Constants.QUERY_FACTORY_USER);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
