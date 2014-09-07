package com.ybxiang.driver.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.adapter.FactoryUserAdapter;
import com.maogousoft.logisticsmobile.driver.adapter.SpecialLineAdapter;
import com.ybxiang.driver.model.SearchDpResultInfo;

import java.io.Serializable;

/**
 * Created by aliang on 2014/9/6.
 */
public class SpecialLineDetailActivity extends BaseActivity {

    private SearchDpResultInfo searchDpResultInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.special_line_detail);
        findViewById(R.id.titlebar_id_back).setOnClickListener(this);
        findViewById(R.id.titlebar_id_more).setVisibility(View.GONE);
        initData();
    }

    private void initData() {
        Serializable serializable = getIntent().getSerializableExtra(Constants.COMMON_KEY);
        String action = getIntent().getStringExtra(Constants.COMMON_ACTION_KEY);
        if (Constants.QUERY_SPECIAL_LINE.equals(action)) {
            ((TextView) findViewById(R.id.titlebar_id_content)).setText("零担专线详情");
        } else {
            ((TextView) findViewById(R.id.titlebar_id_content)).setText("三方物流详情");
        }
        if (serializable != null) {
            searchDpResultInfo = (SearchDpResultInfo) serializable;
            ((TextView) findViewById(R.id.name)).setText(searchDpResultInfo.getCOMPANY_NAME());
            ((TextView) findViewById(R.id.way)).setText(searchDpResultInfo.getFROMAREA() + searchDpResultInfo.getFROMAREA1() + "--"
                    + searchDpResultInfo.getENDAREA() + searchDpResultInfo.getENDAREA1());
            ((TextView) findViewById(R.id.phone)).setText(searchDpResultInfo.getPHONE());
            ((TextView) findViewById(R.id.desc)).setText(searchDpResultInfo.getMIAOSU());
            ((TextView) findViewById(R.id.address)).setText(searchDpResultInfo.getADDRESS());
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
