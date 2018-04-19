package com.example.z1310_000.sharedppx.service;

import com.example.z1310_000.sharedppx.entity.UseRecord;
import com.example.z1310_000.sharedppx.entity.ResponseResult;
import com.example.z1310_000.sharedppx.utils.RetrofitUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UseRecordService {
    @GET("insertNewUseRecord")
    Call<ResponseResult<UseRecord>> insertNewUseRecord(@Query("userId")int userId, @Query("xiaId")int xiaId);

    @GET("updateUseRecord")
    Call<ResponseResult<String>> updateUseRecord(@Query("useRecordId")int useRecordId,@Query("stopSite")String stopSite,@Query("duration")String duration,@Query("totalMoney")double totalMoney);

    @GET("getSystemTime")
    Call<ResponseResult<String>> getSystemTime();

    @GET("checkRunningOrder")
    Call<ResponseResult<UseRecord>> checkRunningOrder(@Query("userId")int userId);

    @GET("getUseRecordByUserId")
    Call<ResponseResult<List<UseRecord>>> getUseRecordByUserId(@Query("userId")int userId);

    class util{
        public util(){

        }
        public static UseRecordService getUseRecordService(){
            return RetrofitUtil.getRetrofit().create(UseRecordService.class);
        }
    }
}
