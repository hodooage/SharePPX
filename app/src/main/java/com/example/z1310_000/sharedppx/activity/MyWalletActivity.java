package com.example.z1310_000.sharedppx.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.databinding.ActivityMyWalletBinding;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.service.UserService;
import com.example.z1310_000.sharedppx.entity.ResponseResult;
import com.example.z1310_000.sharedppx.util.AuthResult;
import com.example.z1310_000.sharedppx.util.OrderInfoUtil2_0;
import com.example.z1310_000.sharedppx.util.PayResult;

import org.litepal.crud.DataSupport;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWalletActivity extends BaseActivity {
    private double totalMoney;

    private static final String TAG = "MyWalletActivity";
    private ActivityMyWalletBinding mBinding;

    private User nowUser=DataSupport.findFirst(User.class);

    UserService userService;

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2016091600521012";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public static final String RSA2_PRIVATE = "\n" +
            "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC9yUHGqGb+oXtP\n" +
            "2ibiVe3iC+SjBrvD/LFn9itlCMeevOzCoikxVRP4BuNyGbaUjSRkIU3n+YiPQQi0\n" +
            "q0cDJBK7UagxdNZRLV/dsb8p8cXpNZ4cytg5JY48o+2xvHJcbo0MhgsN3bnnloFH\n" +
            "rtm/imAu6yXzLxDjitUrre3uTl7aNUGm+FlZdWmZ50sagRHEw7n36+yHm2h7uvkt\n" +
            "bURbIOGbgjfe+Je4WtxFGiwlJHaJDij/CnZNEKFQFYRxYgjOQ8/6essAZPGHcSHB\n" +
            "oAnKRiQxrF5pTmIucQnOiMaralideh3RbUXhPoN2io5EwBDrU5B6lbCB28VPAgxr\n" +
            "VFDf9AarAgMBAAECggEAO4KOE4LeuN1d004ufy4+p3DfQpdr7SqhXl38gXPdVFMd\n" +
            "UnKSxBtPZaiTTUXu0GnpjVQ8H49JWNIlrrE+VSfMj9wtB0UBpva5ZsTDkp1zV4su\n" +
            "hwy7/D9geHdWozLaEU3BALpPYQuzrYCuDRIwyTTUsCGgBr8tZow0B7xELUN5zqX1\n" +
            "0d/cbO7U9dhfeyl+JdnAsjgp96HkWXMTPKQmUB9VjpiKRUvHoam+aVZuv1l2vEkP\n" +
            "Xb0vgwSaHtuTXHn23XLMfQ5Cb6Bif4/Q4YKYm2uouWz2yOdFw6FdhstCZD6Iemug\n" +
            "Ak14lIkuI1TZ9tDQIhNTdzZ6qCtI698TLbrKCjXuYQKBgQDtTnN1k+UVey8HEQjS\n" +
            "3tqQM/ljWyTkpUJj+G3xLrxtMnIuRoWniP+Yz/k7lGew5OzkBNXorqt9x8xJyglt\n" +
            "oR7dBDWYyYBP67BXGMW4zdTZFBIRgWx+BCcJGfreYhzq0krQUDjBC252HRYzDLvs\n" +
            "exrrN8Xk7qDLJkKWbVIXbuRsSQKBgQDMvIGfRVx7bVv9VZ9LvMcSpRE0JFFK3aD2\n" +
            "FFd2M0X2hD5X56cSzHPNvxC+dt+G9WE67T4hPJP26qdUaj0InBHD8xJmr76o1Zn4\n" +
            "A1M6A7CAJVSJRq7B+IUi3I7KHuLIy7Tqa1YeNfyqjkL737VTKuPXVUY3kqh23lPK\n" +
            "cYoJvf+TUwKBgAih4fFU//BYGs3XAMsq8Chuu5OxrRXkLRYV42nymUgfnaexSQtf\n" +
            "IG0eo6JXM65rpXJl+qIMOJDKw9OrVcx3H/hKPujf65r8p92DOS4Pzr5WzJF2c8vh\n" +
            "usnJ+CY+y25/V9lcEbR0BgCl7jjNLcjccY486SX6yD6blbCDKYlQr16BAoGAJvZY\n" +
            "1K6mgva04ulAf3FVkT7In1up/M1grHQCkQQA9IEGPERhjzpJb4ZE8Xo937ki38v2\n" +
            "XuzgW8UcQvxU0SaiWuAPSGt5pTfBLWm0lP3OwRRZmiZRa6iX7aJ7qgtmrT/XFiWH\n" +
            "JgiwJOUd6kEQiNtIsNoRmtHWLYjx+TzSQDhgQvECgYB8g3hZKGg6in0pahTrHvw/\n" +
            "uedclNM/h7qFGFO3pKSB5jbnKmz1LTahdjW9cr99a+SF4R4qht5YftCWmjQixFAm\n" +
            "6DMCshtTnIO8jezFys0CpU5K+WHEvViFH2mIb7Rkkqn3Fr+oDB70rZ7T8mes39sH\n" +
            "S6iaJKD9XC2lz7Chv1lPww==\n";
    public static final String RSA_PRIVATE ="";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String> ) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(MyWalletActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        addMoney();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(MyWalletActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String> ) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(MyWalletActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(MyWalletActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_my_wallet);

        initView();
        initData();
        initListener();

    }

    private void initView(){

        initToolbar("我的钱包");
    }

    private void initData(){
         userService=UserService.util.getUserService();
        Log.e(TAG, "initData:               "+nowUser.getUid() );
        Call<ResponseResult<Float>> call=userService.retrieveUserBalance(nowUser.getUid());
        call.enqueue(new Callback<ResponseResult<Float>>() {
            @Override
            public void onResponse(Call<ResponseResult<Float>> call, retrofit2.Response<ResponseResult<Float>> response) {
                ResponseResult<Float> responseResult =response.body();
                Float mBalance= responseResult.getData();
                mBinding.balance.setText(String.valueOf(mBalance));
            }

            @Override
            public void onFailure(Call<ResponseResult<Float>> call, Throwable t) {

            }
        });
    }

    private void initListener(){
        mBinding.recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
    }

    private void showInputDialog() {
        /*@setView 装入一个EditView
         */
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(this);
        inputDialog.setTitle("充多少？").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        totalMoney=Double.parseDouble(editText.getText().toString());
                        payV2(getWindow().getDecorView(),editText.getText().toString());
//                        double totalMoney=Double.parseDouble(editText.getText().toString());
//                        Call<ResponseResult<Integer>> call  =userService.addBalance(nowUser.getUid(),totalMoney);
//                        call.enqueue(new Callback<ResponseResult<Integer>>() {
//                            @Override
//                            public void onResponse(Call<ResponseResult<Integer>> call, Response<ResponseResult<Integer>> response) {
//                                Toast.makeText(MyWalletActivity.this,
//                                        response.body().getMessage(),
//                                        Toast.LENGTH_SHORT).show();
//                                initData();
//                            }
//
//                            @Override
//                            public void onFailure(Call<ResponseResult<Integer>> call, Throwable t) {
//
//                            }
//                        });

                    }
                }).show();
    }

    private void addMoney(){
        Call<ResponseResult<Integer>> call  =userService.addBalance(nowUser.getUid(),totalMoney);
        call.enqueue(new Callback<ResponseResult<Integer>>() {
            @Override
            public void onResponse(Call<ResponseResult<Integer>> call, Response<ResponseResult<Integer>> response) {
                Toast.makeText(MyWalletActivity.this,
                        response.body().getMessage(),
                        Toast.LENGTH_SHORT).show();
                initData();
            }

            @Override
            public void onFailure(Call<ResponseResult<Integer>> call, Throwable t) {

            }
        });
    }

    /**
     * 支付宝支付业务
     *
     * @param v
     */
    public void payV2(View v,String total_amount) {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new android.app.AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,total_amount);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(MyWalletActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
    public void h5Pay(View v) {
        Intent intent = new Intent(this, H5PayDemoActivity.class);
        Bundle extras = new Bundle();
        /**
         * url 是要测试的网站，在 Demo App 中会使用 H5PayDemoActivity 内的 WebView 打开。
         *
         * 可以填写任一支持支付宝支付的网站（如淘宝或一号店），在网站中下订单并唤起支付宝；
         * 或者直接填写由支付宝文档提供的“网站 Demo”生成的订单地址
         * （如 https://mclient.alipay.com/h5Continue.htm?h5_route_token=303ff0894cd4dccf591b089761dexxxx）
         * 进行测试。
         *
         * H5PayDemoActivity 中的 MyWebViewClient.shouldOverrideUrlLoading() 实现了拦截 URL 唤起支付宝，
         * 可以参考它实现自定义的 URL 拦截逻辑。
         */
        String url = "http://m.taobao.com";
        extras.putString("url", url);
        intent.putExtras(extras);
        startActivity(intent);
    }



    public static void startAction(Context context){
        Intent intent=new Intent(context,MyWalletActivity.class);
        context.startActivity(intent);
    }

}
