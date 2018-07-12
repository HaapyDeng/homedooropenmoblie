package com.max_plus.homedooropenplate.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.max_plus.homedooropenplate.Adapter.GuideViewPagerAdapter;
import com.max_plus.homedooropenplate.R;
import com.max_plus.homedooropenplate.View.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {
    private ViewPager vp;
    private GuideViewPagerAdapter viewPagerAdapter;
    private List<View> views;

    private CircleImageView[] dots;
    private int[] ids = {R.id.iv1, R.id.iv2, R.id.iv3};
    private Button btnEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_guide);
        intiView();
        initDots();
        enter();
    }

    private void intiView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.guideone, null));
        views.add(inflater.inflate(R.layout.guidetwo, null));
        views.add(inflater.inflate(R.layout.guidethree, null));

        viewPagerAdapter = new GuideViewPagerAdapter(views, this);

        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(viewPagerAdapter);//获取适配器中的数据

        vp.setOnPageChangeListener(this);//添加回调函数进行监听
    }

    private void enter() {
        btnEnter = (Button) views.get(2).findViewById(R.id.btnEnter);
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //初始化小圆点的ImageView
    private void initDots() {
        dots = new CircleImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            dots[i] = findViewById(ids[i]);
        }
    }

    //当页面被滑动调用
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //当前新的页面被选中时调用
    @SuppressLint("NewApi")
    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < views.size(); i++) {
            if (position == i) {
//设置小圆点为选中状态
                dots[i].setImageDrawable(getDrawable(R.drawable.dot_blue));
            } else {
//设置小圆点为未选中状态
                dots[i].setImageDrawable(getDrawable(R.drawable.dot_grey));

            }
        }
    }

    //活动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

