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
import com.ybxiang.driver.activity.CheckSafeActivity;
import com.ybxiang.driver.activity.FindCarActivity;
import com.ybxiang.driver.activity.MyCarsActivity;
import com.ybxiang.driver.activity.MyFriendsActivity;
import com.ybxiang.driver.activity.PublishGoodsSourceActivity;
import com.ybxiang.driver.activity.SpreadActivity;

/**
 * 三方首页
 * 
 * @author ybxiang
 */
public class HomeShipperActivity extends BaseActivity {

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
		LogUtil.i("wst", "HomeActivity Shipper -onCreate");
		setContentView(R.layout.activity_home_new_shipper);
        mImageLoader = ((MGApplication)getApplication()).getImageLoader();
        adUrlArray = new String[]{""+ R.drawable.shipper_top_ad1, ""+ R.drawable.shipper_top_ad2, ""+ R.drawable.shipper_top_ad3};
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

	// 我的车队
	public void onMyCars(View view) {
		startActivity(new Intent(context, MyCarsActivity.class));
	}

	// 我要推广
	public void onSpread(View view) {
		startActivity(new Intent(context, SpreadActivity.class));
	}

	// 我的好友
	public void onMyFriends(View view) {
		startActivity(new Intent(context, MyFriendsActivity.class));
	}

	// 发布货源
	public void onPublishGoods(View view) {
		startActivity(new Intent(context, PublishGoodsSourceActivity.class));
	}

	// 查找车源
	public void onFindCar(View view) {
		startActivity(new Intent(context, FindCarActivity.class));
	}

	// 验证保险
	public void onCheckSafe(View view) {
		startActivity(new Intent(context, CheckSafeActivity.class));
	}

	// 会员特权
	public void onVIP(View view) {
		startActivity(new Intent(context, ShopListActivity.class));
	}

	// 物流点评
	public void onInteraction(View view) {
		startActivity(new Intent(context, OthersActivity.class));
	}

    // 查找货源
    public void onSearchSource(View view) {
        startActivity(new Intent(context, SearchSourceActivity.class));
    }

	@Override
	public void onBackPressed() {
		exitAppHint();
	}
}
