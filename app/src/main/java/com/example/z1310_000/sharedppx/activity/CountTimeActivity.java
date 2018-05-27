package com.example.z1310_000.sharedppx.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.databinding.ActivityCountTimeBinding;
import com.example.z1310_000.sharedppx.entity.UseRecord;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.request.ChangeXiaStateRequest;
import com.example.z1310_000.sharedppx.request.StopDriveXiaRequest;
import com.example.z1310_000.sharedppx.service.UseRecordService;
import com.example.z1310_000.sharedppx.service.UserService;
import com.example.z1310_000.sharedppx.entity.ResponseResult;
import com.example.z1310_000.sharedppx.service.UtilService;
import com.example.z1310_000.sharedppx.service.XiaService;
import com.example.z1310_000.sharedppx.utils.Tips;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountTimeActivity extends BaseActivity {
    private static final String TAG = "CountTimeActivity";
    private ActivityCountTimeBinding mBinding;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new MyAMapLocationListener();
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private UseRecord useRecord;

    private double latitude,longitude;

    private UserService userService=UserService.util.getUserService();

    private XiaService xiaService=XiaService.util.getXiaService();

    private UseRecordService useRecordService=UseRecordService.util.getUseRecordService();

    private String nowTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_count_time);
        initToolbar("计时页面");

        useRecord= (UseRecord) getIntent().getSerializableExtra("useRecord");
        nowTime=getIntent().getStringExtra("nowTime");
        Log.e(TAG, "onCreate: nowTime  IS:"+nowTime);
        initView();
        initData();
        initListener();

        setBaseTime();

        initLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBaseTime();
    }

    private void initView() {
        //电子字体
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/elecnum.TTF");
        mBinding.xiaId.setTypeface(typeface);
        mBinding.xiaPrice.setTypeface(typeface);
        mBinding.sum.setTypeface(typeface);
        mBinding.countTime.setTypeface(typeface);
    }

    private void initData() {
        /*根据前一页面传过来的数据填充*/
        mBinding.xiaId.setText(String.valueOf(useRecord.getxId()));
        mBinding.xiaPrice.setText(String.valueOf(useRecord.getPrice()));
    }

    private void initListener() {
        mBinding.stopDriveXia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //修改当前用户余额
                //扣款成功才修改订单记录表和虾的状态
                reduceBalance();
            }
        });


        mBinding.countTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                int hour = (int) ((SystemClock.elapsedRealtime() - mBinding.countTime.getBase()) / 1000 / 60);
                mBinding.sum.setText(String.valueOf((hour) * useRecord.getPrice()));
            }
        });

    }

    private void reduceBalance(){
        double totalMoney=Double.parseDouble(String.valueOf(mBinding.sum.getText()));

        Call<ResponseResult<String>> call= userService.reduceBalance(useRecord.getuId(),totalMoney);
        call.enqueue(new Callback<ResponseResult<String>>() {
            @Override
            public void onResponse(Call<ResponseResult<String>> call, Response<ResponseResult<String>> response) {
                Log.e(TAG, "onResponse: 扣钱请求的结果是"+response.body() );
                ResponseResult<String> result=response.body();

                //扣款成功
                if (result.getRet()){
                    //停止计时
                    mBinding.countTime.stop();
                    //更改虾的状态
                    changeXiaState();
                    //更新使用记录
                    updateUseRecord();
                }//扣款失败
                else{
                    Toast.makeText(CountTimeActivity.this, result.getData().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult<String>> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t );
                Toast.makeText(CountTimeActivity.this, Tips.INTERNET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //更改虾的状态
    private void changeXiaState(){
        Call<ResponseResult<String>> call=xiaService.stopXiaById(useRecord.getxId());
        call.enqueue(new Callback<ResponseResult<String>>() {
            @Override
            public void onResponse(Call<ResponseResult<String>> call, Response<ResponseResult<String>> response) {
                ResponseResult<String> result=response.body();
                System.out.println(result+"修改虾状态成功");
            }

            @Override
            public void onFailure(Call<ResponseResult<String>> call, Throwable t) {
                Toast.makeText(CountTimeActivity.this, Tips.INTERNET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //更新使用记录
    private void updateUseRecord(){
        String stopSite=latitude+","+longitude;
        String duration=mBinding.countTime.getText().toString();
        double totalMoney= Double.parseDouble(mBinding.sum.getText().toString());
        Call<ResponseResult<String>> call= useRecordService.updateUseRecord(useRecord.getId(),stopSite,duration,totalMoney);
        call.enqueue(new Callback<ResponseResult<String>>() {
            @Override
            public void onResponse(Call<ResponseResult<String>> call, Response<ResponseResult<String>> response) {
                Toast.makeText(CountTimeActivity.this, "结算成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseResult<String>> call, Throwable t) {
                Toast.makeText(CountTimeActivity.this, Tips.INTERNET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 重置
     */
    public void onReset(View view) {
        //setBase 设置基准时间
        //设置参数base为SystemClock.elapsedRealtime()即表示从当前时间开始重新计时）。

        mBinding.countTime.setBase(SystemClock.elapsedRealtime());
    }

    private void setBaseTime(){
        /*
        * 从服务器返回的开始时间和当前时间，可以计算出经过了多少时间，
        * */
        //获取系统时间
        UseRecordService useRecordService=UseRecordService.util.getUseRecordService();
        Call<ResponseResult<String>> call=useRecordService.getSystemTime();
        call.enqueue(new Callback<ResponseResult<String>>() {
            @Override
            public void onResponse(Call<ResponseResult<String>> call, Response<ResponseResult<String>> response) {
                ResponseResult<String> result=response.body();
                Log.e(TAG, "onResponse: 获取系统时间返回的值是："+result.getData() );
                nowTime=result.getData();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date ST=sdf.parse(useRecord.getStarttime());
                    Date NT=sdf.parse(nowTime);
                    long baseTime=NT.getTime()-ST.getTime();
                    //设置计时的基准时间为 计算出的经过多久时间开始
                    mBinding.countTime.setBase(SystemClock.elapsedRealtime()-baseTime);
                    mBinding.countTime.start();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseResult<String>> call, Throwable t) {
                Toast.makeText(CountTimeActivity.this, Tips.INTERNET_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(false);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }

    private class MyAMapLocationListener implements AMapLocationListener {

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    Log.e("位置：", aMapLocation.getAddress());
                    Log.e("位置：", String.valueOf(aMapLocation.getLatitude()));
                    latitude=aMapLocation.getLatitude();
                    Log.e("位置：", String.valueOf(aMapLocation.getLongitude()));
                    longitude=aMapLocation.getLongitude();
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    //进入这个页面需要传递一个UseRecord对象
    public static void startAction(final Context context, UseRecord useRecord){
        Intent intent=new Intent(context,CountTimeActivity.class);
        intent.putExtra("useRecord",useRecord);
        context.startActivity(intent);
    }
}
