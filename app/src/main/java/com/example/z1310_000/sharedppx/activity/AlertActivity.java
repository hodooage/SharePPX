package com.example.z1310_000.sharedppx.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.z1310_000.sharedppx.R;

public class AlertActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        initView();
        initData();
        initListeren();
    }
    private void initView(){
        exit= (ImageButton) findViewById(R.id.exit);
    }
    private void initData(){

    }
    private void initListeren(){
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exit:
                finish();
                break;
            default:
                    break;
        }
    }
}
