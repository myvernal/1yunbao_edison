package com.ybxiang.driver.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;

public class CarSourceDetailActivity extends BaseActivity {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = CarSourceDetailActivity.this;
        TextView textView = new TextView(mContext);
        textView.setText("车源详细信息界面目前正在开发中...");
        setContentView(textView);
    }
}
