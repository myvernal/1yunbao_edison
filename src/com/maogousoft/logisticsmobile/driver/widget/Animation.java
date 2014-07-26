package com.maogousoft.logisticsmobile.driver.widget;

public interface Animation {

	/**
	 * Transforms the view.
	 * 
	 * @param view
	 * @param time
	 * @return true if this animation should remain active. False otherwise.
	 */
	public boolean update(GestureImageView view, long time);

}
