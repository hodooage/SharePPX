package com.example.z1310_000.sharedppx.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.z1310_000.sharedppx.R;

/**
 * Created by Administrator on 2018/3/2 0002.
 */

public class BaseActivity extends AppCompatActivity {
    Toolbar mToolbar;
    TextView mToolbarTitle;

    public void initToolbar(){
        mToolbar = (Toolbar ) findViewById(R.id.toolbar);
    }
    public void initToolbar(String title){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarTitle(title);
    }


    public void setToolbarTitle(String title) {
        if (mToolbar != null){
            mToolbarTitle = (TextView ) mToolbar.findViewById(R.id.toolbar_title);
            mToolbarTitle.setVisibility(View.VISIBLE);
            mToolbarTitle.setText(title);
        }
    }

    public String getToolbarTitle(){
        String ret = null;
        if (mToolbar != null && mToolbarTitle != null) {
            ret = mToolbarTitle.getText().toString();
        }
        return ret;
    }

    public void enToolbarBack(View.OnClickListener listener){
        if (mToolbar != null){
            ImageView view = (ImageView ) mToolbar.findViewById(R.id.returnLast);
            view.setVisibility(View.VISIBLE);
            view.setOnClickListener(listener);
        }
    }
}
