package com.example.z1310_000.sharedppx.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.databinding.ActivityMyWalletBinding;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.service.UserService;
import com.example.z1310_000.sharedppx.entity.ResponseResult;

import org.litepal.crud.DataSupport;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWalletActivity extends BaseActivity {

    private static final String TAG = "MyWalletActivity";
    private ActivityMyWalletBinding mBinding;

    private User nowUser=DataSupport.findFirst(User.class);

    UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_my_wallet);

        initView();
        initData();
        initListener();

    }

    private void initView(){

        initToolbar("我的钱包");
    }

    private void initData(){
         userService=UserService.util.getUserService();
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

    private void initListener(){
        mBinding.recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
    }

    private void showInputDialog() {
        /*@setView 装入一个EditView
         */
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(this);
        inputDialog.setTitle("充多少？").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        double totalMoney=Double.parseDouble(editText.getText().toString());
                        Call<ResponseResult<Integer>> call  =userService.addBalance(nowUser.getUid(),totalMoney);
                        call.enqueue(new Callback<ResponseResult<Integer>>() {
                            @Override
                            public void onResponse(Call<ResponseResult<Integer>> call, Response<ResponseResult<Integer>> response) {
                                Toast.makeText(MyWalletActivity.this,
                                        response.body().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                initData();
                            }

                            @Override
                            public void onFailure(Call<ResponseResult<Integer>> call, Throwable t) {

                            }
                        });

                    }
                }).show();
    }

    public static void startAction(Context context){
        Intent intent=new Intent(context,MyWalletActivity.class);
        context.startActivity(intent);
    }

}
