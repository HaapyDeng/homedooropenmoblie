package com.max_plus.homedooropenplate.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.max_plus.homedooropenplate.R;
import com.max_plus.homedooropenplate.Tools.TakePhotoUtils;
import com.max_plus.homedooropenplate.View.DrawTextImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class UploadInfoImgActivity extends Activity implements View.OnClickListener {

    private ImageButton btn_back;
    private TextView tv_save;
    private DrawTextImageView tv_frontimg, tv_backimg;
    private Dialog chooseCameraDialog;
    private File outputImagepath;//存储拍完照后的图片
    private Bitmap orc_bitmap;//拍照和相册获取图片的Bitmap
    private static final int TAKE_PHOTO_REQUEST_ONE = 111;
    private static final int TAKE_PHOTO_REQUEST_TWO = 222;
    private static final int TAKE_PHOTO_REQUEST_THREE = 333;
    private static final int TAKE_PHOTO_REQUEST_FROU = 4444;
    private static int FLAG = 1;
    private Uri imageUri1, imageUri2;
    private Bitmap bitmap1, bitmap2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_info_img);
        initView();
    }

    private void initView() {
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        tv_save = findViewById(R.id.tv_save);
        tv_save.setOnClickListener(this);

        tv_frontimg = findViewById(R.id.tv_frontimg);
        tv_frontimg.setDrawText("上传身份证正面照片");
        tv_frontimg.setDrawTextColorResourse(R.color.sfz);
        tv_frontimg.setDrawLocalXY(250, 120);
        tv_frontimg.setDrawTextSize(16);
        tv_frontimg.setOnClickListener(this);

        tv_backimg = findViewById(R.id.tv_backimg);
        tv_backimg.setDrawText("上传身份证背面照片");
        tv_backimg.setDrawTextColorResourse(R.color.sfz);
        tv_backimg.setDrawLocalXY(250, 120);
        tv_backimg.setDrawTextSize(16);
        tv_backimg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_save:
                break;
            case R.id.tv_frontimg:
                showChooseCameraDialog1();
                break;
            case R.id.tv_backimg:
                showChooseCameraDialog2();
                break;
        }

    }

    private void doUpLoad(String url) {
        File file = new File(url);
        RequestParams requestParams = new RequestParams();
        try {
            requestParams.put("file", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String url2 = getResources().getString(R.string.local_url) + "/upload";
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("M-Api-Token", token);
        client.post(url2, requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("response==>>>", responseBody.toString());
                // 上传成功后要做的工作
                Toast.makeText(UploadInfoImgActivity.this, "上传成功", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                super.onPostProcessResponse(instance, response);
                Log.d("response==>>>", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // 上传成功后要做的工作
                Toast.makeText(UploadInfoImgActivity.this, "上传失败", Toast.LENGTH_LONG).show();
                FLAG = 0;
            }
        });
    }

    private void doTakePhoto1() {
        try {
            imageUri1 = TakePhotoUtils.takePhoto(UploadInfoImgActivity.this, TAKE_PHOTO_REQUEST_ONE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doTakePhoto2() {
        try {
            imageUri2 = TakePhotoUtils.takePhoto(UploadInfoImgActivity.this, TAKE_PHOTO_REQUEST_TWO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO_REQUEST_ONE:
                if (resultCode == RESULT_CANCELED) {
//                    Toast.makeText(UploadInfoImgActivity.this, "点击取消从相册选择", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d("url==?>>", imageUri1.toString());
                bitmap1 = BitmapFactory.decodeFile(imageUri1.getPath(), getOptions(imageUri1.getPath()));

                doUpLoad(String.valueOf(imageUri1));
                if (FLAG == 1) {
                    tv_frontimg.setImageBitmap(bitmap1);
                    tv_frontimg.setDrawText("");
                }
                break;
            case TAKE_PHOTO_REQUEST_TWO:
                if (resultCode == RESULT_CANCELED) {
//                    Toast.makeText(UploadInfoImgActivity.this, "点击取消从相册选择", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d("url==?>>", imageUri2.toString());
                bitmap2 = BitmapFactory.decodeFile(imageUri2.getPath(), getOptions(imageUri2.getPath()));
                doUpLoad(String.valueOf(imageUri2));
                if (FLAG == 1) {
                    tv_backimg.setImageBitmap(bitmap1);
                    tv_backimg.setDrawText("");
                }
                break;
            case TAKE_PHOTO_REQUEST_THREE:
                if (resultCode == RESULT_CANCELED) {
//                    Toast.makeText(UploadInfoImgActivity.this, "点击取消从相册选择", Toast.LENGTH_LONG).show();
                    return;
                }
                imageUri1 = data.getData();
                Log.d("url==?>>", imageUri1.toString());
//                Bitmap bitmap = BitmapFactory.decodeFile(imageUri1.getPath(), getOptions(imageUri1.getPath()));

                doUpLoad(String.valueOf(imageUri1));
                if (FLAG == 1) {
                    tv_frontimg.setImageBitmap(bitmap1);
                    tv_frontimg.setDrawText("");
                }
                break;
            case TAKE_PHOTO_REQUEST_FROU:
                if (resultCode == RESULT_CANCELED) {
//                    Toast.makeText(UploadInfoImgActivity.this, "点击取消从相册选择", Toast.LENGTH_LONG).show();
                    return;
                }
                imageUri2 = data.getData();
                Log.d("url==?>>", imageUri2.toString());
//                Bitmap bitmap2 = BitmapFactory.decodeFile(imageUri2.getPath(), getOptions(imageUri1.getPath()));
                doUpLoad(String.valueOf(imageUri2));
                if (FLAG == 1) {
                    tv_backimg.setImageBitmap(bitmap1);
                    tv_backimg.setDrawText("");
                }
                break;
        }
    }

    /**
     * 获取压缩图片的options
     *
     * @return
     */
    public static BitmapFactory.Options getOptions(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 4;      //此项参数可以根据需求进行计算
        options.inJustDecodeBounds = false;

        return options;
    }

    /*
    弹出选择拍照还是相册
     */
    private void showChooseCameraDialog1() {
        chooseCameraDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.choose_camera_dialog, null);
        root.findViewById(R.id.tv_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseCameraDialog.dismiss();
                doTakePhoto1();
            }
        });
        root.findViewById(R.id.tv_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseCameraDialog.dismiss();
                pickImageFromAlbum1();
            }
        });
        chooseCameraDialog.setContentView(root);
        Window dialogWindow = chooseCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        chooseCameraDialog.show();
    }

    private void pickImageFromAlbum1() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, TAKE_PHOTO_REQUEST_THREE);
    }

    private void pickImageFromAlbum2() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, TAKE_PHOTO_REQUEST_FROU);
    }

    /*
 弹出选择拍照还是相册
  */
    private void showChooseCameraDialog2() {
        chooseCameraDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.choose_camera_dialog, null);
        root.findViewById(R.id.tv_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseCameraDialog.dismiss();
                doTakePhoto2();
            }
        });
        root.findViewById(R.id.tv_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseCameraDialog.dismiss();
                pickImageFromAlbum2();
            }
        });
        chooseCameraDialog.setContentView(root);
        Window dialogWindow = chooseCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        chooseCameraDialog.show();
    }
}
