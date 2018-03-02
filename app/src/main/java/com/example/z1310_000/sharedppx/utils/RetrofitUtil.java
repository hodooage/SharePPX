package com.example.z1310_000.sharedppx.utils;

import retrofit2.Retrofit;

/**
 * Created by Administrator on 2018/3/1 0001.
 */

public class RetrofitUtil {
    public static Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(AppConfig.URL_BASE)
                .build();
    }
}
