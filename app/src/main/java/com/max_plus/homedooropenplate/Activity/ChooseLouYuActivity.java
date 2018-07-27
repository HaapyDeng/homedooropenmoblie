package com.max_plus.homedooropenplate.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.max_plus.homedooropenplate.Adapter.ChooseLouYuAdapter;
import com.max_plus.homedooropenplate.Adapter.ChooseXiaoQuAdapter;
import com.max_plus.homedooropenplate.Bean.LouYuBean;
import com.max_plus.homedooropenplate.Bean.XiaoQuBean;
import com.max_plus.homedooropenplate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ChooseLouYuActivity extends Activity implements View.OnClickListener {
    private ImageButton btn_back;
    private TextView tv_save;
    private ListView list_view;
    List<LouYuBean> louyu = new ArrayList<>();
    private int xiaoquid, louyuId;
    private String louyuName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_lou_yu);
        Intent intent = getIntent();
        xiaoquid = intent.getIntExtra("xiaoquId", 0);
        initViews();
        initDate();
    }

    private void initDate() {
        String url = getResources().getString(R.string.local_url) + "/v1/application/community/building/" + xiaoquid;
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("M-Api-Token", token);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response==>>>", response.toString());
                try {
                    int code = response.getInt("code");
                    if (code == 0) {
                        JSONArray list = response.getJSONArray("data");
                        Log.d("list==>>>", list.toString());
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject jsonObject = list.getJSONObject(i);
                            louyu.add(new LouYuBean(jsonObject.getInt("id"), jsonObject.getString("name")));
                        }
                        final ChooseLouYuAdapter chooseLouYuAdapter = new ChooseLouYuAdapter(ChooseLouYuActivity.this);
                        chooseLouYuAdapter.setDatas(louyu);
                        list_view.setAdapter(chooseLouYuAdapter);
                        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            int currentNum = -1;

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                for (LouYuBean person : louyu) {
                                    person.setChecked(false);
                                }

                                if (currentNum == -1) { //选中
                                    louyu.get(position).setChecked(true);
                                    currentNum = position;
                                    louyuId = louyu.get(position).getLouyu_Id();
                                    louyuName = louyu.get(position).getLouyu_Name();
                                } else if (currentNum == position) { //同一个item选中变未选中
                                    for (LouYuBean person : louyu) {
                                        person.setChecked(false);
                                    }
                                    currentNum = -1;
                                } else if (currentNum != position) { //不是同一个item选中当前的，去除上一个选中的
                                    for (LouYuBean person : louyu) {
                                        person.setChecked(false);
                                    }
                                    louyu.get(position).setChecked(true);
                                    currentNum = position;
                                    louyuId = louyu.get(position).getLouyu_Id();
                                    louyuName = louyu.get(position).getLouyu_Name();
                                }

                                chooseLouYuAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        Toast.makeText(ChooseLouYuActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ChooseLouYuActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ChooseLouYuActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ChooseLouYuActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private void initViews() {
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        tv_save = findViewById(R.id.tv_save);
        tv_save.setOnClickListener(this);
        list_view = findViewById(R.id.list_view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_save:
                //设置返回上一个页面的数据
                if (louyuName.length() == 0) {
                    Toast.makeText(ChooseLouYuActivity.this, "请选择小区", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    setBackData();
                }
                break;
        }
    }

    private void setBackData() {
        Intent intentTemp = new Intent();
        intentTemp.putExtra("louyuId", louyuId);
        intentTemp.putExtra("louyuName", louyuName);
        setResult(3, intentTemp);
        finish();
    }
}
