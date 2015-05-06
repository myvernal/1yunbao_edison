package com.maogousoft.logisticsmobile.driver.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.HistroyOrderActivity;

/**
 * Created by aliang on 2015/5/6.
 */
public class MoneyManagerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_manager);
        initViews();
    }

    private void initViews() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText(R.string.string_money_manager);
        findViewById(R.id.titlebar_id_more).setVisibility(View.VISIBLE);
    }

    /**
     * 历史订单
     *
     * @param view
     */
    public void onMyHistoryOrder(View view) {
        startActivity(new Intent(mContext, HistroyOrderActivity.class));
    }
}
