package com.maogousoft.logisticsmobile.driver.activity.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseActivity;
import com.maogousoft.logisticsmobile.driver.widget.GestureImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 查看大图
 * 
 * @author lenovo
 */
public class ImageShowActivity extends BaseActivity {

	private GestureImageView mGestureImageView;

	private String imageUrl;

	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_imageshow);
		initViews();
		initData();
	}

	private void initViews() {
		options = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().displayer(new FadeInBitmapDisplayer(300))
				.build();

		((TextView) findViewById(R.id.titlebar_id_content))
				.setText(R.string.string_home_imageshow_title);
		mGestureImageView = (GestureImageView) findViewById(R.id.image);
	}

	private void initData() {
		if (getIntent().hasExtra("imageUrl")) {
			imageUrl = getIntent().getStringExtra("imageUrl");
			imageLoader.displayImage(imageUrl, mGestureImageView, options);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mGestureImageView != null) {
			imageLoader.cancelDisplayTask(mGestureImageView);
		}
	}
}
