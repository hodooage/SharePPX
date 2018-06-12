package com.example.z1310_000.sharedppx.service;

import com.example.z1310_000.sharedppx.utils.AppConfig;
import com.example.z1310_000.sharedppx.utils.RetrofitUtil;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WallpaperService {
    @GET("vertical/category/4e4d610cdf714d2966000003/vertical")
        //定义返回的方法，返回的响应体使用了ResponseBody
    Call<ResponseBody> getWallPaper(@Query("limit")int limitNum);
    class util{
        public util(){

        }
        public static WallpaperService getWallpaperService(){
            return new Retrofit.Builder()
                    .baseUrl(AppConfig.WALLPAPER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(WallpaperService.class);
        }
    }
}
