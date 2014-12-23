package com.ybxiang.driver.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.model.CardInfo;

/**
 * Created by aliang on 2014/8/30.
 */
public class CheckCardResultActivity extends BaseActivity implements View.OnClickListener {

    private TextView name, gender, result, birthday, region, number;
    private Button share;
    private ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_card_result);
        initView();
        initData();
    }

    private void initView() {
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("验证结果");
        findViewById(R.id.titlebar_id_more).setVisibility(View.GONE);
        findViewById(R.id.titlebar_id_back).setOnClickListener(this);

        name = (TextView) findViewById(R.id.name);
        gender = (TextView) findViewById(R.id.gender);
        result = (TextView) findViewById(R.id.result);
        birthday = (TextView) findViewById(R.id.birthday);
        region = (TextView) findViewById(R.id.region);
        number = (TextView) findViewById(R.id.number);
        share = (Button) findViewById(R.id.share);
        share.setOnClickListener(this);
        photo = (ImageView) findViewById(R.id.photo);
    }

    private void initData() {
        CardInfo cardInfo = (CardInfo) getIntent().getSerializableExtra(Constants.COMMON_KEY);
        name.setText(name.getText() + cardInfo.getId_name());
        gender.setText(gender.getText() + cardInfo.getGender());
        switch (cardInfo.getVerifyresult()) {
            case 0:
                result.setText("验证失败");
                result.setTextColor(0xffff0000);
                break;
            case 1:
                result.setText("一致");
                break;
            case 2:
//                result.setText("身份验证不一致");
                result.setText("不一致");
                result.setTextColor(0xffff0000);
                break;
            case 3:
//                result.setText("库中无此号码");
                result.setText("不一致");
                result.setTextColor(0xffff0000);
                break;
            case 4:
                result.setText("身份验证一致，无照片");
                break;
        }
        birthday.setText(cardInfo.getId_year() + "年" + cardInfo.getId_month() + "月" + cardInfo.getId_day() + "日");
        region.setText(cardInfo.getRegioninfo());
        number.setText(cardInfo.getId_num());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.share:
                share();
                break;
        }
    }
}
