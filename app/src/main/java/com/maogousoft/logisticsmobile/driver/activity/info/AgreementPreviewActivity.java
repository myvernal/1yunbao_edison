package com.maogousoft.logisticsmobile.driver.activity.info;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.utils.LogUtil;
import com.maogousoft.logisticsmobile.driver.widget.HeaderView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * Created by EdisonZhao on 15/5/19.
 */
public class AgreementPreviewActivity extends BaseActivity {

    private View loadingView;
    private ImageView imageView;
    private WebView mWebView;
    private DisplayImageOptions options;
    private String url;
    private HeaderView mHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_preview_layout);
        mHeaderView = (HeaderView) findViewById(R.id.headerView);
        mHeaderView.setTitle(R.string.agreement_title);
        initView();
        initData();
    }

    private void initView() {
        loadingView = findViewById(R.id.loading);
        imageView = (ImageView) findViewById(R.id.image);
        mWebView = (WebView) findViewById(R.id.webView);
    }

    private void initData() {
        url = getIntent().getStringExtra(Constants.COMMON_KEY);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        /*自适应屏幕*/
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        /*支持缩放*/
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        /*可获取焦点*/
        mWebView.requestFocusFromTouch();
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);
        /*mWebView.addJavascriptInterface(new Object() {
            public void clickOnAndroid() {
                mHandler.post(new Runnable() {
                    public void run() {
                        webview.loadUrl("javascript:wave()");
                    }
                });
            }
        }, "demo");*/

        /*
        imageView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);*/
        mWebView.loadUrl(url);
    }

    WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mHeaderView.setTitle(title);
        }

    };

    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            LogUtil.d(TAG, "onPageFinished");
            //结束
            super.onPageFinished(view, url);
            loadingView.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //开始
            super.onPageStarted(view, url, favicon);
            LogUtil.d(TAG, "onPageFinished");
        }
    };
}
