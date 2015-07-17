package com.esp_v6.thread.ConnonWifi;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.esp_v6.mysocket.util.WifiAdmin;

/**
 * Created by Administrator on 2015/7/9.
 */
public class CommonWifi extends Thread {
    public static final String ESP_SSID="ESP";
    public boolean checkSSID;
    public WifiAdmin wifiAdmin;
    Handler mHandler;

    public void setCheckSSID(boolean checkSSID) {
        this.checkSSID = checkSSID;
    }

    public CommonWifi(WifiAdmin wifiAdmin, Handler mHandler){
        this.wifiAdmin = wifiAdmin;
        this.mHandler = mHandler;
    }

    @Override
    public void run() {
        while(checkSSID){
            Message message=new Message();
            // SSID = wifiAdmin.getSSID();
            if(wifiAdmin.findSSID(ESP_SSID)){
                String ssids = wifiAdmin.getSSID();
                if(ssids !=null && ssids.indexOf(ESP_SSID) == -1) {
                    int status = wifiAdmin.IsConfiguration(ESP_SSID);
                    boolean types = true;
                    if (status != -1) {
                        types = wifiAdmin.ConnectASaveWifi(status);
                        if (types) {
                            message.what = 3;
                            //  checkSSID = false;
                        } else {
                            message.what = 2;
                        }
                    } else {
                        status = wifiAdmin.AddWifiConfig(ESP_SSID, "12345678");
                        if (status != -1) {
                            types = wifiAdmin.ConnectASaveWifi(status);
                            if (types) {
                                message.what = 3;
                                //  checkSSID = false;
                            } else {
                                message.what = 2;
                            }
                        } else {
                            message.what = 2;
                        }
                    }
                }else{
                    if(ssids!=null && ssids.indexOf(ESP_SSID) != -1)
                        message.what = 3;
                    else
                        message.what = 1;
                }
            }else{
                message.what=1;
                // checkSSID = false;
            }
            mHandler.sendMessage(message);
        }
        if(!checkSSID){
            Log.e("1111", "111111111");
        }
    }

}
