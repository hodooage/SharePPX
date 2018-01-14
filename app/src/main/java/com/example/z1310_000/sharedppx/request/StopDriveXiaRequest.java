package com.example.z1310_000.sharedppx.request;

import com.example.z1310_000.sharedppx.utils.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by z1310_000 on 2017/9/27 0027.
 */

public class StopDriveXiaRequest implements BaseRequest {
    private String TAG="stopDriveXia";

    private String stopSite,stopTime,duration;

    private double totalMoney;

    private int useRecordId;

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getStopSite() {
        return stopSite;
    }

    public void setStopSite(String stopSite) {
        this.stopSite = stopSite;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getUseRecordId() {
        return useRecordId;
    }

    public void setUseRecordId(int useRecordId) {
        this.useRecordId = useRecordId;
    }

    @Override
    public String getUrl() {
        return "http://" + AppConfig.HOST + ":" + AppConfig.PORT
                + "/sharedPPX/" + TAG;
    }

    @Override
    public StringEntity getStringEntity() {
        StringEntity stringEntity=null;
        JSONObject json=new JSONObject();
        try {
            json.put("useRecordId",useRecordId);

            json.put("stopSite",stopSite);
            json.put("stopTime",stopTime);
            json.put("duration",duration);
            json.put("totalMoney",totalMoney);

            stringEntity=new StringEntity(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return stringEntity;
    }
}
