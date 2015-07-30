package com.esp_v6.thread.ConnonWifi;

/**
 * Created by Administrator on 2015/7/29.
 */
public class WifiConst {
    // Wifi状态 详细
    public static final int WifiSearchTimeOut = 0;// 搜索超时
    public static final int WifiScanResult = 1;// 搜索到wifi返回结果
    public static final int WifiConnectResult = 2;// 连接上wifi热点
    public static final int WifiCreateAPResult = 3;// 创建热点结果
    public static final int WifiUserResult = 4;// 用户上线人数更新命令(待定)
    public static final int WifiConnected = 5;// 连接或断开wifi，3.5秒后刷新adapter
    public static final int WifiConnectting = 6;// 连接热点中
}
