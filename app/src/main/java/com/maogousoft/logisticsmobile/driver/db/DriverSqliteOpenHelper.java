package com.maogousoft.logisticsmobile.driver.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 本地数据库
 * 
 * @author lenovo
 */
public class DriverSqliteOpenHelper extends SQLiteOpenHelper {

	// 数据库版本号
	private static int DATABASE_VERSION = 2;

	// 消息表
	public static final String MESSAGE_INFO = "messageinfo";

	// 创建聊天记录表
	final String CREATE_MESSAGE_INFO_TABLE = "CREATE TABLE IF NOT EXISTS " + MESSAGE_INFO + " (id INTEGER PRIMARY KEY AUTOINCREMENT,msgId Long,msgFrom,msgTo,msgType,msgTime,audioTime,msgContent,msgState,creatUser)";

	public DriverSqliteOpenHelper(Context context) {
		super(context, "driver.db", null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_MESSAGE_INFO_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			dropTable(db);
			onCreate(db);
		}
	}

	private void dropTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + MESSAGE_INFO);
	}

}
