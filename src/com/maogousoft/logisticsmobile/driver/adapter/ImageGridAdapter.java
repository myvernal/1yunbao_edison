package com.maogousoft.logisticsmobile.driver.adapter;

import com.maogousoft.logisticsmobile.driver.R;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.AbsListView.LayoutParams;

/**
 * 九宫格图片展示
 * 
 * @author lenovo
 */
public class ImageGridAdapter extends BaseListAdapter<String> {

	private DisplayMetrics mDisplayMetrics;

	private int height = 0;

	private LayoutParams lp;

	public ImageGridAdapter(Context context) {
		super(context);
		mDisplayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		height = (mDisplayMetrics.widthPixels - 5 * context.getResources().getDimensionPixelSize(R.dimen.grid_space)) / 4;
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, height);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.griditem_image, parent, false);
		}
		if (mList == null || position >= mList.size()) {
			((ImageView) convertView).setImageResource(R.drawable.ic_img_loading);
		} else {
			mImageLoader.displayImage(mList.get(position), (ImageView) convertView);
		}

		convertView.setLayoutParams(lp);
		return convertView;
	}
}
