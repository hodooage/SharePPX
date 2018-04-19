package com.example.z1310_000.sharedppx.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.databinding.ActivityLoginBinding;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.service.UserService;
import com.example.z1310_000.sharedppx.entity.ResponseResult;
import com.example.z1310_000.sharedppx.utils.Tips;

import org.litepal.crud.DataSupport;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding mBinding;
    private UserService userService=UserService.util.getUserService();
    private User user;

    private boolean granted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        setTranslucentStatus();
        initData();


        initPermission();

        if(granted){
            initListener();
        }else{
            Toast.makeText(this, "请先授予权限再登录哦", Toast.LENGTH_SHORT).show();
        }

    }

    private void initData(){
        user=DataSupport.findFirst(User.class);
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
//                RegisterActivity.startAction(this);
                Intent intent=new Intent(LoginActivity.this,PayDemoActivity.class);
                startActivity(intent);
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
                Toast.makeText(LoginActivity.this, Tips.INTERNET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void initPermission(){
        int permission= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permission!= PackageManager.PERMISSION_GRANTED){
            if (shouldRequest()) return;
            //请求权限
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);

        }else{
            granted=true;
        }

    }

    private boolean shouldRequest(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            //显示一个对话框，给用户解释
            explainDialog();
            return true;
        }
        return false;
    }


    private void explainDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("应用需要获取您的定位权限,是否授权？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //请求权限
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                }).setNegativeButton("取消", null)
                .create().show();
    }

    /**
     * 请求权限的回调
     *
     * 参数1：requestCode-->是requestPermissions()方法传递过来的请求码。
     * 参数2：permissions-->是requestPermissions()方法传递过来的需要申请权限
     * 参数3：grantResults-->是申请权限后，系统返回的结果，PackageManager.PERMISSION_GRANTED表示授权成功，PackageManager.PERMISSION_DENIED表示授权失败。
     * grantResults和permissions是一一对应的
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0) {
            granted = grantResults[0] == PackageManager.PERMISSION_GRANTED;//是否授权，可以根据permission作为标记

        }
    }


    public static void startAction(Context context){
        Intent intent=new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

}
