package com.max_plus.homedooropenplate.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.max_plus.homedooropenplate.R;

public class FillInfoActivity extends Activity implements View.OnClickListener {
    private ImageButton btn_back;
    private TextView tv_next;
    private EditText et_userName;
    private TextView tv_sex, tv_type, xiaoqu, louyu, house;
    private String userNaame, xiaoquName = "s", louyuName = "s", houseName = "s";
    private int sex = 0, type = 0, houseid = 0, xiaoquId = 0, louyuId = 0;//sex=1 男 sex=2 女  type=1 户主  type=2 租户
    private Dialog chooseSexDialog, chooseTypeDialog;
    private final int XIAO_QU = 1;
    private final int XIAO_QU_FANHUI = 2;
    private final int LOU_YU = 3;
    private final int LOUY_FANHUI = 4;
    private final int HOUSE = 5;
    private final int HOUSE_BACK = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);
        initViews();
    }

    private void initViews() {
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        tv_next = findViewById(R.id.tv_next);
        tv_next.setOnClickListener(this);

        et_userName = findViewById(R.id.et_userName);
        userNaame = et_userName.getText().toString().trim();

        tv_sex = findViewById(R.id.tv_sex);
        tv_sex.setOnClickListener(this);

        tv_type = findViewById(R.id.tv_type);
        tv_type.setOnClickListener(this);

        xiaoqu = findViewById(R.id.xiaoqu);
        xiaoqu.setOnClickListener(this);

        louyu = findViewById(R.id.louyu);
        louyu.setOnClickListener(this);

        house = findViewById(R.id.house);
        house.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_next:
                userNaame = et_userName.getText().toString().trim();
                if (userNaame.equals("") || sex == 0 || type == 0 || houseid == 0) {
                    Toast.makeText(this, "请把信息补充完整再点下一步", Toast.LENGTH_LONG).show();
                    break;
                }
                Intent intent4 = new Intent(FillInfoActivity.this, UploadInfoImgActivity.class);
                intent4.putExtra("userName", userNaame);
                intent4.putExtra("sex", sex);
                intent4.putExtra("type", type);
                intent4.putExtra("houseid", houseid);
                startActivityForResult(intent4, HOUSE);
                break;
            case R.id.tv_sex:
                showChooseSexDialog();
                break;
            case R.id.tv_man:
                String choose_sex = "男";
                sex = 1;
                tv_sex.setText(choose_sex);
                chooseSexDialog.dismiss();
                break;
            case R.id.tv_woman:
                String choose_sex2 = "女";
                sex = 2;
                tv_sex.setText(choose_sex2);
                chooseSexDialog.dismiss();
                break;
            case R.id.tv_type:
                showChooseTypeDialog();
                break;
            case R.id.tv_owner:
                String choose_type1 = "户主";
                type = 1;
                tv_type.setText(choose_type1);
                chooseTypeDialog.dismiss();
                break;
            case R.id.tv_tenement:
                String choose_type2 = "租户";
                type = 2;
                tv_type.setText(choose_type2);
                chooseTypeDialog.dismiss();
                break;
            case R.id.xiaoqu:
                Intent intent = new Intent(FillInfoActivity.this, ChooseXiaoQuActivity.class);
                startActivityForResult(intent, XIAO_QU);
                break;
            case R.id.louyu:
                if (xiaoquName.equals("s")) {
                    Toast.makeText(this, "请先选择小区", Toast.LENGTH_LONG).show();
                    break;
                }
                Intent intent2 = new Intent(FillInfoActivity.this, ChooseLouYuActivity.class);
                intent2.putExtra("xiaoquId", xiaoquId);
                startActivityForResult(intent2, LOU_YU);
                break;
            case R.id.house:
                if (louyuName.equals("s")) {
                    Toast.makeText(this, "请先选择楼宇", Toast.LENGTH_LONG).show();
                    break;
                }
                Intent intent3 = new Intent(FillInfoActivity.this, ChooseHouseActivity.class);
                intent3.putExtra("louyuId", louyuId);
                startActivityForResult(intent3, HOUSE);
                break;
        }
    }

    private void showChooseTypeDialog() {
        chooseTypeDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.choose_type_dialog, null);
        root.findViewById(R.id.tv_owner).setOnClickListener(this);
        root.findViewById(R.id.tv_tenement).setOnClickListener(this);
        chooseTypeDialog.setContentView(root);
        Window dialogWindow = chooseTypeDialog.getWindow();
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
        chooseTypeDialog.show();
    }

    private void showChooseSexDialog() {
        chooseSexDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.choose_sex_dialog, null);
        root.findViewById(R.id.tv_man).setOnClickListener(this);
        root.findViewById(R.id.tv_woman).setOnClickListener(this);
        chooseSexDialog.setContentView(root);
        Window dialogWindow = chooseSexDialog.getWindow();
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
        chooseSexDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (requestCode == XIAO_QU) {
            xiaoquId = data.getIntExtra("xiaoquId", 0);
            xiaoquName = data.getStringExtra("xiaoquName");
            Log.d("xiaoquId+xiaoquName==>>", xiaoquId + "+" + xiaoquName);
            xiaoqu.setText(xiaoquName);
        } else if (requestCode == XIAO_QU_FANHUI) {

        } else if (requestCode == LOU_YU) {
            louyuId = data.getIntExtra("louyuId", 0);
            louyuName = data.getStringExtra("louyuName");
            Log.d("xiaoquId+xiaoquName==>>", louyuId + "+" + louyuName);
            louyu.setText(louyuName);
        } else if (requestCode == LOUY_FANHUI) {

        } else if (requestCode == HOUSE) {
            houseid = data.getIntExtra("houseId", 0);
            houseName = data.getStringExtra("houseName");
            Log.d("xiaoquId+xiaoquName==>>", houseid + "+" + houseName);
            house.setText(houseName);
        } else if (requestCode == HOUSE_BACK) {

        }
    }
}
