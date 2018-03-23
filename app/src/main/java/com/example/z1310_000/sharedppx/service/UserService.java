package com.example.z1310_000.sharedppx.service;

import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.utils.Result;
import com.example.z1310_000.sharedppx.utils.RetrofitUtil;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2018/2/25.
 */

public interface UserService {

    @GET("basil2style")
        //定义返回的方法，返回的响应体使用了ResponseBody
    Call<ResponseBody> getString();

    @GET("getUserById")
    Call<Result<User>> getUserById(@Query("userId")int userId);

    @POST("userLogin")
    Call<Result<User>> userLogin(@Query("username") String username, @Query("password") String password);

    @GET("retrieveUserBalance")
    Call<Result<Float>> retrieveUserBalance(@Query("userId")int userId);

    @Multipart
    @POST("uploadImage")
    Call<ResponseBody> uploadImage(@Part("userId") int userId,
                                   @Part MultipartBody.Part file);

    @POST("editUserInformation")
    Call<Result<Integer>> editUserInformation(@Body User user);




    class util{
        public util(){

        }
        public static UserService getUserService(){
           return RetrofitUtil.getRetrofit().create(UserService.class);
        }
    }


}
