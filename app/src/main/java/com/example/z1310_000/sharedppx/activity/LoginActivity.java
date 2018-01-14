package com.example.z1310_000.sharedppx.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.request.LoginRequest;
import com.example.z1310_000.sharedppx.utils.Message;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class LoginActivity extends AppCompatActivity {
    private User nowUser;
    private EditText userEdit, pwdEdit;
    private Button loginbtn, registerbtn;
    private CheckBox autoLogin;
    private AsyncHttpClient client = new AsyncHttpClient();

    private LoginRequest loginRequest=new LoginRequest();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();


        pref=PreferenceManager.getDefaultSharedPreferences(this);

        boolean isRemember=pref.getBoolean("remember_password",false);

        if(isRemember){
            User user= DataSupport.findFirst(User.class);
            if(user!=null){
                userEdit.setText(user.getPhonenum());
                pwdEdit.setText(pref.getString("pwd",""));
                autoLogin.setChecked(true);
                loginServer();
            }
        }


    }

    private void initView() {
        userEdit = (EditText) findViewById(R.id.username);
        pwdEdit = (EditText) findViewById(R.id.password);
        loginbtn = (Button) findViewById(R.id.loginbtn);
        registerbtn = (Button) findViewById(R.id.registerbtn);
        autoLogin = (CheckBox) findViewById(R.id.autoLogin);
        if (Message.newUser != null) {
            User user = Message.newUser;
            userEdit.setText(user.getPhonenum());
            pwdEdit.setText(user.getPwd());
        }

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor=pref.edit();
                if(autoLogin.isChecked()){
                    editor.putBoolean("remember_password",true);
                    editor.putString("pwd",pwdEdit.getText().toString());
                }else{
                    editor.clear();
                }
                editor.apply();
                loginServer();
            }
        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginServer() {

        String user = userEdit.getText().toString();
        String pwd = pwdEdit.getText().toString();
        StringEntity stringEntity = loginRequest.getStringEntity(user,pwd);
        client.post(this, loginRequest.getUrl(), stringEntity, "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if(response.getString("result").equals("ok")){
                        Toast.makeText(getApplicationContext(),"登录成功~",Toast.LENGTH_SHORT).show();
                        //Connector.getDatabase();
                        nowUser=new User();
                        nowUser.setUid(response.getInt("id"));
                        //Log.e("userID",String.valueOf(nowUser.getUid()));
                        nowUser.setSex(response.getString("sex"));
                        nowUser.setNickname(response.getString("nickname"));
                        nowUser.setPhonenum(response.getString("phonenum"));
                        nowUser.setBirthday(response.getString("birthday"));
                        nowUser.setEmail(response.getString("email"));
                        nowUser.save();
                        MainActivity.startAction(LoginActivity.this);
                    }else{
                        Toast.makeText(getApplicationContext(),"登录失败，请检查用户名或密码~",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),"登录失败，请检查网络连接~",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void startAction(Context context){
        Intent intent=new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }
}
