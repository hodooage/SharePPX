package com.example.z1310_000.sharedppx.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.databinding.ActivityMainBinding;
import com.example.z1310_000.sharedppx.entity.User;
import org.litepal.crud.DataSupport;

import java.util.List;

//监听定位和定位变化
public class MainActivity extends BaseActivity {
    private ActivityMainBinding mBinding;

    //当前用户从表中取第一行数据
    private User nowUser= DataSupport.findFirst(User.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=DataBindingUtil.setContentView(this,R.layout.activity_main);

        mBinding.map.onCreate(savedInstanceState);
        AMap aMap=mBinding.map.getMap();

        aMap.setTrafficEnabled(true);
        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        aMap.setMapType(AMap.MAP_TYPE_NIGHT);

        initListener();
        checkOrder();
    }


    //初始化界面,给按钮注册监听事件
    private void initView() {

    }

    private void initListener(){
        mBinding.capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });

        mBinding.activityCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toActivityCenterIntent = new Intent(MainActivity.this, ActivityCenterActivity.class);
                startActivity(toActivityCenterIntent);
            }
        });

        mBinding.userCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toUserCenterIntent = new Intent(MainActivity.this, UserCenterActivity.class);
                startActivity(toUserCenterIntent);
            }
        });

        mBinding.alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAlert = new Intent(MainActivity.this, AlertActivity.class);
                startActivity(toAlert);
            }
        });

        mBinding.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.menu.setVisibility(View.GONE);
                mBinding.showMenu.setVisibility(View.VISIBLE);
            }
        });

        mBinding.top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.menu.setVisibility(View.VISIBLE);
                mBinding.showMenu.setVisibility(View.GONE);
            }
        });

        mBinding.tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Ho!",Toast.LENGTH_SHORT).show();
            }
        });
    }


    //定位
    private void initLoc() {

    }

    private void initPpx() {

    }


    public static void startAction(Context context){
        Intent intent=new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    private  void checkOrder(){

    }

}