package com.maogousoft.logisticsmobile.driver.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.maogousoft.logisticsmobile.driver.model.MessageInfo;

public class DBUtils {

	private SQLiteDatabase sdb;

	public DBUtils(SQLiteDatabase sdb) {
		this.sdb = sdb;
	}

	/** 添加聊天消息 **/
	public boolean addMessage(MessageInfo messageInfo) {
		try {
			final ContentValues values = new ContentValues();
			values.put("msgId", messageInfo.getMsgId());
			values.put("msgFrom", messageInfo.getMsgFrom());
			values.put("msgTo", messageInfo.getMsgTo());
			values.put("msgType", messageInfo.getMsgType());
			values.put("msgTime", messageInfo.getMsgTime());
			values.put("audioTime", messageInfo.getAudioTime());
			values.put("msgContent", messageInfo.getMsgContent());
			values.put("msgState", messageInfo.getMsgState());
			values.put("creatUser", messageInfo.getCreateUser());
			return sdb.insert(DriverSqliteOpenHelper.MESSAGE_INFO, null, values) > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**修改消息状态,msgState,0已发送,1发送失败**/
	public boolean updateMsgState(long msgId,int msgState){
		try {
			final ContentValues values=new ContentValues();
			values.put("msgState",msgState);
			return sdb.update(DriverSqliteOpenHelper.MESSAGE_INFO, values, "msgId=?", new String[]{String.valueOf(msgId)})>0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**修改消息内容**/
	public boolean updateMsgContent(long msgId,String msgContent){
		try {
			final ContentValues values=new ContentValues();
			values.put("msgContent",msgContent);
			return sdb.update(DriverSqliteOpenHelper.MESSAGE_INFO, values, "msgId=?", new String[]{String.valueOf(msgId)})>0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/** 查询与对方的聊天记录 **/
	public List<MessageInfo> queryMessage(String msgFrom, String msgTo) {
		String sql = "select id,msgId,msgFrom,msgTo,msgType,msgTime,audioTime,msgContent,msgState,creatUser from " + DriverSqliteOpenHelper.MESSAGE_INFO + " where (msgFrom=? and msgTo=?) or (msgFrom=? and msgTo=?)";
		Cursor cursor = null;
		try {
			cursor = sdb.rawQuery(sql, new String[] { msgFrom, msgTo, msgTo, msgFrom });
			if (cursor != null) {
				List<MessageInfo> mList = new ArrayList<MessageInfo>();
				while (cursor.moveToNext()) {
					final MessageInfo messageInfo = new MessageInfo();
					messageInfo.setMsgId(cursor.getLong(1));
					messageInfo.setMsgFrom(cursor.getString(2));
					messageInfo.setMsgTo(cursor.getString(3));
					messageInfo.setMsgType(cursor.getInt(4));
					messageInfo.setMsgTime(cursor.getLong(5));
					messageInfo.setAudioTime(cursor.getInt(6));
					messageInfo.setMsgContent(cursor.getString(7));
					messageInfo.setMsgState(cursor.getInt(8));
					messageInfo.setCreateUser(cursor.getString(9));
					mList.add(messageInfo);
				}
				return mList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
	}
}
