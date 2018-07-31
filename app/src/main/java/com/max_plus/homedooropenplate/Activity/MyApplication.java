package com.max_plus.homedooropenplate.Activity;

import android.app.Application;
import android.util.Log;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * For developer startup JPush SDK
 * <p>
 * 一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
 */
public class MyApplication extends Application {
    private static final String TAG = "JIGUANG";

    @Override
    public void onCreate() {
        Log.d(TAG, "[Application] onCreate");
        super.onCreate();
        x.Ext.init(this);

        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
    }
}
