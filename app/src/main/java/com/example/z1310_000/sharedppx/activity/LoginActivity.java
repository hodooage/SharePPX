package com.example.z1310_000.sharedppx.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.databinding.ActivityLoginBinding;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.service.UserService;
import com.example.z1310_000.sharedppx.utils.Result;

import org.litepal.crud.DataSupport;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding mBinging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        initListener();
    }

    private void initView() {
        mBinging = DataBindingUtil.setContentView(this,R.layout.activity_login);
        DisplayMetrics metrics =new DisplayMetrics();
/**
 * getRealMetrics - 屏幕的原始尺寸，即包含状态栏。
 * version >= 4.2.2
 */
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        System.out.println("屏幕的分辨率是："+width+"*"+height);
    }

    private void initListener(){
        mBinging.registerbtn.setOnClickListener(this);
        mBinging.loginbtn.setOnClickListener(this);
        mBinging.href.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerbtn:
                RegisterActivity.startAction(this);
                break;
            case  R.id.loginbtn:
                loginServer();
                break;
            case  R.id.href:
                MyWalletActivity.startAction(this);
                break;
        }
    }

    private void loginServer() {
        String username= String.valueOf(mBinging.username.getText());
        String password= String.valueOf(mBinging.password.getText());
        UserService userService=UserService.util.getUserService();

        Call<Result<User>> call=userService.userLogin(username,password);
        call.enqueue(new Callback<Result<User>>() {
            @Override
            public void onResponse(Call<Result<User>> call, Response<Result<User>> response) {
                DataSupport.deleteAll(User.class);
                Result<User> result=response.body();
                User user=result.getData();
                System.out.println(user.getUid());
                System.out.println(user.toString());
                user.save();
            }

            @Override
            public void onFailure(Call<Result<User>> call, Throwable t) {
                System.out.println("请求失败");
            }
        });

    }

    public static void startAction(Context context){
        Intent intent=new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

}
