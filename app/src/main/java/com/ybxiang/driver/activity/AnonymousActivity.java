package com.ybxiang.driver.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.info.LoginActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.RegisterActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.RegisterShipperActivity;

/**
 * Created by aliang on 2014/8/19.
 */
public class AnonymousActivity extends Activity {

    private MGApplication application;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_anonymous_layout);
        application = (MGApplication) getApplication();
        if(!application.isAnonymous()) {
            finish();
        }
    }

    public void onClickLoginNow(View view) {
        startActivity(new Intent(mContext, LoginActivity.class));
        finish();
    }

    public void onClickRegisterNow(View view) {
        int mUserType = application.getUserType();
        switch (mUserType) {
            // 司机
            case Constants.USER_DRIVER:
                startActivity(new Intent(mContext, RegisterActivity.class));
                break;
            // 货主
            case Constants.USER_SHIPPER:
                startActivity(new Intent(mContext, RegisterShipperActivity.class));
                break;
            default:
                break;
        }
        finish();
    }

    public void onClickBackNow(View view) {
        Intent intent = new Intent();
        setResult(Constants.ANONYMOUS_RESULT_CODE, intent);
        finish();
    }
}
