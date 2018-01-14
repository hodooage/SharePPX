package com.example.z1310_000.sharedppx.request;

import com.example.z1310_000.sharedppx.utils.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by z1310_000 on 2017/9/16 0016.
 */

public class StartDriveXiaRequest implements BaseRequest {
    private String TAG="startDriveXia";

    @Override
    public String getUrl() {
        return "http://" + AppConfig.HOST + ":" + AppConfig.PORT
                + "/sharedPPX/" + TAG;
    }

    @Override
    public StringEntity getStringEntity() {


        return null;
    }


    public StringEntity getStringEntity(int u_id,int x_id) {
        JSONObject json=new JSONObject();
        StringEntity stringEntity=null;
        try {
            json.put("u_id",u_id);
            json.put("x_id",x_id);
            stringEntity=new StringEntity(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return stringEntity;
    }

}
