package com.maogousoft.logisticsmobile.driver.adapter;

import java.util.List;

import com.maogousoft.logisticsmobile.driver.activity.home.ImageShowFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * 图片切换适配器
 * @author lenovo
 *
 */
public class ImagePagerAdapter extends FragmentStatePagerAdapter {

	private List<String> mList;

	public void setList(List<String> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	public ImagePagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		return ImageShowFragment.newInstance(mList.get(arg0));
	}

	@Override
	public int getCount() {
		return mList==null?0:mList.size();
	}

}
