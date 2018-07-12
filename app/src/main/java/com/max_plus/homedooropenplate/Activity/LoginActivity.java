package com.max_plus.homedooropenplate.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.max_plus.homedooropenplate.R;
import com.max_plus.homedooropenplate.Tools.NetworkUtils;

public class LoginActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener {
    private EditText et_user, et_psd;
    private Button btn_login;
    private TextView tv_forget_psd, tv_regist, tv_error_string;
    private String userName, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        et_user = findViewById(R.id.et_user);
        et_user.setOnFocusChangeListener(this);


        et_psd = findViewById(R.id.et_psd);
        et_psd.setOnFocusChangeListener(this);


        tv_error_string = findViewById(R.id.tv_error_string);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        tv_forget_psd = findViewById(R.id.tv_forget_psd);
        tv_forget_psd.setOnClickListener(this);

        tv_regist = findViewById(R.id.tv_regist);
        tv_regist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                userName = et_user.getText().toString().trim();
                password = et_psd.getText().toString().trim();
                if (userName.length() <= 0 || password.length() <= 0) {
                    Toast.makeText(this, R.string.not_null, Toast.LENGTH_LONG).show();
                    return;
                }
                if (!NetworkUtils.checkNetWork(this)) {
                    Toast.makeText(this, R.string.no_network, Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            case R.id.tv_forget_psd:
                Intent intent = new Intent(this, ForgetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_regist:
                Intent intent2 = new Intent(this, RegisterActivity.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_user:
                if (!hasFocus) {
                    userName = et_user.getText().toString().trim();
                    if (!NetworkUtils.isChinaPhoneLegal(userName)) {
                        tv_error_string.setVisibility(View.VISIBLE);
                        tv_error_string.setText(R.string.error_phone);
                        btn_login.setEnabled(false);
                    } else {
                        tv_error_string.setVisibility(View.INVISIBLE);
                        btn_login.setEnabled(true);
                    }
                }
                break;
        }
    }
}
