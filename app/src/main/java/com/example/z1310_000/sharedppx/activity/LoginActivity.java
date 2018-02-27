package com.example.z1310_000.sharedppx.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class LoginActivity extends AppCompatActivity {
    private User nowUser;
    private EditText userEdit, pwdEdit;
    private Button loginbtn, registerbtn;
    private CheckBox autoLogin;
    private ImageView href;
    private AsyncHttpClient client = new AsyncHttpClient();

    private LoginRequest loginRequest=new LoginRequest();

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.e("SHA1", "onCreate: "+getCertificateSHA1Fingerprint(this) );

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
        href= ( ImageView ) findViewById(R.id.href);
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
        href.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestActivity.startAction(getApplicationContext());
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
    //这个是获取SHA1的方法
    public static String getCertificateSHA1Fingerprint(Context context) {
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取当前要获取SHA1值的包名，也可以用其他的包名，但需要注意，
        //在用其他包名的前提是，此方法传递的参数Context应该是对应包的上下文。
        String packageName = context.getPackageName();
        //返回包括在包中的签名信息
        int flags = PackageManager.GET_SIGNATURES;
        PackageInfo packageInfo = null;
        try {
            //获得包的所有内容信息类
            packageInfo = pm.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //签名信息
        Signature[] signatures = packageInfo.signatures;
        byte[] cert = signatures[0].toByteArray();
        //将签名转换为字节数组流
        InputStream input = new ByteArrayInputStream(cert);
        //证书工厂类，这个类实现了出厂合格证算法的功能
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X509");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //X509证书，X.509是一种非常通用的证书格式
        X509Certificate c = null;
        try {
            c = (X509Certificate) cf.generateCertificate(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String hexString = null;
        try {
            //加密算法的类，这里的参数可以使MD4,MD5等加密算法
            MessageDigest md = MessageDigest.getInstance("SHA1");
            //获得公钥
            byte[] publicKey = md.digest(c.getEncoded());
            //字节到十六进制的格式转换
            hexString = byte2HexFormatted(publicKey);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        return hexString;
    }
    //这里是将获取到得编码进行16进制转换
    private static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1)
                h = "0" + h;
            if (l > 2)
                h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1))
                str.append(':');
        }
        return str.toString();
    }
}
