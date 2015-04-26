package com.ybxiang.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.maogousoft.logisticsmobile.driver.CitySelectView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.vip.AddActivity;
import com.maogousoft.logisticsmobile.driver.activity.vip.ShopListActivity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aliang on 2014/9/10.
 */
public class SearchShopActivity extends BaseActivity {

    private Button mBack;
    private Button mQuery;
    private CitySelectView citySelect;
    private Button titlebar_id_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_shop_list);
        initViews();
        initListener();
    }

    // 初始化视图
    private void initViews() {

        ((TextView) findViewById(R.id.titlebar_id_content)).setText("物流园区");
        titlebar_id_more = (Button) findViewById(R.id.titlebar_id_more);
        titlebar_id_more.setText("添加园区");

        mQuery = (Button) findViewById(R.id.query);
        citySelect = (CitySelectView) findViewById(R.id.cityselect_start);
        mQuery.setOnClickListener(this);
    }

    private void initListener() {
        titlebar_id_more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //进入添加物流园区的页面
                startActivity(new Intent(mContext, AddActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.query:
                if (citySelect.getSelectedProvince() == null || citySelect.getSelectedCity() == null) {
                    Toast.makeText(mContext, "请至少选择到城市一级", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (citySelect.getSelectedProvince() == null || citySelect.getSelectedProvince() == null) {
                    showMsg("请选择出发地，目的地。");
                    return;
                }
                try {
                    JSONObject params = new JSONObject()
                            .put("province", citySelect.getSelectedProvince().getId())
                            .put("city", citySelect.getSelectedCity() == null ? "" :citySelect.getSelectedCity().getId())
                            .put("district", citySelect.getSelectedTowns() == null ? "" : citySelect.getSelectedTowns().getId());
                    Intent intent = new Intent(mContext, ShopListActivity.class);
                    intent.putExtra(Constants.COMMON_KEY, params.toString());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}