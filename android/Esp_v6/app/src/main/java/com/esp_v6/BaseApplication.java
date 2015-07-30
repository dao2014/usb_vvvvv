package com.esp_v6;

import android.app.Application;

/**
 * Created by Administrator on 2015/7/29.
 */
public class BaseApplication extends Application {
    private static BaseApplication instance; // 唯一实例
//    protected SharedFileUtils sharedFileUtils;

    /**
     * <p>
     * 获取BaseApplication实例
     * <p>
     * 单例模式，返回唯一实例
     *
     * @return instance
     */
    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (instance == null) {
            instance = this;
        }

    }




}
