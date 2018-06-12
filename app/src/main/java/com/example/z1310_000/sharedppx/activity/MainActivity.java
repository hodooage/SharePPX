package com.example.z1310_000.sharedppx.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.databinding.ActivityMainBinding;
import com.example.z1310_000.sharedppx.entity.UseRecord;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.entity.Xia;
import com.example.z1310_000.sharedppx.service.UseRecordService;
import com.example.z1310_000.sharedppx.service.XiaService;
import com.example.z1310_000.sharedppx.entity.ResponseResult;
import com.example.z1310_000.sharedppx.utils.Tips;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//监听定位和定位变化
public class MainActivity extends BaseActivity implements AMapLocationListener {
    private ActivityMainBinding mBinding;
    private String TAG = "MainActivity";
    private XiaService xiaService = XiaService.util.getXiaService();

    private UseRecordService useRecordService = UseRecordService.util.getUseRecordService();

    private User nowUser = DataSupport.findFirst(User.class);

    private double nowLng, nowLat;

    //声明mlocationClient对象
    AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    AMapLocationClientOption mLocationOption = null;

    AMap aMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setTranslucentStatus();
        mBinding.map.onCreate(savedInstanceState);
        aMap = mBinding.map.getMap();

        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        //显示定位蓝点
        aMap.setMyLocationEnabled(true);

        initLoc();

        initListener();
        checkOrder();

    }

    private void initListener() {
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
                Toast.makeText(getApplicationContext(), "Ho!", Toast.LENGTH_SHORT).show();
            }
        });

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Animation animation = new ScaleAnimation(1, 1.2f, 1, 1.2f);
                marker.setAnimation(animation);
                marker.startAnimation();
                return false;
            }
        });

    }


    //定位
    private void initLoc() {

        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        //mLocationOption.setInterval(60000);
        //设置只定位一次
        mLocationOption.setOnceLocation(true);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        aMap.setMyLocationStyle(myLocationStyle);

        mlocationClient.startLocation();
    }

    private void initPpx() {
        Call<ResponseResult<List<Xia>>> call = xiaService.getNearByEnableXia(nowLng, nowLat);
        call.enqueue(new Callback<ResponseResult<List<Xia>>>() {
            @Override
            public void onResponse(Call<ResponseResult<List<Xia>>> call, Response<ResponseResult<List<Xia>>> response) {
                System.out.println("is ok" + response.body());
                ResponseResult<List<Xia>> responseResult = response.body();
                List<Xia> xiaList = responseResult.getData();
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.xia_marker);
                for (Xia xia : xiaList) {
                    LatLng latLng = new LatLng(xia.getLatitude(), xia.getLongitude());
                    final Marker marker = aMap.addMarker(new MarkerOptions()
                            .position(latLng).title("皮皮虾")
                            .snippet("DefaultMarker")
                            .icon(bitmapDescriptor)
                    );
                    Animation animation = new ScaleAnimation(0, 1, 0, 1);
                    long duration = 1000L;
                    animation.setDuration(duration);
                    animation.setInterpolator(new LinearInterpolator());

                    marker.setAnimation(animation);
                    marker.startAnimation();

                }
            }

            @Override
            public void onFailure(Call<ResponseResult<List<Xia>>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "可能服务器炸了或者你没网", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static void startAction(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    //检测是否存在正在骑行的订单，存在就让扫码按钮不可用
    private void checkOrder() {
        Call<ResponseResult<UseRecord>> call = useRecordService.checkRunningOrder(nowUser.getUid());

        call.enqueue(new Callback<ResponseResult<UseRecord>>() {
            @Override
            public void onResponse(Call<ResponseResult<UseRecord>> call, Response<ResponseResult<UseRecord>> response) {
                //返回值为true就说明存在骑行中的订单
                if (response.body()!=null&&response.body().getRet()) {
                    final UseRecord useRecord = response.body().getData();
                    Log.e(TAG, "onResponse: " + useRecord.toString());
                    mBinding.tips.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CountTimeActivity.startAction(getApplicationContext(), useRecord);
                        }
                    });
                    mBinding.capture.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this, "当前存在正在骑行的订单，请点击右上角进入哦~", Toast.LENGTH_SHORT).show();
                        }
                    });
                    mBinding.tips.setVisibility(View.VISIBLE);
                    mBinding.capture.setClickable(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseResult<UseRecord>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
            }
        });


    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                nowLat = amapLocation.getLatitude();//获取纬度
                //西经在高德地图中用负数表示，因此西经前面要加-号，东经不用
                nowLng = amapLocation.getLongitude();//获取经度
                initPpx();
                aMap.moveCamera(CameraUpdateFactory
                        .changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));

                aMap.moveCamera(CameraUpdateFactory.zoomTo(19));
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                Log.e(TAG, "onLocationChanged: " + amapLocation.toStr());
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }


}