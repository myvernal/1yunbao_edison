package com.maogousoft.logisticsmobile.driver.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.home.MyabcActivityDriver;
import com.maogousoft.logisticsmobile.driver.activity.home.MyabcActivityShipper;
import com.maogousoft.logisticsmobile.driver.adapter.AdImageAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.AdvertInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.widget.OneGallery;
import com.maogousoft.logisticsmobile.driver.widget.OneGalleryBottomView;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by EdisonZhao on 14-8-11.
 * Email:zhaoliangyu@sobey.com
 */
public class BaseHomeActivity extends  BaseActivity {

    private static final String TAG = "BaseHomeActivity";
    private OneGallery gallery;
    private AdImageAdapter adapter = null;
    private String[] adUrlArray;
    private long clickTime = System.currentTimeMillis();
    private static final long clickDuration = 1000L;
    private ImageLoader mImageLoader;
    private OneGalleryBottomView oneGalleryBottomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageLoader = ((MGApplication) getApplication()).getImageLoader();
        adUrlArray = new String[]{"" + R.drawable.top_ad_1, "" + R.drawable.top_ad_2, "" + R.drawable.top_ad_3};
        initViews();
        getAdvertList();
    }

    private void initViews() {
        gallery = (OneGallery) findViewById(R.id.top_ad);
        oneGalleryBottomView = (OneGalleryBottomView) findViewById(R.id.top_ad_bottom);
        initGallery(mContext);
    }

    private void initGallery(final Context context) {
        adapter = new AdImageAdapter(context, adUrlArray, mImageLoader);
        gallery.setAdapter(adapter);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if ((System.currentTimeMillis() - clickTime) < clickDuration) {
                    return;
                }
                clickTime = System.currentTimeMillis();
            }
        });

        if (adapter.getDrawableCount() > 0) {
            gallery.setSelection(0);
        }

        gallery.setAutoScroll(true);
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //顶部广告图片切换
                oneGalleryBottomView.setHighLight(position % 3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    /**
     * 车主我的设置
     * @param view
     */
    public void onSettingDriver(View view) {
        Intent intent = new Intent(this,MyabcActivityDriver.class);
        startActivity(intent);
    }

    /**
     * 货主我的设置
     * @param view
     */
    public void onSettingShipper(View view) {
        Intent intent = new Intent(this,MyabcActivityShipper.class);
        startActivity(intent);
    }

    // 获取我的广告信息
    private void getAdvertList() {

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.ACTION, Constants.QUERY_ADVERT_LIST);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("userType", application.getUserType()).toString());
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    AdvertInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result != null) {
                                        List<AdvertInfo> list = (List<AdvertInfo>) result;
                                        LogUtil.d(TAG, "list:" + list.size());
                                        for (int i = 0; i < list.size(); i++) {
                                            if (i <= 2) {
                                                AdvertInfo ad = list.get(i);
                                                if (ad.getStatus() == 0) {
                                                    adUrlArray[i] = ad.getAd_img();
                                                }
                                            }
                                        }
                                        adapter.setNewAdList(adUrlArray);
                                    }
                                    break;
                                case ResultCode.RESULT_ERROR:
                                    // if (result != null)
                                    // showMsg(result.toString());
                                    break;
                                case ResultCode.RESULT_FAILED:
                                    // if (result != null)
                                    // showMsg(result.toString());
                                    break;

                                default:
                                    break;
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
