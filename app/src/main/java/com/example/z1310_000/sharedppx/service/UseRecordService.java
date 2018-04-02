package com.example.z1310_000.sharedppx.service;

import com.example.z1310_000.sharedppx.entity.UseRecord;
import com.example.z1310_000.sharedppx.entity.ResponseResult;
import com.example.z1310_000.sharedppx.utils.RetrofitUtil;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UseRecordService {
    @GET("insertNewUseRecord")
    Call<ResponseResult<UseRecord>> insertNewUseRecord(@Query("userId")int userId, @Query("xiaId")int xiaId);

    @GET("updateUseRecord")
    Call<ResponseResult<UseRecord>> updateUseRecord(@Query("useRecordId")int useRecordId);

    class util{
        public util(){

        }
        public static UseRecordService getUseRecordService(){
            return RetrofitUtil.getRetrofit().create(UseRecordService.class);
        }
    }
}
