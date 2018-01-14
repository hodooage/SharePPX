package com.example.z1310_000.sharedppx.request;

import com.example.z1310_000.sharedppx.utils.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by z1310_000 on 2017/9/20 0020.
 */

public class EditUserInformationRequest implements BaseRequest {
    private int id;

    private String nickname,phonenum,sex,email,birthday,image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String TAG="editUserInformation";
    @Override
    public String getUrl() {
        return "http://" + AppConfig.HOST + ":" + AppConfig.PORT
                + "/sharedPPX/" + TAG;
    }

    @Override
    public StringEntity getStringEntity() {
        StringEntity stringEntity=null;

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("id",id);

            if(null!=nickname){
                jsonObject.put("nickname",nickname);
            }
            if(null!=phonenum){
                jsonObject.put("phonenum",phonenum);
            }
            if(null!=birthday){
                jsonObject.put("birthday",birthday);
            }
            if(null!=email){
                jsonObject.put("email",email);
            }
            if(null!=sex){
                jsonObject.put("sex",sex);
            }
            if(null!=image){
                jsonObject.put("image",image);
            }

            stringEntity=new StringEntity(jsonObject.toString(),"utf-8");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }
}
