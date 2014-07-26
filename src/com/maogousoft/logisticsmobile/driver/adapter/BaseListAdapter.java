package com.maogousoft.logisticsmobile.driver.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.maogousoft.logisticsmobile.driver.MGApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 基础adapter
 * 
 * @author
 * @param <T>
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

	protected List<T> mList;

	protected Context mContext;

	protected ListView mListView;

	protected LayoutInflater mInflater;

	protected ImageLoader mImageLoader;

	protected MGApplication application;
	
	protected Resources mResources;

	public BaseListAdapter(Context context) {
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		application = (MGApplication) ((Activity) context).getApplication();
		mImageLoader = application.getImageLoader();
		mResources=context.getResources();
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mList == null ? 0 : position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	public void setList(List<T> list) {
		this.mList = list;
		notifyDataSetChanged();
	}

	public List<T> getList() {
		return this.mList;
	}

	public void add(T t) {
		if (mList == null) {
			mList = new ArrayList<T>();
		}
		mList.add(t);
		notifyDataSetChanged();
	}

	public void addAll(List<T> list) {
		if (mList == null) {
			mList = new ArrayList<T>();
		}
		if (list != null) {
			mList.addAll(list);
		}
		notifyDataSetChanged();
	}

	public void addAll(List<T> list, int position) {
		if (mList == null) {
			return;
		}
		mList.addAll(position, list);
		notifyDataSetChanged();
	}

	public void remove(int position) {
		if (mList != null) {
			mList.remove(position);
			notifyDataSetChanged();
		}
	}

	public void remove(T t) {
		if (mList != null) {
			mList.remove(t);
			notifyDataSetChanged();
		}
	}

	public void removeAll() {
		if (mList != null) {
			mList.removeAll(mList);
			notifyDataSetChanged();
		}
	}

	public void setListView(ListView listView) {
		this.mListView = listView;
	}

	public ListView getListView() {
		return this.mListView;
	}
}
