package com.example.z1310_000.sharedppx.request;

import com.example.z1310_000.sharedppx.utils.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by z1310_000 on 2017/10/8 0008.
 */

public class GetUseRecordRequest implements BaseRequest {
    private String TAG="GetUseRecord";

    @Override
    public String getUrl() {
        return "http://" + AppConfig.HOST + ":" + AppConfig.PORT
                + "/sharedPPX/" + TAG;
    }

    @Override
    public StringEntity getStringEntity() {
        return null;
    }

    public StringEntity getStringEntity(int uid) {
        StringEntity stringEntity=null;
        JSONObject json=new JSONObject();
        try {
            json.put("uid",uid);
            stringEntity=new StringEntity(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return stringEntity;
    }
}
