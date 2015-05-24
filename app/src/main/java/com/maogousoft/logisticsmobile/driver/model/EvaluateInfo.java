package com.maogousoft.logisticsmobile.driver.model;

/****
 * 评价信息
 * 
 * @author admin
 * 
 */
public class EvaluateInfo {
    private int id;//评论ID
    private int user_id;//货主编号
    private int driver_id;//评论司机编号
    private String score1;//评价分1
    private String score2;//评价分2
    private String score3;//评价分3
    private String reply_content;//评价内容
    private long reply_time;//评论时间
    private String return_content;//回复内容
    private long return_time;//回复时间
    private String driver_name;//评论的司机的名称
    private String user_name;//评论的货主的名称
    private String car_photo;//评论的司机的头像
    private String company_logo;//评论的货主的头像

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

    public String getScore1() {
        return score1;
    }

    public void setScore1(String score1) {
        this.score1 = score1;
    }

    public String getScore2() {
        return score2;
    }

    public void setScore2(String score2) {
        this.score2 = score2;
    }

    public String getScore3() {
        return score3;
    }

    public void setScore3(String score3) {
        this.score3 = score3;
    }

    public String getReply_content() {
        return reply_content;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }

    public long getReply_time() {
        return reply_time;
    }

    public void setReply_time(long reply_time) {
        this.reply_time = reply_time;
    }

    public String getReturn_content() {
        return return_content;
    }

    public void setReturn_content(String return_content) {
        this.return_content = return_content;
    }

    public long getReturn_time() {
        return return_time;
    }

    public void setReturn_time(long return_time) {
        this.return_time = return_time;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCar_photo() {
        return car_photo;
    }

    public void setCar_photo(String car_photo) {
        this.car_photo = car_photo;
    }

    public String getCompany_logo() {
        return company_logo;
    }

    public void setCompany_logo(String company_logo) {
        this.company_logo = company_logo;
    }
}
