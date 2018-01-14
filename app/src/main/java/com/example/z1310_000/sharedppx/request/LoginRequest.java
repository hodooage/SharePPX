package com.example.z1310_000.sharedppx.request;

import com.example.z1310_000.sharedppx.utils.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by z1310_000 on 2017/9/6 0006.
 */

public class LoginRequest implements BaseRequest {
    private String TAG = "login";

    @Override
    public String getUrl() {
         return "http://" + AppConfig.HOST + ":" + AppConfig.PORT
                + "/sharedPPX/" + TAG;
    }

    @Override
    public StringEntity getStringEntity() {
        return null;
    }

    public StringEntity getStringEntity(String phonenum,String password){
        JSONObject json = new JSONObject();
        StringEntity stringEntity = null;

        try {
            json.put("phonenum", phonenum);
            json.put("password", password);

            stringEntity = new StringEntity(json.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return stringEntity;
    }
}
