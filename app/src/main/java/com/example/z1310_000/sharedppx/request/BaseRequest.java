package com.example.z1310_000.sharedppx.request;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by z1310_000 on 2017/9/6 0006.
 */

public interface BaseRequest {
    public String getUrl();
    public StringEntity getStringEntity();
}
