package com.ybxiang.driver.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.ybxiang.driver.model.SearchDpResultInfo;

import java.io.Serializable;

/**
 * Created by aliang on 2014/9/6.
 */
public class FactoryUserDetailActivity extends BaseActivity {

    private SearchDpResultInfo searchDpResultInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.factory_user_detail);
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("工厂货主详情");
        findViewById(R.id.titlebar_id_back).setOnClickListener(this);
        findViewById(R.id.titlebar_id_more).setVisibility(View.GONE);

        initData();
    }

    private void initData() {
        Serializable serializable = getIntent().getSerializableExtra(Constants.COMMON_KEY);
        if(serializable != null) {
            searchDpResultInfo = (SearchDpResultInfo) serializable;
            ((TextView) findViewById(R.id.name)).setText(searchDpResultInfo.getCOMPANY_NAME());
            ((TextView) findViewById(R.id.contact)).setText(searchDpResultInfo.getNAME());
            ((TextView) findViewById(R.id.phone)).setText(searchDpResultInfo.getPHONE());
            ((TextView) findViewById(R.id.address)).setText(searchDpResultInfo.getADDRESS());
            ((TextView) findViewById(R.id.desc)).setText(searchDpResultInfo.getMIAOSU());
            findViewById(R.id.desc_layout).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
