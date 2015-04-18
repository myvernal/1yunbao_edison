package com.maogousoft.logisticsmobile.driver.activity.other;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.Constants;
import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;

/***
 * 帮助中心
 * 
 * @author lenovo
 */
public class HelpActivity extends BaseActivity {

	private WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_others_help);
		initViews();
		initData();
	}

	private void initViews() {

		((TextView) findViewById(R.id.titlebar_id_content)).setText("帮助中心");

		webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
	}

	private void initData() {
		webview.getSettings().setDefaultTextEncodingName("UTF-8");
		webview.loadUrl(Constants.HELP_SERVER_URL);
	}
}
