package com.esp_v6.mysocket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.esp_v6.GetDateActivity;
import com.esp_v6.MainActivity;
import com.esp_v6.mysocket.util.WifiAdmin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Administrator on 2015/6/24.
 */
public class connenAP extends Thread{
    public String values="";
    public ProgressDialog progressDialog;

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public MainActivity mainActivity;

    private BufferedReader br = null;
    private PrintWriter pw = null;
    Socket socket = null;
    public int num=0;
    public WifiAdmin wifiAdmin;
    public String SSID;//之前的ssid
    public String ssidPass;  //测试密码
    public String newssid;  //测试SSID
    public int status=0;  //检测 状态: 输入 1. ssid 错误  2. 输入 ssid 密码有错误. 3. 请打开设备.
    public static final String ESP_SSID="ESP";
    public static final String ESP_PASS="12345678";
    public Handler mHandler;
    public int wifistatusId; //连接wifi的id状态;
    public int reswifiId=0; //回复之前的wifi连接ID

    public void setValues(String values) {
        this.values = values;
    }

    public void setmHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public void setSsidPass(String ssidPass) {
        this.ssidPass = ssidPass;
    }

    public void setNewssid(String newssid) {

        this.newssid = newssid;
    }

    public void setWifiAdmin(WifiAdmin wifiAdmin) {

        this.wifiAdmin = wifiAdmin;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }


    /**
     * 检测 输入的ssid 以及密码是否正确.
     */
    public void checkSSid(){
        String nowssid  = wifiAdmin.getSSID();
        int statusid;
        int wifistatus;
        if(nowssid!="0x"){
            int ssidId = wifiAdmin.getNetWordId();
            reswifiId = ssidId;
            wifiAdmin.disConnectionWifi(ssidId);
        }

        if(wifiAdmin.findSSID(newssid)){
            //statusid = wifiAdmin.IsConfiguration(newssid);
            //暂时去掉这些
            statusid=-1;
            if(statusid == -1){
                statusid = wifiAdmin.AddWifiConfig(newssid,ssidPass);
            }
            wifiAdmin.ConnectASaveWifi(statusid);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if( wifiAdmin.getWifiInfos()!=0){
                //如果连接成功就断开当前的连接
                wifiAdmin.disConnectionWifi(statusid);
                status = 0;
            }else{
                status = 2;
            }
        }else{
            status=1;
        }
    }

    /**
     * 连接 当前设备wifi情况
     * @return
     */
    public void connetWifiAp(){
        int statusid;
        int wifistatus;
        if(wifiAdmin.findSSID(ESP_SSID)){
            statusid = wifiAdmin.IsConfiguration(ESP_SSID);
            if(statusid == -1){
                statusid = wifiAdmin.AddWifiConfig(ESP_SSID,ESP_PASS);
            }
            wifiAdmin.ConnectASaveWifi(statusid);
            wifistatusId =  statusid;
            boolean wail=true;
             while(wail) {
                 if (wifiAdmin.getWifiInfos() != 0) {
                     wail=false;
                     status = 0;
                 }
             }
        }else{
            status=1;
        }
    }

    @Override
    public void run() {
        System.out.println("客户端启动...");
        System.out.println("当接收到服务器端字符为 \"OK\" 的时候, 客户端将终止\n");
        int i = 0;
        Message message = new Message();
        checkSSid();
        //检测 状态: 输入 1. ssid 错误  2. 输入 ssid 密码有错误. 3. 请打开设备.
        if (status == 1) {
            message.what = 4;
            resetConnet( );
            mHandler.sendMessage(message);
        }
        if (status == 2) {
            message.what = 5;
            resetConnet( );
            mHandler.sendMessage(message);
        }
        if (status == 0) {
            connetWifiAp();
            if (status != 0) {
                resetConnet( );
                message.what = 1;
                mHandler.sendMessage(message);
            } else {
                while (true) {

                    Bundle bundle = new Bundle();
                    Message msg = Message.obtain();
                    try {
                        msg.what = 0x11;
                        bundle.clear();
                        //创建一个流套接字并将其连接到指定主机上的指定端口号
                        socket = new Socket("192.168.4.1", 1001);
                        //读取服务器端数据
                        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        //向服务器端发送数据
                        // DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                        pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                                socket.getOutputStream())));
                        // out.writeUTF(values);
                        pw.println(values);
                        pw.flush();
                        // out.close();
//                String str="";
//                if(num!=0)
//                 str= br.readLine();
//                num++;
                        Thread.sleep(1500);
                    } catch (Exception e) {
                        socket = null;
                        System.out.println("客户端异常:" + e.getMessage());

                        break;
                    } finally {
                        if (socket != null) {
                            try {
                                br.close();
                                pw.close();
                                socket.close();
                                //socket.close();
                            } catch (IOException e) {
                                socket = null;
                                System.out.println("客户端 finally 异常:" + e.getMessage());

                            }
                        } else {

                            try {
                                if (br != null)
                                    br.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (pw != null)
                                pw.close();
                            progressDialog.dismiss();
                            resetConnet( );
                            //wifiAdmin.disConnectionWifi(wifistatusId);
//                            if (SSID != null && !SSID.equals("")) {
//                                SSID = SSID.substring(1, SSID.length() - 1);
//                                if (!SSID.equals(CommonWifi.ESP_SSID)) {
//                                    wifiAdmin.connetGuration(SSID);
//                                }
//                            }
                            Intent internt = new Intent();
                            internt.setClass(mainActivity,
                                    GetDateActivity.class);
                            mainActivity.startActivity(internt);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 回复之前的 wifi连接
     */
    public void resetConnet( ){
        if(reswifiId!=0){
            wifiAdmin.ConnectASaveWifi(reswifiId);
        }
    }

}
