package com.maogousoft.logisticsmobile.driver.activity.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.activity.other.OthersActivity;
import com.maogousoft.logisticsmobile.driver.activity.vip.ShopListActivity;
import com.maogousoft.logisticsmobile.driver.adapter.AdImageAdapter;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.widget.OneGallery;
import com.maogousoft.logisticsmobile.driver.widget.OneGalleryBottomView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ybxiang.driver.activity.HelpFindGoodsSourceActivity;
import com.ybxiang.driver.activity.MyFriendsActivity;
import com.ybxiang.driver.activity.PublishCarSourceActivity;
import com.ybxiang.driver.activity.SpreadActivity;

/**
 * 司机首页
 * 
 * @author ybxiang
 */
public class HomeDriverActivity extends BaseActivity {

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

}
