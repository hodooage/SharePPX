package com.example.z1310_000.sharedppx.service;

import com.example.z1310_000.sharedppx.entity.ResponseResult;
import com.example.z1310_000.sharedppx.utils.RetrofitUtil;

import java.util.Date;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UtilService {
    @GET("getSystemTime")
    Call<ResponseResult<Date>> getSystemTime();


    class util{
        public util(){

        }
        public static UtilService getUtilService(){
            return RetrofitUtil.getRetrofit().create(UtilService.class);
        }
    }
}
