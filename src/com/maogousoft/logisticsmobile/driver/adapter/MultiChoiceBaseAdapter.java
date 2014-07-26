package com.maogousoft.logisticsmobile.driver.adapter;

import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

public class MultiChoiceBaseAdapter extends SimpleCursorAdapter {

	private MultiChoiceAdapterHelper helper = new MultiChoiceAdapterHelper(this) {

		@Override
		protected long positionToSelectionHandle(int position) {
			return getItemId(position);
		}
	};

	public MultiChoiceBaseAdapter(Bundle savedInstanceState, Context context, int layout, Cursor cursor, String[] from, int[] to, int flags) {
		super(context, layout, cursor, from, to, flags);
		helper.restoreSelectionFromSavedInstanceState(savedInstanceState);
	}

	public void setAdapterView(AdapterView<? super BaseAdapter> adapterView) {
		helper.setAdapterView(adapterView);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		helper.setOnItemClickListener(listener);
	}

	public void save(Bundle outState) {
		helper.save(outState);
	}

	public void select(long itemId, boolean selected) {
		helper.select(itemId, selected);
	}

	public void select(long itemId) {
		helper.select(itemId);
	}

	public void unselect(long itemId) {
		helper.unselect(itemId);
	}

	public Set<Long> getSelection() {
		return helper.getSelection();
	}

	public int getSelectionCount() {
		return helper.getSelectionCount();
	}

	public boolean isSelected(long itemId) {
		return helper.isSelected(itemId);
	}

	protected void finishActionMode() {
		helper.finishActionMode();
	}

	protected Context getContext() {
		return helper.getContext();
	}

	@Override
	public final View getView(int position, View convertView, ViewGroup parent) {
		View viewWithoutSelection = super.getView(position, convertView, parent);
		return helper.getView(position, viewWithoutSelection);
	}
}
