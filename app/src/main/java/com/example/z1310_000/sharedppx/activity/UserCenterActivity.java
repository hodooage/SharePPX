package com.example.z1310_000.sharedppx.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.example.z1310_000.sharedppx.databinding.ActivityUserCenterBinding;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.utils.GetBitmapForUrl;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;

public class UserCenterActivity extends AppCompatActivity {
    private ActivityUserCenterBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_user_center);
        initData();
        initListener();
    }

    private void initData() {
        User user=DataSupport.findFirst(User.class);
        //这么一大段，还不如用glide省事，一句话搞定
//        new Thread() {
//            public void run() {
//                //这儿是耗时操作，完成之后更新UI；
//                try {
//                    runOnUiThread(new Runnable(){
//                        User user=DataSupport.findFirst(User.class);
//                        Bitmap bitmap= GetBitmapForUrl.getBitmap(user.getImage());
//                        @Override
//                        public void run() {
//                            //更新UI
//                            mBinding.userImage.setImageBitmap(bitmap);
//                        }
//
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//           Glide.with(this)
//                    .load(user.getImage())
//                   //.apply(bitmapTransform(new CropCircleTransformation(this)))
//                    .into(mBinding.userImage);

        mBinding.phonenum.setText(user.getPhonenum());
    }

    private void initListener() {
        mBinding.exit.setOnClickListener(new View.OnClickListener() {
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
                mBinding.exit.startAnimation(animation);

            }
        });
        mBinding.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInformationActivity.startAction(UserCenterActivity.this);
            }
        });

        mBinding.myWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyWalletActivity.startAction(UserCenterActivity.this);
            }
        });

        mBinding.myService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyServiceActivity.startAction(UserCenterActivity.this);
            }
        });
    }

    public static  void startAction(Context context){
        Intent intent=new Intent(context,UserCenterActivity.class);
        context.startActivity(intent);
    }

}