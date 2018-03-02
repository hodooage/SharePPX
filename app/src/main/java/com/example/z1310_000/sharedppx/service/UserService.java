package com.example.z1310_000.sharedppx.service;

import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.utils.RetrofitUtil;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018/2/25.
 */

public interface UserService {

    @GET("basil2style")
        //定义返回的方法，返回的响应体使用了ResponseBody
    Call<ResponseBody> getString();

    @POST("userLogin")
    Call<ResponseBody> userLogin(@Query("username") String username, @Query("password") String password);


    class util{
        public util(){

        }
        public static UserService getUserService(){
           return RetrofitUtil.getRetrofit().create(UserService.class);
        }
    }
}
