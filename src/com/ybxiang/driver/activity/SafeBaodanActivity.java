package com.ybxiang.driver.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by aliang on 2014/9/3.
 */
public class SafeBaodanActivity extends BaseActivity {

    private View loadingView;
    private ImageView imageView;
    private DisplayImageOptions options;
    private String url;
    private Button mTitleBarBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safe_baodan_layout);
        initViews();
        initData();
    }

    private void initViews() {
        loadingView = findViewById(R.id.loading);
        imageView = (ImageView) findViewById(R.id.image);
        ((TextView) findViewById(R.id.titlebar_id_content)).setText("电子保单");
        // 返回按钮生效
        mTitleBarBack = (Button) findViewById(R.id.titlebar_id_back);
        mTitleBarBack.setOnClickListener(this);
        // 更多按钮隐藏
        findViewById(R.id.titlebar_id_more).setVisibility(View.GONE);
    }

    private void initData() {
        url = getIntent().getStringExtra(Constants.COMMON_KEY);
        options = new DisplayImageOptions.Builder().resetViewBeforeLoading()
                .cacheOnDisc()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoader.getInstance().displayImage(url, imageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted() {

            }

            @Override
            public void onLoadingFailed(FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(Bitmap loadedImage) {
                loadingView.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled() {

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
    }
}
