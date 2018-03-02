package com.example.z1310_000.sharedppx.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.databinding.ActivityLoginBinding;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.request.LoginRequest;
import com.example.z1310_000.sharedppx.service.UserService;
import com.example.z1310_000.sharedppx.utils.Message;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

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
                TestActivity.startAction(this);
                break;
        }
    }

    private void loginServer() {
        String username= String.valueOf(mBinging.username.getText());
        String password= String.valueOf(mBinging.password.getText());
        UserService userService=UserService.util.getUserService();

        Call<ResponseBody> call=userService.userLogin(username,password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    mBinging.loginState.setText(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "onResponse: "+response.body().toString() );
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mBinging.loginState.setText(t.getLocalizedMessage());
            }
        });
    }

    public static void startAction(Context context){
        Intent intent=new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

}
