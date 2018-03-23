package com.example.z1310_000.sharedppx.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.request.ChangeBalanceRequest;
import com.example.z1310_000.sharedppx.request.ChangeXiaStateRequest;
import com.example.z1310_000.sharedppx.request.RetrieveUserBalanceRequest;
import com.example.z1310_000.sharedppx.request.StopDriveXiaRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class CountTimeActivity extends BaseActivity {
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new MyAMapLocationListener();
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


    private Bundle mbundle;

    private Chronometer countTime;

    private TextView xiaId, xiaPrice, sum;

    private Button stopDriveXia,toMain;

    private ImageButton returnMain;

    private int  xid;
    private float price;

    private double latitude,longitude;

    private User nowUser= DataSupport.findFirst(User.class);



    //是否扣款成功
    boolean reduceBalance=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_time);

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
        xiaId = (TextView) findViewById(R.id.xiaId);
        xiaPrice = (TextView) findViewById(R.id.xiaPrice);
        sum = (TextView) findViewById(R.id.sum);
        countTime = (Chronometer) findViewById(R.id.countTime);
        stopDriveXia = (Button) findViewById(R.id.stopDriveXia);
        toMain= (Button) findViewById(R.id.toMain);
        returnMain= (ImageButton) findViewById(R.id.close);
        //电子字体
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/elecnum.TTF");
        xiaId.setTypeface(typeface);
        xiaPrice.setTypeface(typeface);
        sum.setTypeface(typeface);
        countTime.setTypeface(typeface);
    }

    private void initData() {
        mbundle = getIntent().getExtras();

        /*根据前一页面传过来的数据填充*/
        xid = mbundle.getInt("xid");
        price = Float.parseFloat(mbundle.getString("price"));

        xiaId.setText(String.valueOf(xid));
        xiaPrice.setText(String.valueOf(price));
    }

    private void initListener() {
        stopDriveXia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int uid = nowUser.getUid();
                ChangeBalanceRequest changeBalanceRequest = new ChangeBalanceRequest();

                //修改当前用户余额
                //扣款成功才修改订单记录表和虾的状态
                double nowsum = Double.parseDouble(String.valueOf(sum.getText()));
                AsyncHttpClient changeBalanceClient = new AsyncHttpClient();
                changeBalanceClient.post(CountTimeActivity.this, changeBalanceRequest.getUrl(), changeBalanceRequest.getStringEntity(uid, nowsum), "application/json", new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if(response.getString("result").equals("ok")){
                                Toast.makeText(getApplicationContext(), "扣款成功", Toast.LENGTH_SHORT).show();
                                countTime.stop();

                                reduceBalance=true;

                                ChangeXiaStateRequest changeXiaStateRequest = new ChangeXiaStateRequest();
                                StopDriveXiaRequest stopDriveXiaRequest = new StopDriveXiaRequest();

                                //修改当前虾状态,位置
                                AsyncHttpClient changeXiaStateClient = new AsyncHttpClient();
                                changeXiaStateClient.post(CountTimeActivity.this, changeXiaStateRequest.getUrl(), changeXiaStateRequest.getStringEntity(xid, 0,latitude,longitude), "application/json", new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        Toast.makeText(getApplicationContext(), "恢复虾状态成功", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        super.onFailure(statusCode, headers, responseString, throwable);
                                        Toast.makeText(getApplicationContext(), "虾状态修改失败", Toast.LENGTH_SHORT).show();

                                    }
                                });

                                //修改当前订单记录,需要useRecordId,stopTime,stopSite,duration,totalMoney
                                stopDriveXiaRequest.setUseRecordId(mbundle.getInt("useRecordId"));
                                //字符串转date型
                                String startTime = mbundle.getString("startTime");

                                stopDriveXiaRequest.setDuration(countTime.getText().toString());
                                stopDriveXiaRequest.setStopSite(latitude+","+longitude);
                                stopDriveXiaRequest.setTotalMoney(Double.parseDouble(sum.getText().toString()));
                                AsyncHttpClient stopDriveXiaClient = new AsyncHttpClient();
                                stopDriveXiaClient.post(CountTimeActivity.this, stopDriveXiaRequest.getUrl(), stopDriveXiaRequest.getStringEntity(), "application/json", new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        Toast.makeText(getApplicationContext(), "修改订单记录成功", Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        super.onFailure(statusCode, headers, responseString, throwable);
                                        Toast.makeText(getApplicationContext(), "修改订单记录失败", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }else{
                                String errorMsg=response.getString("errorMsg");
                                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });


            }
        });


        countTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                int hour = (int) ((SystemClock.elapsedRealtime() - countTime.getBase()) / 1000 / 60);
                sum.setText(String.valueOf((hour) * price));
            }
        });

        toMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.startAction(CountTimeActivity.this);
            }
        });

        returnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.startAction(CountTimeActivity.this);
            }
        });

    }


    /**
     * 重置
     */
    public void onReset(View view) {
        //setBase 设置基准时间
        //设置参数base为SystemClock.elapsedRealtime()即表示从当前时间开始重新计时）。

        countTime.setBase(SystemClock.elapsedRealtime());
    }

    private void setBaseTime(){
        /*
        * 从服务器返回的开始时间和当前时间，可以计算出经过了多少时间，
        * */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date ST=sdf.parse(mbundle.getString("startTime"));
            Date NT=sdf.parse(mbundle.getString("nowTime"));
            long baseTime=NT.getTime()-ST.getTime();
            //设置计时的基准时间为 计算出的经过多久时间开始
            countTime.setBase(SystemClock.elapsedRealtime()-baseTime);
            countTime.start();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    //进入这个页面需要传递一个bundle,包含useRecordId,startTime,xid,price,nowTime
    public static void startAction(Context context,Bundle bundle){
        Intent intent=new Intent(context,CountTimeActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }
}
