package com.maogousoft.logisticsmobile.driver.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.fragment.InvoiceFragment;
import com.maogousoft.logisticsmobile.driver.activity.info.AgreementCarrierListActivity;
import com.maogousoft.logisticsmobile.driver.activity.info.TruckFailedReasonActivity;
import com.maogousoft.logisticsmobile.driver.adapter.CommonFragmentPagerAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.InvoiceNumberInfo;
import com.maogousoft.logisticsmobile.driver.model.NewSourceInfo;
import com.maogousoft.logisticsmobile.driver.widget.HeaderView;
import com.ybxiang.driver.activity.MySourceDetailActivity;
import com.ybxiang.driver.activity.PublishGoodsSourceActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aliang on 2015/4/25.
 */
public class InvoiceActivity extends BaseActivity {
    private ViewPager mPager;
    private HeaderView mHeaderView;
    private ArrayList<Fragment> fragmentList;
    private TextView view1, view2, view3;
    private View bottomMenu1, bottomMenu2, bottomMenu3;
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
        initData();
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
        bottomMenu3 = findViewById(R.id.invoice_menu_3);
        view1.setOnClickListener(this);
        view2.setOnClickListener(this);
        view3.setOnClickListener(this);
    }

    private void initData() {
        String action;
        if(application.getUserType() == Constants.USER_DRIVER) {
            action = Constants.QUERY_PENDING_SOURCE_COUNT;
        } else {
            action = Constants.QUERY_ORDER_COUNT;
        }
        //获取货单数量
        doAction(action, "", false, InvoiceNumberInfo.class, new ActionCallBack() {
            @Override
            public void onCallBack(Object result) {
                if (result instanceof InvoiceNumberInfo) {
                    InvoiceNumberInfo invoiceNumberInfo = (InvoiceNumberInfo) result;
                    view1.setText(getString(R.string.invoice_1, invoiceNumberInfo.getPending_order_count()));
                    view2.setText(getString(R.string.invoice_2, invoiceNumberInfo.getShipment_order_count()));
                    view3.setText(getString(R.string.invoice_3, invoiceNumberInfo.getHistory_order_count()));
                }
            }
        });
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
        }
    }

    /**
     * (司机)待定货单
     *
     * @param view
     */
    public void onBottomMenu1(View view) throws JSONException {
        final NewSourceInfo sourceInfo = firstFragment.getSelectedItem();
        if (sourceInfo == null) {
            showMsg("请选择货单");
            return;
        }
        JSONObject params = new JSONObject();
        params.put("order_id", sourceInfo.getId());
        switch (view.getId()) {
            case R.id.menu_bottom1:
                //删除货单(司机)
                doAction(Constants.DELETE_PENDING_ORDER, params.toString(), true, new ActionCallBack() {
                    @Override
                    public void onCallBack(Object result) {
                        firstFragment.removeDataAndNotifyDataChange(sourceInfo);
                    }
                });
                break;
            case R.id.menu_bottom2:
                //已订货单(司机)
                doAction(Constants.DONE_PENDING_ORDER, params.toString(), true, new ActionCallBack() {
                    @Override
                    public void onCallBack(Object result) {
                        firstFragment.removeDataAndNotifyDataChange(sourceInfo);
                    }
                });
                break;
            case R.id.menu_bottom3:
                //接受邀约货单(司机)
                if(TextUtils.equals("Y", sourceInfo.getIs_has_invite())) {
                    doAction(Constants.ACCEPT_CONTRACT_INVITE, params.toString(), true, new ActionCallBack() {
                        @Override
                        public void onCallBack(Object result) {
                            firstFragment.removeDataAndNotifyDataChange(sourceInfo);
                        }
                    });
                } else {
                    showMsg("该货单没有被邀约！");
                }
                break;
        }
    }

    /**
     * (司机)待装货单
     *
     * @param view
     */
    public void onBottomMenu2(View view) throws JSONException {
        final NewSourceInfo sourceInfo = secondFragment.getSelectedItem();
        if (sourceInfo == null) {
            showMsg("请选择货单");
            return;
        }
        JSONObject params = new JSONObject();
        params.put("order_id", sourceInfo.getId());
        switch (view.getId()) {
            case R.id.menu_bottom4:
                //装车不成功货单(通用)
                Intent intent = new Intent(mContext, TruckFailedReasonActivity.class);
                intent.putExtra(Constants.COMMON_KEY, sourceInfo);
                startActivity(intent);
                break;
            case R.id.menu_bottom5:
                //已装车货单(通用)
                doAction(Constants.TRUCK_LOADING_FINISH, params.toString(), true, new ActionCallBack() {
                    @Override
                    public void onCallBack(Object result) {
                        showMsg(result.toString());
                        //firstFragment.removeDataAndNotifyDataChange(sourceInfo);
                    }
                });
                break;
            case R.id.menu_bottom6:
                //订单确认货单(通用)
                if (TextUtils.equals("Y", sourceInfo.getIs_able_confim_contract())) {
                    doAction(Constants.USER_CONFIM_CONTRACT, params.toString(), true, new ActionCallBack() {
                        @Override
                        public void onCallBack(Object result) {
                            showMsg(result.toString());
                            firstFragment.removeDataAndNotifyDataChange(sourceInfo);
                        }
                    });
                } else {
                    showMsg("该货单还未完成！");
                }
                break;
        }
    }

    /**
     * (货主)待定货单
     *
     * @param view
     */
    public void onBottomMenu3(View view) throws JSONException {
        final NewSourceInfo sourceInfo = firstFragment.getSelectedItem();
        if (sourceInfo == null) {
            showMsg("请选择货单");
            return;
        }
        JSONObject params = new JSONObject();
        params.put("order_id", sourceInfo.getId());
        switch (view.getId()) {
            case R.id.menu_bottom7:
                //撤销货单(货主)
                doAction(Constants.CANCEL_ORDER, params.toString(), true, new ActionCallBack() {
                    @Override
                    public void onCallBack(Object result) {
                        firstFragment.removeDataAndNotifyDataChange(sourceInfo);
                    }
                });
                break;
            case R.id.menu_bottom8:
                //修改货单(货主)
                startActivity(new Intent(mContext, PublishGoodsSourceActivity.class).putExtra(Constants.COMMON_KEY, sourceInfo));
                break;
            case R.id.menu_bottom9:
                //推送货单(货主)
                doAction(Constants.PUSH_ORDER, params.toString(), true, new ActionCallBack() {
                    @Override
                    public void onCallBack(Object result) {
                        showMsg("推送请求已发送");
                    }
                });
                break;
            case R.id.menu_bottom10:
                //导入合同(货主)
                startActivity(new Intent(mContext, AgreementCarrierListActivity.class).putExtra(Constants.COMMON_KEY, sourceInfo));
                break;
        }
    }

    /**
     * 执行对应的操作
     *
     * @param action
     * @param params
     */
    private void doAction(String action, String params, final boolean hasTip, final ActionCallBack actionCallBack) {
        doAction(action, params, hasTip, null, actionCallBack);
    }

    private void doAction(String action, String params, final boolean hasTip, Class T, final ActionCallBack actionCallBack) {
        try {
            showProgress("正在处理");
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.ACTION, action);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, params);

            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    T, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            dismissProgress();
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (hasTip) {
                                        showMsg("操作成功");
                                    }
                                    actionCallBack.onCallBack(result);
                                    break;
                                default:
                                    showMsg(result.toString());
                                    break;
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
         * 初始化ViewPager
         */
    public void initViewPager() {
        mPager = (ViewPager) findViewById(R.id.viewpager);
        fragmentList = new ArrayList<Fragment>();
        firstFragment = InvoiceFragment.newInstance(1);
        secondFragment = InvoiceFragment.newInstance(2);
        thirdFragment = InvoiceFragment.newInstance(3);
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
        switch (position) {
            case 0:
                view1.setSelected(true);
                view2.setSelected(false);
                view3.setSelected(false);
                if (application.getUserType() == Constants.USER_DRIVER) {
                    bottomMenu1.setVisibility(View.VISIBLE);
                    bottomMenu3.setVisibility(View.GONE);
                } else {
                    bottomMenu3.setVisibility(View.VISIBLE);
                    bottomMenu1.setVisibility(View.GONE);
                }
                bottomMenu2.setVisibility(View.GONE);
                break;
            case 1:
                view1.setSelected(false);
                view2.setSelected(true);
                view3.setSelected(false);
                bottomMenu1.setVisibility(View.GONE);
                bottomMenu2.setVisibility(View.VISIBLE);
                bottomMenu3.setVisibility(View.GONE);
                break;
            case 2:
                view1.setSelected(false);
                view2.setSelected(false);
                view3.setSelected(true);
                bottomMenu1.setVisibility(View.GONE);
                bottomMenu2.setVisibility(View.GONE);
                bottomMenu3.setVisibility(View.GONE);
                break;
        }
    }

    interface ActionCallBack {
        public void onCallBack(Object result);
    }
}
