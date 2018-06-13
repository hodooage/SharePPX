package com.example.z1310_000.sharedppx.activity;

import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.databinding.ActivitySplashBinding;
import com.example.z1310_000.sharedppx.entity.ResponseResult;
import com.example.z1310_000.sharedppx.entity.WallPaper;
import com.example.z1310_000.sharedppx.service.WallpaperService;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding mBinding;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=DataBindingUtil.setContentView(this,R.layout.activity_splash);
        initSplash();
        initListener();
    }

    private void initSplash(){
        //倒计时开始
        CountDownTimer countDownTimer=new CountDownTimer(5000,1000) {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onTick(long l) {
                mBinding.progressBar.setMax(5);
                mBinding.progressBar.setProgress(5-(int)l/1000);
                mBinding.countDown.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                LoginActivity.startAction(SplashActivity.this);
            }
        };
        countDownTimer.start();

        WallpaperService wallpaperService=WallpaperService.util.getWallpaperService();
        Call<ResponseBody> call=wallpaperService.getWallPaper(1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //txt.setText(response.body().string());
                    //这里有个坑，response.body().string()只能调用一次，第二次调用会报空指针异常
                    WallPaper wallPaper=new Gson().fromJson(response.body().string(),WallPaper.class);

                    String url=wallPaper.getRes().getVertical().get(0).getWp();

                    Glide.with(getApplicationContext())
                            .load(url)
                            .into(mBinding.splashBackground);
                    Glide.with(getApplicationContext())
                            .load(url)
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                    BitmapDrawable bitmapDrawable= ( BitmapDrawable ) resource;
                                    bitmap=bitmapDrawable.getBitmap();
                                }
                            });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }



    private  void initListener(){
        mBinding.wallpaperSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    WallpaperManager.getInstance(getApplicationContext()).setBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
