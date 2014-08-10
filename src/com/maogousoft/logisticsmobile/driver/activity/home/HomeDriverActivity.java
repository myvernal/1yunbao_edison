package com.maogousoft.logisticsmobile.driver.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.other.OthersActivity;
import com.maogousoft.logisticsmobile.driver.activity.vip.ShopListActivity;
import com.maogousoft.logisticsmobile.driver.adapter.AdImageAdapter;
import com.maogousoft.logisticsmobile.driver.api.AjaxCallBack;
import com.maogousoft.logisticsmobile.driver.api.ApiClient;
import com.maogousoft.logisticsmobile.driver.api.ResultCode;
import com.maogousoft.logisticsmobile.driver.model.AdvertInfo;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.widget.OneGallery;
import com.maogousoft.logisticsmobile.driver.widget.OneGalleryBottomView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybxiang.driver.activity.HelpFindGoodsSourceActivity;
import com.ybxiang.driver.activity.MyFriendsActivity;
import com.ybxiang.driver.activity.PublishCarSourceActivity;
import com.ybxiang.driver.activity.SpreadActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 司机首页
 * 
 * @author ybxiang
 */
public class HomeDriverActivity extends BaseActivity {

    private static final String TAG = "HomeDriverActivity";
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
		LogUtil.i("wst", "HomeActivity driver -onCreate");
		setContentView(R.layout.activity_home_new_driver1);
        mImageLoader = ((MGApplication)getApplication()).getImageLoader();
        adUrlArray = new String[]{""+ R.drawable.top_ad_1, ""+ R.drawable.top_ad_2, ""+ R.drawable.top_ad_3};
        initViews();
        getAdvertList();
	}

    private void initViews() {
        gallery = (OneGallery) findViewById(R.id.top_ad);
        oneGalleryBottomView = (OneGalleryBottomView) findViewById(R.id.top_ad_bottom);
        initGallery(context);
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

	// 查找货源
	public void onSearchSource(View view) {
		startActivity(new Intent(context, SearchSourceActivity.class));
	}

	// 帮我找货
	public void onHelpFindGoods(View view) {
		startActivity(new Intent(context, HelpFindGoodsSourceActivity.class));
	}

	// 我的好友
	public void onMyFriends(View view) {
		startActivity(new Intent(context, MyFriendsActivity.class));
	}

	// 新货源
	public void onNewSource(View view) {
		Intent intentNewSource = new Intent(context, NewSourceActivity.class);
		intentNewSource.putExtra("isFromHomeActivity", true);
		startActivity(intentNewSource);
	}

	// 好友货源
	public void onFriendsSource(View view) {
		Intent intentNewSource = new Intent(context, NewSourceActivity.class);
		intentNewSource.putExtra("getFriendOrderList", true);
		startActivity(intentNewSource);
	}

    // 关注货源
    public void onFocusSource(View view) {
        Intent intentNewSource = new Intent(context, NewSourceActivity.class);
        intentNewSource.putExtra("QUERY_MAIN_LINE_ORDER", true);
        startActivity(intentNewSource);
    }

	// 发布车源
	public void onPublishCar(View view) {
		startActivity(new Intent(context, PublishCarSourceActivity.class));
	}

	// 我的推广
	public void onSpread(View view) {
		startActivity(new Intent(context, SpreadActivity.class));
	}

    //货运名片
    public void onCard(View view) {
        startActivity(new Intent(context, MyBusinessCard.class));
    }

	// 物流园区
	public void onVIP(View view) {
		startActivity(new Intent(context, ShopListActivity.class));
	}

	// 互动
	public void onInteraction(View view) {
		startActivity(new Intent(context, OthersActivity.class));
	}

	@Override
	public void onBackPressed() {
		exitAppHint();
	}

    // 获取我的广告信息
    private void getAdvertList() {

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.ACTION, Constants.QUERY_ADVERT_LIST);
            jsonObject.put(Constants.TOKEN, application.getToken());
            jsonObject.put(Constants.JSON, new JSONObject().put("userType", 2).toString());
            ApiClient.doWithObject(Constants.DRIVER_SERVER_URL, jsonObject,
                    AdvertInfo.class, new AjaxCallBack() {

                        @Override
                        public void receive(int code, Object result) {
                            switch (code) {
                                case ResultCode.RESULT_OK:
                                    if (result != null) {
                                        List<AdvertInfo> list = (List<AdvertInfo>) result;
                                        LogUtil.d(TAG, "list:" + list.size());
                                        for(int i=0;i<list.size();i++) {
                                            if(i <= 2) {
                                                AdvertInfo ad = list.get(i);
                                                if(ad.getStatus() == 0) {
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
