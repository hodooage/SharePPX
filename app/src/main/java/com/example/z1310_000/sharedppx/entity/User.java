package com.example.z1310_000.sharedppx.entity;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.util.Date;

public class User extends DataSupport{
    //由于litepal操作数据库不支持自定义主键id,需要一个uid的字段来存储实际上的主键
    //@SerializedName这个注解用于gson将json转换成对象时将json中的“id”字段赋值给uid
    @SerializedName("id")
    private int uid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.phonenum
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    private String phonenum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.pwd
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    private String pwd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.nickname
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    private String nickname;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.email
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    private String email;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.sex
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    private String sex;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.birthday
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    private String birthday;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.enabled
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    private Integer enabled;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.image
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    private String image;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.last_login_time
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    private Date lastLoginTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.register_time
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    private Date registerTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.balance
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    private Double balance;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user.points
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    private Integer points;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.phonenum
     *
     * @return the value of user.phonenum
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public String getPhonenum() {
        return phonenum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.phonenum
     *
     * @param phonenum the value for user.phonenum
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.pwd
     *
     * @return the value of user.pwd
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.pwd
     *
     * @param pwd the value for user.pwd
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.nickname
     *
     * @return the value of user.nickname
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.nickname
     *
     * @param nickname the value for user.nickname
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.email
     *
     * @return the value of user.email
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public String getEmail() {
        return email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.email
     *
     * @param email the value for user.email
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.sex
     *
     * @return the value of user.sex
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public String getSex() {
        return sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.sex
     *
     * @param sex the value for user.sex
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.birthday
     *
     * @return the value of user.birthday
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.birthday
     *
     * @param birthday the value for user.birthday
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.enabled
     *
     * @return the value of user.enabled
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public Integer getEnabled() {
        return enabled;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.enabled
     *
     * @param enabled the value for user.enabled
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.image
     *
     * @return the value of user.image
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public String getImage() {
        return image;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.image
     *
     * @param image the value for user.image
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.last_login_time
     *
     * @return the value of user.last_login_time
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.last_login_time
     *
     * @param lastLoginTime the value for user.last_login_time
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.register_time
     *
     * @return the value of user.register_time
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public Date getRegisterTime() {
        return registerTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.register_time
     *
     * @param registerTime the value for user.register_time
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.balance
     *
     * @return the value of user.balance
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.balance
     *
     * @param balance the value for user.balance
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user.points
     *
     * @return the value of user.points
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public Integer getPoints() {
        return points;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user.points
     *
     * @param points the value for user.points
     *
     * @mbggenerated Tue Feb 06 14:54:22 CST 2018
     */
    public void setPoints(Integer points) {
        this.points = points;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", phonenum='" + phonenum + '\'' +
                ", pwd='" + pwd + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", enabled=" + enabled +
                ", image='" + image + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", registerTime=" + registerTime +
                ", balance=" + balance +
                ", points=" + points +
                '}';
    }
}