package com.maogousoft.logisticsmobile.driver.activity.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.widget.HeaderView;

/**
 * Created by aliang on 2015/4/27.
 */
public class OfferDriverActivity extends BaseActivity {

    private HeaderView mHeaderView;
    private Button confirm;
    private EditText offerValue;
    private View offer_type_car, offer_type_weight;
    private int offerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_layout);
        mHeaderView = (HeaderView) findViewById(R.id.headerView);
        mHeaderView.setTitle("报价");
        initView();
    }

    private void initView() {
        confirm = (Button) findViewById(R.id.offer_confirm);
        offerValue = (EditText) findViewById(R.id.offer_value);
        offer_type_car = findViewById(R.id.offer_type_car);
        offer_type_weight = findViewById(R.id.offer_type_weight);
        confirm.setOnClickListener(this);
        offer_type_car.setOnClickListener(this);
        offer_type_weight.setOnClickListener(this);
        offer_type_car.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.offer_confirm:
                if(offerValue.getText().toString().length() <= 0) {
                    Toast.makeText(mContext, "请输入保证金金额", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, offerValue.getText(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.offer_type_car:
                // 元/车
                offerType = 0;
                offer_type_car.setSelected(true);
                offer_type_weight.setSelected(false);
                break;
            case R.id.offer_type_weight:
                offerType = 1;
                offer_type_car.setSelected(false);
                offer_type_weight.setSelected(true);
                // 元/吨
                break;
        }
    }
}
