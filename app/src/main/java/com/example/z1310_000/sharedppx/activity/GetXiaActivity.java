package com.example.z1310_000.sharedppx.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.request.ChangeXiaStateRequest;
import com.example.z1310_000.sharedppx.request.GetXiaRequest;
import com.example.z1310_000.sharedppx.request.StartDriveXiaRequest;
import com.example.z1310_000.sharedppx.utils.Message;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.Format;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class GetXiaActivity extends AppCompatActivity {
    private AsyncHttpClient client = new AsyncHttpClient();
    private StartDriveXiaRequest startDriveXiaRequest = new StartDriveXiaRequest();
    private Bundle bundle;

    //页面注册
    private TextView num, type, price;
    private ImageButton ppxgo,toMain;

    private User nowUser= DataSupport.findFirst(User.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_xia);

        bundle=getIntent().getExtras();

        initView();

        initData();

        initListener();
        //当前登录用户的id
        //Message.nowUser.getId();
    }

    //初始化页面
    private void initView() {
        num = (TextView) findViewById(R.id.num);
        type = (TextView) findViewById(R.id.type);
        price = (TextView) findViewById(R.id.price);
        ppxgo = (ImageButton) findViewById(R.id.ppxgo);
    }


    private void initData(){


        num.setText(String.valueOf(bundle.getInt("xid")));
        Typeface colorttf=Typeface.createFromAsset(getAssets(),"fonts/color.ttf");
        switch (bundle.getInt("type")){

            case 0:
                type.setText("普通皮皮虾");
                break;
            case 1:
                type.setText("黄金皮皮虾");
                break;
            case 2:
                type.setTypeface(colorttf);
                type.setText("七彩至尊皮皮虾");
                break;
        }

        price.setText(bundle.getString("price"));

    }

    private void initListener(){
        ppxgo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                //未登录则跳转到登录界面
                if(null==DataSupport.findFirst(User.class)){
                    Toast.makeText(getApplicationContext(),"您还没有登录哦~",Toast.LENGTH_LONG).show();
                    LoginActivity.startAction(GetXiaActivity.this);
                }else{
                    int u_id=nowUser.getUid();
                    int x_id=bundle.getInt("xid");
                    //生成新的使用记录
                    client.post(GetXiaActivity.this,startDriveXiaRequest.getUrl(),startDriveXiaRequest.getStringEntity(u_id,x_id),"application/json",new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            //将虾的数据传到计时页面
                            try {
                                String startTime=response.getString("startTime");
                                int useRecordId=response.getInt("useRecordId");
                                bundle.putString("startTime",startTime);
                                bundle.putInt("useRecordId",useRecordId);
                                //这里放一个当前时间与开始时间一致，用于计时页面从零开始计时
                                bundle.putString("nowTime",startTime);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            /*Intent intent=new Intent(GetXiaActivity.this,CountTimeActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);*/
                            //更好并且可重用的做法
                            CountTimeActivity.startAction(GetXiaActivity.this,bundle);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }
                    });

                    //更改虾状态为正在使用
                    AsyncHttpClient changeXiaStateClient=new AsyncHttpClient();
                    ChangeXiaStateRequest changeXiaStateRequest=new ChangeXiaStateRequest();
                    changeXiaStateClient.post(GetXiaActivity.this,changeXiaStateRequest.getUrl(),changeXiaStateRequest.getStringEntity(x_id,1),"application/json",new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            Toast.makeText(getApplicationContext(),"更改虾的状态为正在使用",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }
                    });

                }
            }
        });
        toMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.startAction(GetXiaActivity.this);
            }
        });
    }
}
