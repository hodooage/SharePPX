package com.example.z1310_000.sharedppx.service;

import com.example.z1310_000.sharedppx.entity.Xia;
import com.example.z1310_000.sharedppx.entity.ResponseResult;
import com.example.z1310_000.sharedppx.utils.RetrofitUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface XiaService {
    @GET("getNearByEnableXia")
    Call<ResponseResult<List<Xia>>> getNearByEnableXia(@Query("lng")double lng, @Query("lat")double lat);

    @GET("getXiaById")
    Call<ResponseResult<Xia>> getXiaById(@Query("xiaId")int xiaId);

    @GET("startXiaById")
    Call<ResponseResult<String>> startXiaById(@Query("xiaId") int xiaId);

    @GET("stopXiaById")
    Call<ResponseResult<String>> stopXiaById(@Query("xiaId") int xiaId);



    class util{
        public util(){

        }
        public static XiaService getXiaService(){
            return RetrofitUtil.getRetrofit().create(XiaService.class);
        }
    }
}
