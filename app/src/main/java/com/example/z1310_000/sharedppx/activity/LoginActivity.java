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

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding mBinding;
    private UserService userService=UserService.util.getUserService();
    private User user=DataSupport.findFirst(User.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        setTranslucentStatus();
        initData();
        initListener();
    }

    private void initData(){

        if(user!=null){
            Call<Result<User>> call= userService.getUserById(user.getUid());
            call.enqueue(new Callback<Result<User>>() {
                @Override
                public void onResponse(Call<Result<User>> call, Response<Result<User>> response) {
                    Result<User> result=response.body();
                    user=result.getData();
                    user.save();
                    mBinding.loginState.setText("数据同步成功");
                }

                @Override
                public void onFailure(Call<Result<User>> call, Throwable t) {

                }
            });
            mBinding.href.setOnClickListener(this);
        }else{
            mBinding.loginState.setText("请先登录");
        }
    }

    private void initListener(){
        mBinding.registerbtn.setOnClickListener(this);
        mBinding.loginbtn.setOnClickListener(this);

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
                TestAmapActivity.startAction(this);
                break;
        }
    }

    private void loginServer() {
        String username= String.valueOf(mBinding.username.getText());
        String password= String.valueOf(mBinding.password.getText());

        Call<Result<User>> call=userService.userLogin(username,password);
        call.enqueue(new Callback<Result<User>>() {
            @Override
            public void onResponse(Call<Result<User>> call, Response<Result<User>> response) {
                //清除之前的用户信息
                DataSupport.deleteAll(User.class);
                Result<User> result=response.body();
                User user=result.getData();
                user.save();
                mBinding.loginState.setText("登录成功");
            }

            @Override
            public void onFailure(Call<Result<User>> call, Throwable t) {
                mBinding.loginState.setText("登录失败");
            }
        });

    }

    public static void startAction(Context context){
        Intent intent=new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

}
