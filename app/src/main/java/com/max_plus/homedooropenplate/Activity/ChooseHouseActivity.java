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
import com.max_plus.homedooropenplate.Adapter.ChooseDanYuanAdapter;
import com.max_plus.homedooropenplate.Adapter.ChooseHouseAdapter;
import com.max_plus.homedooropenplate.Adapter.ChooseLouYuAdapter;
import com.max_plus.homedooropenplate.Bean.DanYuanBean;
import com.max_plus.homedooropenplate.Bean.HouseBean;
import com.max_plus.homedooropenplate.Bean.LouYuBean;
import com.max_plus.homedooropenplate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ChooseHouseActivity extends Activity implements View.OnClickListener {
    private ImageButton btn_back;
    private TextView tv_save;
    private ListView list_view;
    List<DanYuanBean> danyuan = new ArrayList<>();
    List<HouseBean> house = new ArrayList<>();
    private int louyuId, danyuanId, houseId, floor_num;
    private String danyuanName, houseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_house);
        Intent intent = getIntent();
        louyuId = intent.getIntExtra("louyuId", 0);
        initViews();
        initDate();
    }

    private void initDate() {
        String url = getResources().getString(R.string.local_url) + "/v1/application/community/unit/" + louyuId;
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
                            danyuan.add(new DanYuanBean(jsonObject.getInt("id"), jsonObject.getString("name")));
                        }
                        final ChooseDanYuanAdapter chooseDanYuanAdapter = new ChooseDanYuanAdapter(ChooseHouseActivity.this);
                        chooseDanYuanAdapter.setDatas(danyuan);
                        list_view.setAdapter(chooseDanYuanAdapter);
                        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            int currentNum = -1;

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                for (DanYuanBean person : danyuan) {
                                    person.setChecked(false);
                                }

                                if (currentNum == -1) { //选中
                                    danyuan.get(position).setChecked(true);
                                    currentNum = position;
                                    danyuanId = danyuan.get(position).getDanyuan_Id();
                                    danyuanName = danyuan.get(position).getDanyuan_Name();
                                    doGetHouse(danyuanId);
                                } else if (currentNum == position) { //同一个item选中变未选中
                                    for (DanYuanBean person : danyuan) {
                                        person.setChecked(false);
                                    }
                                    currentNum = -1;
                                } else if (currentNum != position) { //不是同一个item选中当前的，去除上一个选中的
                                    for (DanYuanBean person : danyuan) {
                                        person.setChecked(false);
                                    }
                                    danyuan.get(position).setChecked(true);
                                    currentNum = position;
                                    danyuanId = danyuan.get(position).getDanyuan_Id();
                                    danyuanName = danyuan.get(position).getDanyuan_Name();
                                    doGetHouse(danyuanId);
                                }

                                chooseDanYuanAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        Toast.makeText(ChooseHouseActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ChooseHouseActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ChooseHouseActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ChooseHouseActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private void doGetHouse(int danyuanId) {
        String url = getResources().getString(R.string.local_url) + "/v1/application/community/house" + "?uid=" + danyuanId;
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
                        JSONObject object = response.getJSONObject("data");
                        JSONArray list = object.getJSONArray("list");
                        Log.d("list==>>>", list.toString());
                        if (list.length() == 0) {
                            Toast.makeText(ChooseHouseActivity.this, "此单元没有住户", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            danyuan.clear();
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject jsonObject = list.getJSONObject(i);
                                house.add(new HouseBean(jsonObject.getInt("id"), jsonObject.getString("name"), jsonObject.getInt("floor_num")));
                            }
                            final ChooseHouseAdapter chooseHouseAdapter = new ChooseHouseAdapter(ChooseHouseActivity.this);
                            chooseHouseAdapter.setDatas(house);
                            list_view.setAdapter(chooseHouseAdapter);
                            list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                int currentNum = -1;

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    for (HouseBean person : house) {
                                        person.setChecked(false);
                                    }

                                    if (currentNum == -1) { //选中
                                        house.get(position).setChecked(true);
                                        currentNum = position;
                                        floor_num = house.get(position).getFloor_num();
                                        houseId = house.get(position).getHouse_Id();
                                        houseName = house.get(position).getHouse_Name();
                                    } else if (currentNum == position) { //同一个item选中变未选中
                                        for (HouseBean person : house) {
                                            person.setChecked(false);
                                        }
                                        currentNum = -1;
                                    } else if (currentNum != position) { //不是同一个item选中当前的，去除上一个选中的
                                        for (HouseBean person : house) {
                                            person.setChecked(false);
                                        }
                                        house.get(position).setChecked(true);
                                        currentNum = position;
                                        floor_num = house.get(position).getFloor_num();
                                        houseId = house.get(position).getHouse_Id();
                                        houseName = house.get(position).getHouse_Name();
                                    }

                                    chooseHouseAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(ChooseHouseActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ChooseHouseActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ChooseHouseActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ChooseHouseActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
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
                if (houseName.length() == 0) {
                    Toast.makeText(ChooseHouseActivity.this, "请选择住户", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    setBackData();
                }
                break;
        }
    }

    private void setBackData() {
        Intent intentTemp = new Intent();
        intentTemp.putExtra("houseId", houseId);
        intentTemp.putExtra("houseName", danyuanName + "单元" + floor_num + "楼" + houseName);
        setResult(5, intentTemp);
        finish();
    }
}
