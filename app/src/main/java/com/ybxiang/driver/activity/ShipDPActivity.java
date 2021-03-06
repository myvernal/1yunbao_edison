package com.ybxiang.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.MyBusinessCardUser;
import com.maogousoft.logisticsmobile.driver.activity.share.ShareActivity;

/**
 * Created by aliang on 2014/8/10.
 */
public class ShipDPActivity extends BaseActivity {

    private View part1;
    private View part2;
    private View part3;
    private View part4;
    private View part5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ship_dp_layout);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("物流点评(56dp.com)");
        findViewById(R.id.titlebar_id_more).setOnClickListener(this);
        // 返回按钮生效

        part1 = findViewById(R.id.part1);
        part2 = findViewById(R.id.part2);
        part3 = findViewById(R.id.part3);
        part4 = findViewById(R.id.part4);
        part5 = findViewById(R.id.part5);
        part1.setOnClickListener(this);
        part2.setOnClickListener(this);
        part3.setOnClickListener(this);
        part4.setOnClickListener(this);
        part5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.titlebar_id_more:
				startActivity(new Intent(mContext, ShareActivity.class));
                break;
            case R.id.part1:
                startActivity(new Intent(mContext, SearchThreePartyActivity.class));
                break;
            case R.id.part2:
                startActivity(new Intent(mContext, SearchSpecialLineActivity.class));
                break;
            case R.id.part3:
                startActivity(new Intent(mContext, SearchFactoryUserActivity.class));
                break;
            case R.id.part4:
                startActivity(new Intent(mContext, SearchShopActivity.class));
                break;
            case R.id.part5:
                startActivity(new Intent(mContext, MyBusinessCardUser.class));
                break;

        }
    }
}
