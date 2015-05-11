package com.maogousoft.logisticsmobile.driver.model;

/****
 * 评价信息
 * 
 * @author admin
 * 
 */
public class EvaluateInfo {
    private int id;
    private int user_id;
    private int driver_id;
    private String driver_name;
    private long reply_time = -1;
	private String reply_content = "";
    private String return_content = "";
    private long return_time = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public long getReply_time() {
        return reply_time;
    }

    public void setReply_time(long reply_time) {
        this.reply_time = reply_time;
    }

    public String getReply_content() {
        return reply_content;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }

    public String getReturn_content() {
        return return_content;
    }

    public void setReturn_content(String return_content) {
        this.return_content = return_content;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public long getReturn_time() {
        return return_time;
    }

    public void setReturn_time(long return_time) {
        this.return_time = return_time;
    }
}
