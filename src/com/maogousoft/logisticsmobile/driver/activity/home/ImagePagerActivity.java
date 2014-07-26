package com.maogousoft.logisticsmobile.driver.activity.home;

import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.TextView;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.activity.BaseFragmentActivity;
import com.maogousoft.logisticsmobile.driver.adapter.ImagePagerAdapter;

/**
 * 多张图片浏览
 * 
 * @author admin
 */
public class ImagePagerActivity extends BaseFragmentActivity {

	private Button mBack;

	private ViewPager mViewPager;

	private ImagePagerAdapter mAdapter;

	private List<String> images;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_imagepager);
		initViews();
		initData();
	}

	// 初始化视图控件
	private void initViews() {
		mBack = (Button) findViewById(R.id.titlebar_id_back);
		((TextView) findViewById(R.id.titlebar_id_content)).setText("图片详情");
		mViewPager = (ViewPager) findViewById(R.id.home_imagepager_viewpager);

		mBack.setOnClickListener(this);
	}

	private void initData() {
		mAdapter = new ImagePagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		if (getIntent().hasExtra("images")) {
			images = getIntent().getStringArrayListExtra("images");
			mAdapter.setList(images);
		}
	}
}
