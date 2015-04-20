package com.maogousoft.logisticsmobile.driver.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.MainActivity;
import com.maogousoft.logisticsmobile.driver.activity.home.MyabcActivityDriver;
import com.maogousoft.logisticsmobile.driver.activity.home.MyabcActivityShipper;
import com.ybxiang.driver.activity.PublishCarSourceActivity;

/**
 * Created by aliang on 2015/4/20.
 */
public class CommonBottomView extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private View mHome, mPublish, mPager, mMine;

    public CommonBottomView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public CommonBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.common_bottom_view, this, true);
        setOrientation(VERTICAL);
        mHome = view.findViewById(R.id.bottom_home);
        mPublish = view.findViewById(R.id.bottom_publish);
        mPager = view.findViewById(R.id.bottom_pager);
        mMine = view.findViewById(R.id.bottom_mine);

        mHome.setOnClickListener(this);
        mPublish.setOnClickListener(this);
        mPager.setOnClickListener(this);
        mMine.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom_home:
                mContext.startActivity(new Intent(mContext, MainActivity.class));
                break;
            case R.id.bottom_publish:
                mContext.startActivity(new Intent(mContext, PublishCarSourceActivity.class));
                break;
            case R.id.bottom_pager:
                mContext.startActivity(new Intent(mContext, PublishCarSourceActivity.class));
                break;
            case R.id.bottom_mine:
                mContext.startActivity(new Intent(mContext, MyabcActivityShipper.class));
                break;
        }
    }
}
