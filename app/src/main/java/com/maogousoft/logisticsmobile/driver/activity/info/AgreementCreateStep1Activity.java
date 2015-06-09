package com.maogousoft.logisticsmobile.driver.activity.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.widget.HeaderView;

import java.io.Serializable;

/**
 * Created by aliang on 2014/11/13.
 */
public class AgreementCreateStep1Activity extends BaseActivity {

    private RadioButton radioButtonShipper;
    private RadioButton radioButtonThird;
    private int orderId;
    private int agreementType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_step1);
        HeaderView mHeaderView = (HeaderView) findViewById(R.id.headerView);
        mHeaderView.setTitle(R.string.agreement_edit_tip);

        initView();
        initData();
    }

    private void initView() {
        radioButtonShipper = (RadioButton) findViewById(R.id.agreement_shipper);
        radioButtonThird = (RadioButton) findViewById(R.id.agreement_third);
        radioButtonShipper.setText(Html.fromHtml(getString(R.string.agreement_shipper)));
        radioButtonThird.setText(Html.fromHtml(getString(R.string.agreement_third)));
        radioButtonShipper.setOnClickListener(this);
        radioButtonThird.setOnClickListener(this);
    }

    private void initData() {
        orderId = getIntent().getIntExtra(Constants.ORDER_ID, -1);
        if (orderId == -1) {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.agreement_shipper:
                agreementType = 1;//运输合同
                break;
            case R.id.agreement_third:
                agreementType = 2;//中介合同
                break;
        }
    }

    public void onNext(View view) {
        startActivity(new Intent(mContext, AgreementCreateStep2Activity.class)
                .putExtra(Constants.ORDER_ID, orderId)
                .putExtra(Constants.AGREEMENT_TYPE, agreementType));
        finish();
    }
}
