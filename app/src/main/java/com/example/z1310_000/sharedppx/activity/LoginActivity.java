package com.example.z1310_000.sharedppx.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.databinding.ActivityLoginBinding;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.service.UserService;
import com.example.z1310_000.sharedppx.entity.ResponseResult;

import org.litepal.crud.DataSupport;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            Call<ResponseResult<User>> call= userService.getUserById(user.getUid());
            call.enqueue(new Callback<ResponseResult<User>>() {
                @Override
                public void onResponse(Call<ResponseResult<User>> call, Response<ResponseResult<User>> response) {
                    ResponseResult<User> responseResult =response.body();
                    user= responseResult.getData();
                    user.save();
                    mBinding.loginState.setText("数据同步成功");
                }

                @Override
                public void onFailure(Call<ResponseResult<User>> call, Throwable t) {

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
                MainActivity.startAction(this);
                break;
        }
    }

    private void loginServer() {
        String username= String.valueOf(mBinding.username.getText());
        String password= String.valueOf(mBinding.password.getText());

        Call<ResponseResult<User>> call=userService.userLogin(username,password);
        call.enqueue(new Callback<ResponseResult<User>>() {
            @Override
            public void onResponse(Call<ResponseResult<User>> call, Response<ResponseResult<User>> response) {
                //清除之前的用户信息
                DataSupport.deleteAll(User.class);
                ResponseResult<User> responseResult =response.body();
                User user= responseResult.getData();
                user.save();
                mBinding.loginState.setText("登录成功");
                initData();
            }

            @Override
            public void onFailure(Call<ResponseResult<User>> call, Throwable t) {
                mBinding.loginState.setText("登录失败");
            }
        });

    }

    public static void startAction(Context context){
        Intent intent=new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

}
