package com.example.z1310_000.sharedppx.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.databinding.ActivityMyWalletBinding;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.service.UserService;
import com.example.z1310_000.sharedppx.entity.ResponseResult;

import org.litepal.crud.DataSupport;

import retrofit2.Call;
import retrofit2.Callback;

public class MyWalletActivity extends BaseActivity {

    private static final String TAG = "MyWalletActivity";
    private ActivityMyWalletBinding mBinding;

    private User nowUser=DataSupport.findFirst(User.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_my_wallet);

        initView();
        initData();
    }

    private void initView(){

        initToolbar("我的钱包");
    }

    private void initData(){
        UserService userService=UserService.util.getUserService();
        Log.e(TAG, "initData:               "+nowUser.getUid() );
        Call<ResponseResult<Float>> call=userService.retrieveUserBalance(nowUser.getUid());
        call.enqueue(new Callback<ResponseResult<Float>>() {
            @Override
            public void onResponse(Call<ResponseResult<Float>> call, retrofit2.Response<ResponseResult<Float>> response) {
                ResponseResult<Float> responseResult =response.body();
                Float mBalance= responseResult.getData();
                mBinding.balance.setText(String.valueOf(mBalance));
            }

            @Override
            public void onFailure(Call<ResponseResult<Float>> call, Throwable t) {

            }
        });
    }

    public static void startAction(Context context){
        Intent intent=new Intent(context,MyWalletActivity.class);
        context.startActivity(intent);
    }

}
