package com.ybxiang.driver.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;

import java.io.*;

/**
 * Created by aliang on 2014/9/16.
 */
public class BaoxianDescActivity extends BaseActivity {

    private WebView webView;
    private Button mTitleBarBack, mTitleBarMore;
    private final String mimetype = "text/html";
    private final String encoding = "utf-8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baoxian_desc_layout);
        initViews();
        initData();
    }

    private void initViews() {
        mTitleBarBack = (Button) findViewById(R.id.titlebar_id_back);
        mTitleBarMore = (Button) findViewById(R.id.titlebar_id_more);
        mTitleBarMore.setVisibility(View.GONE);
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");

        mTitleBarBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    private void initData() {
        int safeType = getIntent().getIntExtra(Constants.COMMON_KEY, Constants.SAFE_CPIC);
        int resourceId = R.raw.cpic;
        if(Constants.SAFE_CPIC != safeType) {
            resourceId = R.raw.pingan;
            ((TextView) findViewById(R.id.titlebar_id_content)).setText("平安保险协议");
        } else {
            ((TextView) findViewById(R.id.titlebar_id_content)).setText("太平洋保险协议");
        }
        final int finalResourceId = resourceId;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String desc = readFile(finalResourceId);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadDataWithBaseURL(null, desc, mimetype, encoding, null);
                    }
                });
            }
        }).start();
    }

    private String readFile(int resourceId) {
        Resources res = this.getResources();
        InputStream in = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        sb.append("");
        try {
            in = res.openRawResource(resourceId);
            String str;
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((str = br.readLine()) != null) {
                sb.append(str);
                sb.append("<br>");
            }
        } catch (Resources.NotFoundException e) {
            Toast.makeText(this, "保险协议不存在", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, "保险协议编码出现异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, "保险协议读取错误", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
