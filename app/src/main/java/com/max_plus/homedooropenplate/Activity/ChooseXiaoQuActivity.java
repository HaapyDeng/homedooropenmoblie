package com.max_plus.homedooropenplate.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.max_plus.homedooropenplate.Adapter.ChooseXiaoQuAdapter;
import com.max_plus.homedooropenplate.Bean.XiaoQuBean;
import com.max_plus.homedooropenplate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ChooseXiaoQuActivity extends Activity implements View.OnClickListener {
    private ImageButton btn_back;
    private TextView tv_save;
    private EditText et_search;
    private int xiaoqu_ID;
    private String xiaoqu_Name;
    private ListView list_view;
    List<XiaoQuBean> xiaoqu = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_xiao_qu);
        initViews();
        initDate();
    }


    private void initViews() {
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        tv_save = findViewById(R.id.tv_save);
        tv_save.setOnClickListener(this);

        et_search = findViewById(R.id.et_search);
        et_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                Drawable drawable = et_search.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (drawable == null)
                    return false;
                //如果不是按下事件，不再处理
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > et_search.getWidth()
                        - et_search.getPaddingRight()
                        - drawable.getIntrinsicWidth()) {
                    String searchString = et_search.getText().toString().trim();
                    if (searchString.equals("")) {
                        Toast.makeText(ChooseXiaoQuActivity.this, "请输入小区名再点击搜索", Toast.LENGTH_LONG).show();
                        return false;
                    } else {
                        doSearchXiaoQu(searchString);
                    }
                }
                return false;
            }
        });

        list_view = findViewById(R.id.list_view);
    }

    private void doSearchXiaoQu(String searchString) {
        String url = getResources().getString(R.string.local_url) + "/v1/application" + "?" + "p=" + searchString;
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
                        JSONObject data = response.getJSONObject("data");
                        JSONObject community = data.getJSONObject("community");
                        JSONArray list = community.getJSONArray("list");
                        Log.d("list==>>>", list.toString());
                        if (list.length() == 0) {
                            Toast.makeText(ChooseXiaoQuActivity.this, "没有符合的小区", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            xiaoqu.clear();
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject jsonObject = list.getJSONObject(i);
                                xiaoqu.add(new XiaoQuBean(jsonObject.getInt("id"), jsonObject.getString("name")));
                            }
                            final ChooseXiaoQuAdapter chooseXiaoQuAdapter = new ChooseXiaoQuAdapter(ChooseXiaoQuActivity.this);
                            chooseXiaoQuAdapter.setDatas(xiaoqu);
                            list_view.setAdapter(chooseXiaoQuAdapter);
                            list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                int currentNum = -1;

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    for (XiaoQuBean person : xiaoqu) {
                                        person.setChecked(false);
                                    }

                                    if (currentNum == -1) { //选中
                                        xiaoqu.get(position).setChecked(true);
                                        currentNum = position;
                                        xiaoqu_ID = xiaoqu.get(position).getXiaoqu_Id();
                                        xiaoqu_Name = xiaoqu.get(position).getXiaoqu_Name();
                                    } else if (currentNum == position) { //同一个item选中变未选中
                                        for (XiaoQuBean person : xiaoqu) {
                                            person.setChecked(false);
                                        }
                                        currentNum = -1;
                                    } else if (currentNum != position) { //不是同一个item选中当前的，去除上一个选中的
                                        for (XiaoQuBean person : xiaoqu) {
                                            person.setChecked(false);
                                        }
                                        xiaoqu.get(position).setChecked(true);
                                        currentNum = position;
                                        xiaoqu_ID = xiaoqu.get(position).getXiaoqu_Id();
                                        xiaoqu_Name = xiaoqu.get(position).getXiaoqu_Name();
                                    }

                                    chooseXiaoQuAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(ChooseXiaoQuActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ChooseXiaoQuActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ChooseXiaoQuActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ChooseXiaoQuActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    private void initDate() {
        String url = getResources().getString(R.string.local_url) + "/v1/application";
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
                        JSONObject data = response.getJSONObject("data");
                        JSONObject community = data.getJSONObject("community");
                        JSONArray list = community.getJSONArray("list");
                        Log.d("list==>>>", list.toString());
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject jsonObject = list.getJSONObject(i);
                            xiaoqu.add(new XiaoQuBean(jsonObject.getInt("id"), jsonObject.getString("name")));
                        }
                        final ChooseXiaoQuAdapter chooseXiaoQuAdapter = new ChooseXiaoQuAdapter(ChooseXiaoQuActivity.this);
                        chooseXiaoQuAdapter.setDatas(xiaoqu);
                        list_view.setAdapter(chooseXiaoQuAdapter);
                        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            int currentNum = -1;

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                for (XiaoQuBean person : xiaoqu) {
                                    person.setChecked(false);
                                }

                                if (currentNum == -1) { //选中
                                    xiaoqu.get(position).setChecked(true);
                                    currentNum = position;
                                    xiaoqu_ID = xiaoqu.get(position).getXiaoqu_Id();
                                    xiaoqu_Name = xiaoqu.get(position).getXiaoqu_Name();
                                } else if (currentNum == position) { //同一个item选中变未选中
                                    for (XiaoQuBean person : xiaoqu) {
                                        person.setChecked(false);
                                    }
                                    currentNum = -1;
                                } else if (currentNum != position) { //不是同一个item选中当前的，去除上一个选中的
                                    for (XiaoQuBean person : xiaoqu) {
                                        person.setChecked(false);
                                    }
                                    xiaoqu.get(position).setChecked(true);
                                    currentNum = position;
                                    xiaoqu_ID = xiaoqu.get(position).getXiaoqu_Id();
                                    xiaoqu_Name = xiaoqu.get(position).getXiaoqu_Name();
                                }

                                chooseXiaoQuAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        Toast.makeText(ChooseXiaoQuActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ChooseXiaoQuActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ChooseXiaoQuActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ChooseXiaoQuActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_save:
                //设置返回上一个页面的数据
                if (xiaoqu_Name.length() == 0) {
                    Toast.makeText(ChooseXiaoQuActivity.this, "请选择小区", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    setBackData();
                }
                break;
        }
    }

    private void setBackData() {
        Intent intentTemp = new Intent();
        intentTemp.putExtra("xiaoquId", xiaoqu_ID);
        intentTemp.putExtra("xiaoquName", xiaoqu_Name);
        setResult(1, intentTemp);
        finish();
    }
}
