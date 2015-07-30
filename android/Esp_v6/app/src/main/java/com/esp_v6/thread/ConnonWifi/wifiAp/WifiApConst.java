package com.esp_v6.thread.ConnonWifi.wifiAp;

/**
 * Created by Administrator on 2015/7/28.
 */
public class WifiApConst {
    // Wifi状态 粗略
    public static final int CLOSE = 0x001;
    public static final int SEARCH = 0x002;
    public static final int CREATE = 0x003;
    public static final int NOTHING = 0x004;
    // Wifi状态 详细
    public static final int ApSearchTimeOut = 0;// 搜索超时
    public static final int ApScanResult = 1;// 搜索到wifi返回结果
    public static final int ApConnectResult = 2;// 连接上wifi热点
    public static final int ApCreateAPResult = 3;// 创建热点结果
    public static final int ApUserResult = 4;// 用户上线人数更新命令(待定)
    public static final int ApConnected = 5;// 连接或断开wifi，3.5秒后刷新adapter
    public static final int ApConnectting = 6;// 连接热点中
    //wifi ap 密码以及ssid
    public static final String AP_SSID="apssid";  //热点的SSOD
    public static final String AP_PASS="appass";  //热点的密码
    public static final String STA_SSID="stassid";  //路由器ssid
    public static final String STA_PASS="stapass";  //路由器密码
    //修改发送给服务器的规格方式或者 下位机的格式 不管是修改还是发送,都要以这个格式.
    public static final String SEND_CONTENT="";  //STA:AT+CWJAP="TD","12345678"NAPAT+CWJAP="TD","12345678"
    // WifiAP 初始值 参数
    public static final String FIRST_OPEN_KEY = "version";
    public static final String WIFI_AP_HEADER = "ESP_";
    public static final String WIFI_AP_PASSWORD = "wifiAp123";

}
