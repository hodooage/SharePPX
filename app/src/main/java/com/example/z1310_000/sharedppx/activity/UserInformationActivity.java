package com.example.z1310_000.sharedppx.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.z1310_000.sharedppx.R;
import com.example.z1310_000.sharedppx.entity.User;
import com.example.z1310_000.sharedppx.request.EditUserInformationRequest;
import com.example.z1310_000.sharedppx.utils.Message;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.jar.Manifest;

import cz.msebera.android.httpclient.Header;

public class UserInformationActivity extends AppCompatActivity {
    private AsyncHttpClient client = new AsyncHttpClient();
    private EditUserInformationRequest editUserInformationRequest = new EditUserInformationRequest();

    private RelativeLayout itemNickName,itemSex,itemBirthday,itemEmail,itemPhonenum;
    private ImageButton returnBtn,userImage;

    private TextView nickName, userNickname, userSex, userPhonenum, userEmail, userBirthday, userWhat;

    //初始化性别选项为-1
    private int yourChoice = -1;

    /* 头像文件名 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

    //头像临时文件地址
    private Uri imageUri;


    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 200;
    private static int output_Y = 200;

    private User nowUser= DataSupport.findFirst(User.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        returnBtn= (ImageButton) findViewById(R.id.returnBtn);

        userImage = (ImageButton) findViewById(R.id.userImage);

        itemNickName = (RelativeLayout) findViewById(R.id.itemNickName);
        itemSex= (RelativeLayout) findViewById(R.id.itemSex);
        itemBirthday= (RelativeLayout) findViewById(R.id.itemBirthday);
        itemPhonenum= (RelativeLayout) findViewById(R.id.itemPhonenum);
        itemEmail= (RelativeLayout) findViewById(R.id.itemEmail);

        nickName = (TextView) findViewById(R.id.nickName);

        userNickname = (TextView) findViewById(R.id.userNickName);
        userSex = (TextView) findViewById(R.id.userSex);
        userPhonenum = (TextView) findViewById(R.id.userPhonenum);
        userEmail = (TextView) findViewById(R.id.userEmail);
        userBirthday = (TextView) findViewById(R.id.userBirthday);
        userWhat = (TextView) findViewById(R.id.userWhat);


    }

    private void initData() {
        if (null != DataSupport.findFirst(User.class)) {
            User user = DataSupport.findFirst(User.class);
            userNickname.setText(user.getNickname());
            userSex.setText(user.getSex());
            userBirthday.setText(user.getBirthday().toString());
            userPhonenum.setText(user.getPhonenum());
            userEmail.setText(user.getEmail());
            nickName.setText(user.getNickname());


            editUserInformationRequest.setId(user.getUid());
        } else {
            Toast.makeText(getApplicationContext(),"您还没有登录，请先登录",Toast.LENGTH_SHORT).show();
            LoginActivity.startAction(this);
        }


    }

    private void initListener() {
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        itemNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        itemSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSingleChoiceDialog();
            }
        });

        itemBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });


        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListDialog();
            }
        });



    }

    //选择相册中的图片
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(UserInformationActivity.this, "com.example.z1310_000.sharedppx.fileProvider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }

        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;
            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }
                break;
            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    cropRawPhoto(imageUri);
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");

            userImage.setImageBitmap(toRoundBitmap(photo));
        }
    }

    //设置圆形头像
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
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
                        choseHeadImageFromCameraCapture();
                        break;
                    case 1:
                        choseHeadImageFromGallery();
                        break;
                }

            }
        });
        listDialog.show();
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

                        editUserInformationRequest.setNickname(editText.getText().toString());

                        client.post(UserInformationActivity.this, editUserInformationRequest.getUrl(), editUserInformationRequest.getStringEntity(), "application/json", new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    if ("ok".equals(response.getString("result"))) {
                                        Toast.makeText(getApplicationContext(), "修改成功~", Toast.LENGTH_SHORT).show();
                                        nowUser.setNickname(editText.getText().toString());
                                        nowUser.save();
                                        initData();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "修改失败~", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                Toast.makeText(getApplicationContext(), "网络连接失败~", Toast.LENGTH_SHORT).show();
                            }
                        });
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
                        if (yourChoice != -1) {
                            editUserInformationRequest.setSex(items[yourChoice]);
                            client.post(UserInformationActivity.this,editUserInformationRequest.getUrl(),editUserInformationRequest.getStringEntity(),"application/json",new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    try {
                                        if ("ok".equals(response.getString("result"))) {
                                            Toast.makeText(getApplicationContext(), "修改成功~", Toast.LENGTH_SHORT).show();

                                            nowUser.setSex(items[yourChoice]);
                                            nowUser.save();
                                            initData();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "修改失败~", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                    Toast.makeText(getApplicationContext(), "网络连接失败~", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
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
                final String StringBirthday=year+"年"+(monthOfYear+1)
                        +"月"+dayOfMonth+"日";
                editUserInformationRequest.setBirthday(StringBirthday);
                client.post(UserInformationActivity.this,editUserInformationRequest.getUrl(),editUserInformationRequest.getStringEntity(),"application/json",new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if ("ok".equals(response.getString("result"))) {
                                Toast.makeText(getApplicationContext(), "修改成功~", Toast.LENGTH_SHORT).show();
                                nowUser.setBirthday(StringBirthday);
                                nowUser.save();
                                initData();
                            } else {
                                Toast.makeText(getApplicationContext(), "修改失败~", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(getApplicationContext(), "网络连接失败~", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        },year,month,day).show();

    }
}
