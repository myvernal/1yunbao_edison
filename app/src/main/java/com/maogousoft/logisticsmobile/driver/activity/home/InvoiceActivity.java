package com.maogousoft.logisticsmobile.driver.activity.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.fragment.InvoiceFragment;
import com.maogousoft.logisticsmobile.driver.adapter.CommonFragmentPagerAdapter;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.widget.HeaderView;

import java.util.ArrayList;

/**
 * Created by aliang on 2015/4/25.
 */
public class InvoiceActivity extends BaseActivity {
    private ViewPager mPager;
    private HeaderView mHeaderView;
    private ArrayList<Fragment> fragmentList;
    private TextView view1, view2, view3;
    private View bottomMenu1, bottomMenu2;
    private InvoiceFragment firstFragment;
    private InvoiceFragment secondFragment;
    private InvoiceFragment thirdFragment;
    private int currIndex = 0;//当前页卡编号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_layout);
        mHeaderView = (HeaderView) findViewById(R.id.headerView);
        mHeaderView.setTitle(R.string.invoice_title);
        mHeaderView.setMoreTitle(R.string.invoice_tip_title);
        initView();
        initViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeStates(currIndex);
    }

    /*
         * 初始化标签名
         */
    public void initView() {
        view1 = (TextView) findViewById(R.id.tv_guid1);
        view2 = (TextView) findViewById(R.id.tv_guid2);
        view3 = (TextView) findViewById(R.id.tv_guid3);

        bottomMenu1 = findViewById(R.id.invoice_menu_1);
        bottomMenu2 = findViewById(R.id.invoice_menu_2);
        view1.setOnClickListener(this);
        view2.setOnClickListener(this);
        view3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_guid1:
                mPager.setCurrentItem(0);
                break;
            case R.id.tv_guid2:
                mPager.setCurrentItem(1);
                break;
            case R.id.tv_guid3:
                mPager.setCurrentItem(2);
                break;
            case R.id.menu_bottom1:
                //删除货单(司机)
                NewSourceInfo sourceInfo1 = firstFragment.getSelectedItem();
                if(sourceInfo1 == null) {
                    showMsg("请选择货单");
                } else {
                    showMsg("删除货单" + sourceInfo1.getId());
                }
                break;
            case R.id.menu_bottom2:
                //已订货单(司机)
                NewSourceInfo sourceInfo2 = firstFragment.getSelectedItem();
                if(sourceInfo2 == null) {
                    showMsg("请选择货单");
                } else {
                    showMsg("已定货单" + sourceInfo2.getId());
                }
                break;
            case R.id.menu_bottom3:
                //邀约货单(司机)
                NewSourceInfo sourceInfo3 = firstFragment.getSelectedItem();
                if(sourceInfo3 == null) {
                    showMsg("请选择货单");
                } else {
                    showMsg("邀约货单" + sourceInfo3.getId());
                }
                break;
            case R.id.menu_bottom4:
                //装车不成功货单(司机)
                NewSourceInfo sourceInfo4 = secondFragment.getSelectedItem();
                if(sourceInfo4 == null) {
                    showMsg("请选择货单");
                } else {
                    showMsg("装车不成功货单" + sourceInfo4.getId());
                }
                break;
            case R.id.menu_bottom5:
                //已装车货单(司机)
                NewSourceInfo sourceInfo5 = secondFragment.getSelectedItem();
                if(sourceInfo5 == null) {
                    showMsg("请选择货单");
                } else {
                    showMsg("已装车货单" + sourceInfo5.getId());
                }
                break;
            case R.id.menu_bottom6:
                //订单确认货单(司机)
                NewSourceInfo sourceInfo6 = secondFragment.getSelectedItem();
                if(sourceInfo6 == null) {
                    showMsg("请选择货单");
                } else {
                    showMsg("订单确认货单" + sourceInfo6.getId());
                }
                break;
        }
    }

    /*
         * 初始化ViewPager
         */
    public void initViewPager() {
        mPager = (ViewPager) findViewById(R.id.viewpager);
        fragmentList = new ArrayList<Fragment>();
        firstFragment = InvoiceFragment.newInstance(1, application.getUserType());
        secondFragment = InvoiceFragment.newInstance(2, application.getUserType());
        thirdFragment = InvoiceFragment.newInstance(3,application.getUserType());
        fragmentList.add(firstFragment);
        fragmentList.add(secondFragment);
        fragmentList.add(thirdFragment);

        //给ViewPager设置适配器
        mPager.setAdapter(new CommonFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        mPager.setCurrentItem(0);//设置当前显示标签页为第一页
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());//页面变化时的监听器
        mPager.setOffscreenPageLimit(3);
    }


    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageSelected(int position) {
            currIndex = position;
            changeStates(currIndex);
        }
    }

    private void changeStates(int position) {
        switch (position){
            case 0:
                view1.setSelected(true);
                view2.setSelected(false);
                view3.setSelected(false);
                bottomMenu1.setVisibility(View.VISIBLE);
                bottomMenu2.setVisibility(View.GONE);
                break;
            case 1:
                view1.setSelected(false);
                view2.setSelected(true);
                view3.setSelected(false);
                bottomMenu1.setVisibility(View.GONE);
                bottomMenu2.setVisibility(View.VISIBLE);
                break;
            case 2:
                view1.setSelected(false);
                view2.setSelected(false);
                view3.setSelected(true);
                bottomMenu1.setVisibility(View.GONE);
                bottomMenu2.setVisibility(View.GONE);
                break;
        }
    }
}
