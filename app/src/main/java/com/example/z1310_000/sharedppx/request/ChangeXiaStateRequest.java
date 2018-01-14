package com.example.z1310_000.sharedppx.request;

import com.example.z1310_000.sharedppx.utils.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by z1310_000 on 2017/9/27 0027.
 */

public class ChangeXiaStateRequest implements BaseRequest {
    private String TAG="ChangeXiaStateRequest";


    @Override
    public String getUrl() {
        return "http://" + AppConfig.HOST + ":" + AppConfig.PORT
                + "/sharedPPX/" + TAG;
    }

    @Override
    public StringEntity getStringEntity() {
        return null;
    }

    //只更改状态
    public StringEntity getStringEntity(int xid,int state) {
        JSONObject json=new JSONObject();
        StringEntity stringEntity=null;
        try {
            json.put("xid",xid);
            json.put("state",state);
            stringEntity=new StringEntity(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }

    //更改状态和位置
    public StringEntity getStringEntity(int xid,int state,double latitude,double longitude) {
        JSONObject json=new JSONObject();
        StringEntity stringEntity=null;
        try {
            json.put("xid",xid);
            json.put("state",state);
            json.put("latitude",latitude);
            json.put("longitude",longitude);
            stringEntity=new StringEntity(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }
}
