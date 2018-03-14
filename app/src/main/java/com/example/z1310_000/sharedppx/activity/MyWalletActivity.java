package com.example.z1310_000.sharedppx.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.databinding.ActivityMyWalletBinding;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.request.RetrieveUserBalanceRequest;
import com.example.z1310_000.sharedppx.service.UserService;
import com.example.z1310_000.sharedppx.utils.Result;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import okhttp3.Response;
import okhttp3.ResponseBody;
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
        initToolbar();
        setToolbarTitle("我的钱包");
    }

    private void initData(){
        UserService userService=UserService.util.getUserService();
        Log.e(TAG, "initData:               "+nowUser.getUid() );
        Call<Result<Float>> call=userService.retrieveUserBalance(nowUser.getUid());
        call.enqueue(new Callback<Result<Float>>() {
            @Override
            public void onResponse(Call<Result<Float>> call, retrofit2.Response<Result<Float>> response) {
                Result<Float> result=response.body();
                Float mBalance=result.getData();
                //mBinding.balance.setText(String.valueOf(mBalance));
            }

            @Override
            public void onFailure(Call<Result<Float>> call, Throwable t) {

            }
        });
    }

    public static void startAction(Context context){
        Intent intent=new Intent(context,MyWalletActivity.class);
        context.startActivity(intent);
    }

}
