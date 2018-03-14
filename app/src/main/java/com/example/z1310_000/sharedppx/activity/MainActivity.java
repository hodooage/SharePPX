package com.example.z1310_000.sharedppx.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.databinding.ActivityMainBinding;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.overlay.WalkRouteOverlay;
import com.example.z1310_000.sharedppx.request.GetUseRecordRequest;
import com.example.z1310_000.sharedppx.request.XiaRequest;
import com.example.z1310_000.sharedppx.utils.Message;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

//监听定位和定位变化
public class MainActivity extends AppCompatActivity implements LocationSource, AMapLocationListener {
    private ActivityMainBinding mBinding;

    private MarkerOptions nowPosition;

    private AsyncHttpClient client = new AsyncHttpClient();
    private XiaRequest xiaRequest = new XiaRequest();

    private ImageButton capture, activityCenter, userCenter, alert,down,top;
    //private TextView resultTextView;
    private LinearLayout menu;
    private RelativeLayout showMenu;

    private LinearLayout tips;

    //显示地图需要的变量
    private MapView mapView;//地图控件
    private AMap aMap;//地图对象

    //用ArrayList<MarkerOptions> 装载服务器传过来的皮皮虾信息
    private ArrayList<MarkerOptions> markerOptionlst = new ArrayList<>();

    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    private OnLocationChangedListener mListener = null;//定位监听器

    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    //让每秒提示一次的 烦人的 “定位失败”提示只出现一次
    private boolean isFirstError = true;

    //当前用户从表中取第一行数据
    private User nowUser= DataSupport.findFirst(User.class);

    //请求权限的回调常量
    public static  final  int MY_PERMISSIONS_REQUEST_READ_CONTACTS=1 ;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        //显示地图
        mapView = (MapView) findViewById(R.id.map);
        //必须要写
        mapView.onCreate(savedInstanceState);
        //获取地图对象
        aMap = mapView.getMap();


        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);


        //定位的小图标 默认是蓝点 这里自定义为棒棒糖48px
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.lollipop));
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.strokeColor(android.R.color.transparent);
        aMap.setMyLocationStyle(myLocationStyle);
        //使默认的定位蓝点不可见
        myLocationStyle.showMyLocation(false);

        //开始定位
        initLoc();

        //initPpx()用于读取当前附近能使用的皮皮虾
        initPpx();

        initView();

        //checkOrder()检查当前用户是否存在正在进行的订单
        checkOrder();
    }


    //初始化界面,给按钮注册监听事件
    private void initView() {
        mBinding=DataBindingUtil.setContentView(this,R.layout.activity_main);

        capture = (ImageButton) findViewById(R.id.capture);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });

        activityCenter = (ImageButton) findViewById(R.id.activityCenter);
        activityCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toActivityCenterIntent = new Intent(MainActivity.this, ActivityCenterActivity.class);
                startActivity(toActivityCenterIntent);
            }
        });

        userCenter = (ImageButton) findViewById(R.id.userCenter);
        userCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toUserCenterIntent = new Intent(MainActivity.this, UserCenterActivity.class);
                startActivity(toUserCenterIntent);
            }
        });

        alert = (ImageButton) findViewById(R.id.alert);
        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAlert = new Intent(MainActivity.this, AlertActivity.class);
                startActivity(toAlert);
            }
        });

        down= (ImageButton) findViewById(R.id.down);
        top= (ImageButton) findViewById(R.id.top);
        menu = (LinearLayout) findViewById(R.id.menu);
        showMenu= (RelativeLayout) findViewById(R.id.showMenu);

        down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    menu.setVisibility(View.GONE);
                    showMenu.setVisibility(View.VISIBLE);
                }
            });

        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.setVisibility(View.VISIBLE);
                showMenu.setVisibility(View.GONE);
            }
        });

        tips= (LinearLayout) findViewById(R.id.tips);
        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Ho!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initListener(){

    }


    //定位
    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

        /*LatLng latLng = new LatLng(31.257776,120.749151);
        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("普通皮皮虾").snippet("就是一只再普通不过的皮皮虾啦"));
    */


    }

    private void initPpx() {
       /* client.setConnectTimeout(9999);
        client.setTimeout(9999);
        client.setResponseTimeout(9999);*/
        aMap.clear();
        client.post(this, xiaRequest.getUrl(), xiaRequest.getStringEntity(), "application/json", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("getResult", response.toString());
                //parseJsonArray(response);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject json = (JSONObject) response.get(i);
                        final double lat = json.getDouble("latitude");
                        final double lng = json.getDouble("longitude");
                        LatLng latLng = new LatLng(lat, lng);
                        MarkerOptions markerOption = new MarkerOptions();
                        markerOption.position(latLng);
                        markerOption.title("普通皮皮虾").snippet("就是一只再普通不过的皮皮虾啦");

                        markerOption.draggable(true);//设置Marker可拖动
                        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(), R.drawable.prawn)));
                        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                        markerOption.setFlat(true);//设置marker平贴地图效果
                        //将标记添加到地图上
                        markerOptionlst.add(markerOption);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                aMap.addMarkers(markerOptionlst, true);


                setMarkerListener();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("getResult", "error");

            }
        });
    }

    private void parseJsonArray(JSONArray array) {

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject json = (JSONObject) array.get(i);
                final double lat = json.getDouble("latitude");
                final double lng = json.getDouble("longitude");
                LatLng latLng = new LatLng(lat, lng);
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(latLng);
                markerOption.title("普通皮皮虾").snippet("就是一只再普通不过的皮皮虾啦");

                markerOption.draggable(true);//设置Marker可拖动
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.prawn)));
                // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                markerOption.setFlat(true);//设置marker平贴地图效果
                //将标记添加到地图上
                markerOptionlst.add(markerOption);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        aMap.addMarkers(markerOptionlst, true);


        setMarkerListener();


    }


    private void setMarkerListener() {
        //Toast.makeText(getApplicationContext(),String.valueOf(startLat),Toast.LENGTH_SHORT).show();

        //为标记添加单击事件
        AMap.OnMarkerClickListener markerClickListener = new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                marker.setAlpha(0);
                //只有marker的title为普通皮皮虾时才绘制路径，注册监听事件
                if (marker.getTitle().equals("普通皮皮虾")) {
                    final double startLat = nowPosition.getPosition().latitude;
                    final double startLng = nowPosition.getPosition().longitude;
                    Log.e("llllllllllllllllllll", marker.getId());

                    RouteSearch routeSearch = new RouteSearch(getApplicationContext());

                    routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
                        @Override
                        public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

                        }

                        @Override
                        public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

                        }

                        @Override
                        public void onWalkRouteSearched(WalkRouteResult mWalkRouteResult, int i) {
                            if (i == 1000) {
                                Log.e("route", mWalkRouteResult.toString());

                                //aMap.clear();// 清理地图上的所有覆盖物


                                initPpx();//把皮皮虾加入地图

                                //绘制路线
                                WalkPath walkPath = mWalkRouteResult.getPaths().get(0);

                                WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                                        getApplicationContext(), aMap, walkPath,
                                        mWalkRouteResult.getStartPos(),
                                        mWalkRouteResult.getTargetPos());
                                walkRouteOverlay.addToMap();
                                walkRouteOverlay.zoomToSpan();
                            }
                        }

                        @Override
                        public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

                        }
                    });


                    RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(new LatLonPoint(startLat, startLng),
                            new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude));


                    RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WALK_DEFAULT);
                    routeSearch.calculateWalkRouteAsyn(query);


                    return false;
                }
                return false;

            }
        };
        aMap.setOnMarkerClickListener(markerClickListener);

    }


    //定位回调函数
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {

                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(19));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(amapLocation);
                    //添加图钉
                    aMap.addMarker(getMarkerOptions(amapLocation));
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                    //Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                }


            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                if (isFirstError) {
                    Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
                    isFirstError = false;
                }
            }
        }
    }

    //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
        //设置图钉选项
        MarkerOptions options = new MarkerOptions();
        //图标
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.lollipop));
        //位置
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        StringBuffer buffer = new StringBuffer();
        buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
        //标题
        options.title(buffer.toString());
        //子标题
        options.snippet("您现在所在的位置");
        //设置多少帧刷新一次图片资源
        options.period(60);
        nowPosition = options;
        return options;

    }

    //激活定位
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;

    }

    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            //resultTextView.setText(scanResult + "&uId=" + Message.nowUser.getId());

        }
    }

    public static void startAction(Context context){
        Intent intent=new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    private  void checkOrder(){
        AsyncHttpClient checkOrderClient=new AsyncHttpClient();
        GetUseRecordRequest getUseRecordRequest=new GetUseRecordRequest();
        //发送uid，去useRecord表中查询是否有未完成订单，如果有，服务器返回exist，
        // 在MainActivity页面上显示一个超链接用于进入CountTimeActivity，
        // 并传入一个参数（useRecordId）表明存在未结算订单;
        checkOrderClient.post(MainActivity.this,getUseRecordRequest.getUrl(),getUseRecordRequest.getStringEntity(nowUser.getUid()),"application/json",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if(response.getString("result").equals("exist")){
                        final Bundle bundle=new Bundle();
                        bundle.putInt("useRecordId",response.getInt("useRecordId"));
                        bundle.putString("startTime",response.getString("startTime"));
                        bundle.putInt("xid",response.getInt("xid"));
                        bundle.putString("price",response.getString("price"));
                        bundle.putString("nowTime",response.getString("nowTime"));

                        tips.setVisibility(View.VISIBLE);
                        tips.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CountTimeActivity.startAction(MainActivity.this,bundle);

                            }
                        });
                        capture.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(),"请点击右上角进入正在进行的订单哦~",Toast.LENGTH_LONG).show();
                            }
                        });

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
}