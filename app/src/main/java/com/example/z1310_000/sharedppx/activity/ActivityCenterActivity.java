package com.example.z1310_000.sharedppx.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.z1310_000.sharedppx.R;

public class ActivityCenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
    }

    public static void startAction(Context context){
        Intent intent=new Intent(context,ActivityCenterActivity.class);
        context.startActivity(intent);
    }

}
