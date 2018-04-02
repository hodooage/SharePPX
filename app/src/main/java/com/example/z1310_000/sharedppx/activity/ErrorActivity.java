package com.example.z1310_000.sharedppx.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.z1310_000.sharedppx.R;

public class ErrorActivity extends BaseActivity {
    private TextView errorMessage;
    private Button toMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        initToolbar("出错啦~");
        initView();
        initData();
        initListener();

    }

    //其他任意页面都可以调用此方法，接收错误信息参数
    public static void startAction(Context context,String ErrorMessage){
        Intent intent=new Intent(context,ErrorActivity.class);
        intent.putExtra("errorMessage",ErrorMessage);
        context.startActivity(intent);
    }
    private void initView(){
        errorMessage= (TextView) findViewById(R.id.errorMessage);
        toMain= (Button) findViewById(R.id.toMain);
    }
    private void initData(){
        String error=getIntent().getStringExtra("errorMessage");
        errorMessage.setText(error);
    }
    private void initListener(){
        toMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMain=new Intent(ErrorActivity.this,MainActivity.class);
                startActivity(toMain);
            }
        });
    }
}
