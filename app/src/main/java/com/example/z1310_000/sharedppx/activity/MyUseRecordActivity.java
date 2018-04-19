package com.example.z1310_000.sharedppx.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.adapter.UseRecordAdapter;
import com.example.z1310_000.sharedppx.databinding.ActivityMyUseRecordBinding;
import com.example.z1310_000.sharedppx.entity.ResponseResult;
import com.example.z1310_000.sharedppx.entity.UseRecord;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.service.UseRecordService;

import org.litepal.crud.DataSupport;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyUseRecordActivity extends BaseActivity {
    private ActivityMyUseRecordBinding mBinding;

    private UseRecordService useRecordService=UseRecordService.util.getUseRecordService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_my_use_record);

        initToolbar("我的行程");
        initData();

    }

    private void initData(){
        int userId= DataSupport.findFirst(User.class).getUid();

        Call<ResponseResult<List<UseRecord>>> call=useRecordService.getUseRecordByUserId(userId);

        call.enqueue(new Callback<ResponseResult<List<UseRecord>>>() {
            @Override
            public void onResponse(Call<ResponseResult<List<UseRecord>>> call, Response<ResponseResult<List<UseRecord>>> response) {
                //如果存在记录
                if (response.body()!=null&&response.body().getRet()) {
                    System.out.println(response.body());

                    List<UseRecord> useRecordList=response.body().getData();


                    UseRecordAdapter useRecordAdapter=new UseRecordAdapter(useRecordList);
                    LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
                    mBinding.recycleView.setLayoutManager(layoutManager);

                    mBinding.recycleView.setAdapter(useRecordAdapter);
                }else if(!response.body().getRet()){
//                    mBinding.nullTips.setVisibility(View.VISIBLE);
//                    mBinding.recycleView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseResult<List<UseRecord>>> call, Throwable t) {

            }
        });

    }

    public static  void startAction(Context context){
        Intent intent=new Intent(context,MyUseRecordActivity.class);
        context.startActivity(intent);
    }

}
