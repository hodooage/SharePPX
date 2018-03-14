package com.example.z1310_000.sharedppx.activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1310_000.sharedppx.R;

/**
 * Created by Administrator on 2018/3/2 0002.
 */

public class BaseActivity extends AppCompatActivity {
    Toolbar mToolbar;
    TextView mToolbarTitle;
    ImageButton returnLast,close;

    public void initToolbar(){
        mToolbar=findViewById(R.id.mToolbar);
    }


    public void setToolbarTitle(String title) {
        if (mToolbar != null){
            mToolbarTitle = mToolbar.findViewById(R.id.activity_title);
            mToolbarTitle.setVisibility(View.VISIBLE);
            mToolbarTitle.setText(title);
            Toast.makeText(this, "are you ojbk?", Toast.LENGTH_SHORT).show();
            setToolbarListener();
        }

    }

    public void setToolbarListener(){
        if(returnLast!=null){
            returnLast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "return", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        if(close!=null){
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.startAction(getApplicationContext());
                }
            });
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
