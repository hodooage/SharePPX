package com.example.z1310_000.sharedppx.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.service.UserService;
import com.example.z1310_000.sharedppx.utils.AppConfig;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";
    private TextView textView;
    private EditText text_username,text_password;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initRetrofit();
            }
        });
    }

    private void  initView(){
        text_username= ( EditText ) findViewById(R.id.text_username);
        text_password= ( EditText ) findViewById(R.id.text_password);
        loginBtn= ( Button ) findViewById(R.id.loginBtn);
        textView= ( TextView ) findViewById(R.id.textView);
    }

    private void initRetrofit(){
        Log.e(TAG, "initRetrofit: " );
        //获取Retrofit对象，设置地址
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.URL_BASE)
                .build();
        UserService userService=retrofit.create(UserService.class);
        Call<ResponseBody> call = userService.userLogin("qqq","123");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "success: "+call);
                try {
                    textView.setText(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(TestActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: " );
            }
        });
    }

    public static void startAction(Context context){
        Intent intent=new Intent(context,TestActivity.class);
        context.startActivity(intent);
    }
}