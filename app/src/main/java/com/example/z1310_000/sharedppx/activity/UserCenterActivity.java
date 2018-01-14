package com.example.z1310_000.sharedppx.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.entity.User;

import org.litepal.crud.DataSupport;

import java.io.File;

public class UserCenterActivity extends AppCompatActivity {
    private ImageButton exit, userImage;

    private LinearLayout myRouter,myWallet,invertFriend,exchangeCoupon,myService;

    private TextView phonenum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        exit = (ImageButton) findViewById(R.id.exit);
        userImage = (ImageButton) findViewById(R.id.userImage);
        myRouter= (LinearLayout) findViewById(R.id.myRouter);
        myWallet= (LinearLayout) findViewById(R.id.myWallet);
        invertFriend= (LinearLayout) findViewById(R.id.invertFriend);
        exchangeCoupon= (LinearLayout) findViewById(R.id.invertFriend);
        myService= (LinearLayout) findViewById(R.id.myService);

        phonenum= (TextView) findViewById(R.id.phonenum);
    }

    private void initData() {
        phonenum.setText(DataSupport.findFirst(User.class).getPhonenum());
    }

    private void initListener() {
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animation = AnimationUtils.loadAnimation(UserCenterActivity.this, R.anim.rotate);
                //设置动画监听
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    //重写onAnimationEnd()方法，在动画结束后跳转到主页
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        MainActivity.startAction(UserCenterActivity.this);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                exit.startAnimation(animation);

            }
        });
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInformationActivity.startAction(UserCenterActivity.this);
            }
        });

        myWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyWalletActivity.startAction(UserCenterActivity.this);
            }
        });

        myService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyServiceActivity.startAction(UserCenterActivity.this);
            }
        });
    }

}