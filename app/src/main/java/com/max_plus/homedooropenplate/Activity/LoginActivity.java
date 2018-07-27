package com.max_plus.homedooropenplate.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.max_plus.homedooropenplate.R;
import com.max_plus.homedooropenplate.Tools.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

import cz.msebera.android.httpclient.Header;

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
                doLogin(userName, password);
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

    private void doLogin(String userName, String password) {
        String url = getResources().getString(R.string.local_url) + "/login";
        RequestParams params = new RequestParams();
        params.put("mobile", new BigInteger(userName));
        params.put("password", password);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        JSONObject data = response.getJSONObject("data");
                        int is_go = data.getInt("is_go");
                        int audit_status = data.getInt("audit_status");
                        String token = data.getString("access_token");
                        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", token);
                        editor.commit();
                        if (is_go == 0) {
                            Intent intent = new Intent(LoginActivity.this, FillInfoActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(LoginActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(LoginActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(LoginActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
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
