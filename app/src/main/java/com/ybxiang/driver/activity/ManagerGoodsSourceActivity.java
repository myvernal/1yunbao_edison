package com.ybxiang.driver.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;

public class ManagerGoodsSourceActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(mContext);
        textView.setText("管理货源界面目前正在开发中...");
        setContentView(textView);
    }
}
