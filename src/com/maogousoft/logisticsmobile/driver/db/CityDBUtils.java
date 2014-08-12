package com.maogousoft.logisticsmobile.driver.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.maogousoft.logisticsmobile.driver.R;
import com.maogousoft.logisticsmobile.driver.model.CityInfo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * 城市数据库操作工具类
 * 
 * @author lenovo
 */
public class CityDBUtils {

	// 表名
	private final String tableName = "place";
	private final String[] columns = { "Id", "ParentId", "Name", "ShortName",
			"Deep" };

	private SQLiteDatabase sdb;

	public CityDBUtils(SQLiteDatabase _sdb) {
		this.sdb = _sdb;
	}

	/** 查询城市信息 **/
	public String getCityInfo(int arg0, int arg1, int arg2) {
		Cursor cursor = null;
		StringBuffer buffer = new StringBuffer();
		String[] cols = { "Id", "Name" };
		try {
            if(arg0 > 0) {
                cursor = sdb.query(tableName, cols, "Id=" + arg0, null, null, null,
                        null);
                if (cursor.moveToFirst()) {
                    buffer.append(cursor.getString(1));
                }
            }
            if(arg1 > 0) {
                cursor = sdb.query(tableName, cols, "Id=" + arg1, null, null, null,
                        null);
                if (cursor.moveToFirst()) {
                    buffer.append(cursor.getString(1));
                }
            }
            if(arg2 > 0) {
                cursor = sdb.query(tableName, cols, "Id=" + arg2, null, null, null,
                        null);
                if (cursor.moveToFirst()) {
                    buffer.append(cursor.getString(1));
                }
            }
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return "";
	}

	/** 查询城市信息 **/
	public String getCityInfo(int arg0) {
		Cursor cursor = null;
		StringBuffer buffer = new StringBuffer();
		String[] cols = { "Id", "Name" };
		try {
			cursor = sdb.query(tableName, cols, "Id=" + arg0, null, null, null,
					null);
			if (cursor.moveToFirst()) {
				buffer.append(cursor.getString(1));
			}
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return "";
	}

	/** 获取一级城市列表 **/
	public List<CityInfo> getFirstCity() {
		Cursor cursor = null;
		try {
			cursor = sdb.query(tableName, columns, "Deep=1", null, null, null,
					null);
			if (cursor == null) {
				return null;
			}
			List<CityInfo> mList = new ArrayList<CityInfo>();
			while (cursor.moveToNext()) {
				final CityInfo cityInfo = new CityInfo();
				cityInfo.setId(cursor.getInt(0));
				cityInfo.setParentId(cursor.getInt(1));
				cityInfo.setName(cursor.getString(2));
				cityInfo.setShortName(cursor.getString(3));
				cityInfo.setDeep(cursor.getInt(4));
				mList.add(cityInfo);
			}
			return mList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 获取二级城市列表 **/
	public List<CityInfo> getSecondCity(int parentId) {
		Cursor cursor = null;
		try {
			cursor = sdb
					.query(tableName, columns, "Deep=2 and ParentId=?",
							new String[] { String.valueOf(parentId) }, null,
							null, null);
			if (cursor == null) {
				return null;
			}
			List<CityInfo> mList = new ArrayList<CityInfo>();
			while (cursor.moveToNext()) {
				final CityInfo cityInfo = new CityInfo();
				cityInfo.setId(cursor.getInt(0));
				cityInfo.setParentId(cursor.getInt(1));
				cityInfo.setName(cursor.getString(2));
				cityInfo.setShortName(cursor.getString(3));
				cityInfo.setDeep(cursor.getInt(4));
				mList.add(cityInfo);
			}
			return mList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** 获取三级城市列表 **/
	public List<CityInfo> getThridCity(int parentId) {
		Cursor cursor = null;
		try {
			cursor = sdb
					.query(tableName, columns, "Deep=3 and ParentId=?",
							new String[] { String.valueOf(parentId) }, null,
							null, null);
			if (cursor == null) {
				return null;
			}
			List<CityInfo> mList = new ArrayList<CityInfo>();
			while (cursor.moveToNext()) {
				final CityInfo cityInfo = new CityInfo();
				cityInfo.setId(cursor.getInt(0));
				cityInfo.setParentId(cursor.getInt(1));
				cityInfo.setName(cursor.getString(2));
				cityInfo.setShortName(cursor.getString(3));
				cityInfo.setDeep(cursor.getInt(4));
				mList.add(cityInfo);
			}
			return mList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取线路
	 * 
	 * @param startProvince
	 * @param startCity
	 * @param endProvince
	 * @param endCity
	 * @return
	 */
	public String getStartEndStr(int startProvince, int startCity,
			int endProvince, int endCity) {
		String reuslt = "";

		if (startProvince == 0) {
			startProvince = -1;
		}

		if (startCity == 0) {
			startCity = -1;
		}
		if (endProvince == 0) {
			endProvince = -1;
		}
		if (endCity == 0) {
			endCity = -1;
		}

		String start = getCityInfo(startProvince, startCity, -1);
		String end = getCityInfo(endProvince, endCity, -1);

		if (TextUtils.isEmpty(start) && TextUtils.isEmpty(end)) {
			reuslt = "无";
		} else {
			reuslt = start + "-" + end;
		}

		return reuslt;
	}

}
