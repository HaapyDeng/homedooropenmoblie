package com.max_plus.homedooropenplate.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.max_plus.homedooropenplate.R;
import com.max_plus.homedooropenplate.Tools.CountDownButton;
import com.max_plus.homedooropenplate.Tools.NetworkUtils;

public class RegisterActivity extends Activity implements View.OnClickListener {
    private ImageButton btn_back;
    private EditText et_phone, et_code, et_psd;
    private Button btn_regist;
    private String userName, code, password;
    private CountDownButton getcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
    }

    private void initViews() {
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        et_phone = findViewById(R.id.et_phone);

        et_code = findViewById(R.id.et_code);

        et_psd = findViewById(R.id.et_psd);

        getcode = findViewById(R.id.tv_getcode);
        getcode.setOnClickListener(this);

        btn_regist = findViewById(R.id.btn_regist);
        btn_regist.setOnClickListener(this);


    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_getcode:
                userName = et_phone.getText().toString().trim();
                if (userName.length() <= 0) {
                    Toast.makeText(this, R.string.phone_null, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!NetworkUtils.isChinaPhoneLegal(userName)) {
                    Toast.makeText(this, R.string.error_phone, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!NetworkUtils.checkNetWork(this)) {
                    Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
                    return;
                }
                //这里判断是否倒计时结束，避免在倒计时时多次点击导致重复请求接口
                if (getcode.isFinish()) {
                    doGetCode(userName);
                    //发送成功弹出自定义Toast
                    ToastMessage("发送成功");
                    //发送验证码请求成功后调用
                    getcode.start();
                }

                break;
            case R.id.btn_regist:
                code = et_code.getText().toString().trim();
                password = et_psd.getText().toString().trim();
                if (userName.length() <= 0 || code.length() <= 0 || password.length() <= 0) {
                    Toast.makeText(this, R.string.please_replenish, Toast.LENGTH_LONG).show();
                    break;
                }
                if (!NetworkUtils.checkNetWork(this)) {
                    Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
                    return;
                }
                doRegister(userName, code, password);
                break;

        }
    }

    //注册
    private void doRegister(String userName, String code, String password) {
    }

    //获取验证码
    private void doGetCode(String userName) {

    }

    /**
     * 将Toast封装在一个方法中，在其它地方使用时直接输入要弹出的内容即可
     */
    private void ToastMessage(String messages) {
        //LayoutInflater的作用：对于一个没有被载入或者想要动态载入的界面，都需要LayoutInflater.inflate()来载入，LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化
        LayoutInflater inflater = getLayoutInflater();//调用Activity的getLayoutInflater()
        View view = inflater.inflate(R.layout.toast_style, null); //加載layout下的布局
        ImageView iv = view.findViewById(R.id.tvImageToast);
        iv.setImageResource(R.mipmap.toase_ic);//显示的图片
//        TextView title = view.findViewById(R.id.tvTitleToast);
//        title.setText(titles); //toast的标题
        TextView text = view.findViewById(R.id.tvTextToast);
        text.setText(messages); //toast内容
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 12, 20);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
        toast.setDuration(Toast.LENGTH_LONG);//setDuration方法：设置持续时间，以毫秒为单位。该方法是设置补间动画时间长度的主要方法
        toast.setView(view); //添加视图文件
        toast.show();
    }

}
