package com.example.z1310_000.sharedppx.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.databinding.ActivityUserInformationBinding;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.service.UserService;
import com.example.z1310_000.sharedppx.util.ToastUtil;
import com.example.z1310_000.sharedppx.utils.GlideImageLoader;
import com.example.z1310_000.sharedppx.utils.RetrofitUtil;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInformationActivity extends BaseActivity {
    private ActivityUserInformationBinding mBinding;

    private User user;

    private ImagePicker imagePicker;
    //初始化性别选项为-1
    private int yourChoice = -1;
    private int IMAGE_PICKER=200;
    private int REQUEST_CODE_SELECT=400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_user_information);

        initView();
        initData();
        initImageLoader();
        initListener();
    }

    private void initView() {
        initToolbar("我的信息");
    }

    private void initData() {
            user = DataSupport.findFirst(User.class);
            mBinding.userNickName.setText(user.getNickname());
            mBinding.userSex.setText(user.getSex());
            mBinding.userBirthday.setText(user.getBirthday().toString());
            mBinding.userPhonenum.setText(user.getPhonenum());
            mBinding.userEmail.setText(user.getEmail());
            mBinding.nickName.setText(user.getNickname());

            RequestOptions options = new RequestOptions()
                    .circleCrop()
                    .placeholder(R.drawable.userimagecircle)
                    .error(R.drawable.error)
                    .priority(Priority.HIGH);

            Glide.with(this)
                    .load(user.getImage())
                    .apply(options)
                    .into(mBinding.userImage);
    }

    private void initImageLoader(){
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    private void initListener() {

        mBinding.itemNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        mBinding.itemSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSingleChoiceDialog();
            }
        });

        mBinding.itemBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        mBinding.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListDialog();
            }
        });

    }



    public static void startAction(Context context) {
        Intent intent = new Intent(context, UserInformationActivity.class);
        context.startActivity(intent);
    }

    //showListDialog()展示列表对话框
    private void showListDialog() {
        final String[] items = {"拍照", "相册"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(UserInformationActivity.this);
        listDialog.setTitle("选择获取头像方式");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // which 下标从0开始
                // ...To-do
                switch (which) {
                    case 0:
                        Intent takePhotoIntent = new Intent(getApplicationContext(), ImageGridActivity.class);
                        takePhotoIntent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS,true); // 是否是直接打开相机
                        startActivityForResult(takePhotoIntent, REQUEST_CODE_SELECT);
                        break;
                    case 1:
                        Intent chooseImageIntent = new Intent(getApplicationContext(), ImageGridActivity.class);
                        startActivityForResult(chooseImageIntent, IMAGE_PICKER);
                        break;
                }

            }
        });
        listDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null){
                if(requestCode == IMAGE_PICKER||requestCode == REQUEST_CODE_SELECT) {
                    ArrayList<ImageItem> images = ( ArrayList<ImageItem> ) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    Toast.makeText(this, "is me", Toast.LENGTH_SHORT).show();
                    RequestOptions options = new RequestOptions()
                            .circleCrop()
                            .placeholder(R.drawable.userimagecircle)
                            .error(R.drawable.error)
                            .priority(Priority.HIGH);
                    Glide.with(this)
                            .load(images.get(0).path)
                            .apply(options)
                            .into(mBinding.userImage);
                    File file=new File(images.get(0).path);

                    uploadImage(file);
                }
            }else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // TODO: 2018/3/21 0021 上传图片还有问题，服务器收不到请求 
    private void uploadImage(File file){
        RequestBody requestFile=RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body= MultipartBody.Part.createFormData("file","asd",requestFile);

        UserService userService=UserService.util.getUserService();
        Call<ResponseBody> call=userService.uploadImage(user.getUid(), body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    //更改昵称对话框
    private void showInputDialog() {
    /*@setView 装入一个EditView
     */
        final EditText editText = new EditText(UserInformationActivity.this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(UserInformationActivity.this);
        inputDialog.setTitle("修改昵称").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }




    //性别选择单选框
    private void showSingleChoiceDialog() {

        final String[] items = {"男", "女"};

        AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(UserInformationActivity.this);
        singleChoiceDialog.setTitle("修改性别");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourChoice = which;
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        singleChoiceDialog.show();
    }

    //修改生日对话框
    private void showDateDialog(){
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {

            //monthOfYear的值为0-11,所以使用的时候要加一
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {


            }
        },year,month,day).show();

    }
}
