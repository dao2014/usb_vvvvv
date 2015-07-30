package com.esp_v6.mysocket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.esp_v6.mysocket.util.SocketUtil;
import com.esp_v6.thread.ConnonWifi.wifiAp.WifiApConst;
import com.esp_v6.util.SharedFileUtils;
import com.esp_v6.util.WifiUtils;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Administrator on 2015/6/24.
 */
public class GetDateSocket extends Thread{
    public static final String IP_ADDR = "192.168.6.100";//服务器地址
    public static final int PORT = 1001;//服务器端口号
    public Handler myHandler;
    public boolean isKeepAlive=true;
    public WifiUtils mWifiUtils;
    Socket socket = null;
    public SharedFileUtils spf;

    public void setMyHandler( Handler handler ){
        myHandler = handler;

    }

    public void setSpf(SharedFileUtils spf) {
        this.spf = spf;
    }

    public Socket getSocket() {
        return socket;
    }

    public GetDateSocket (  ){
        super();

    }

    public void setmWifiUtils(WifiUtils mWifiUtils) {
        this.mWifiUtils = mWifiUtils;
    }

    public void setIsKeepAlive(boolean isKeepAlive) {
        this.isKeepAlive = isKeepAlive;
    }

    @Override
    public void run(){

        System.out.println("客户端启动...");
        System.out.println("当接收到服务器端字符为 \"OK\" 的时候, 客户端将终止\n");
        int i = 0;

        try {
            if(mWifiUtils.isWifiConnect())
                socket = new Socket(IP_ADDR, PORT);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        String ret="";
        while (isKeepAlive){
           // if()
            if(!mWifiUtils.isWifiConnect()){
                isKeepAlive = false;
                return;
            }
           String str = "get\n";
           if(!isKeepAlive){
               String sp =  spf.getString(WifiApConst.AP_SSID);
           }
            Bundle bundle = new Bundle();
            Message msg = Message.obtain();
            try {
                msg.what = 0x11;
                bundle.clear();
                if(socket!=null) {
                    SocketUtil.writeStr2Stream(str,
                            socket.getOutputStream());
                    ret = SocketUtil.readStrFromStream(socket.getInputStream());
                }else{
                    ret="服务器没启动!或者没有网络";
                    isKeepAlive=false;
                }
                System.out.println("服务器端返回过来的是: " + ret);
                if(ret != null && ret.equals("stop")){
                    ret="下位机已停止!";

                }
                if(ret != null && ret.equals("notstart")){
                    ret = "等待下位机启动!";
                    Thread.sleep(2500);
                }
                bundle.putString("msg", ret+"");
                msg.setData(bundle);
                //发送消息 修改UI线程中的组件
                myHandler.sendMessage(msg);

                // 如接收到 "OK" 则断开连接
                if (ret != null && "OK".equals(ret)) {
                    System.out.println("客户端将关闭连接");
                    Thread.sleep(500);
                    break;
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                try {

                    socket.close();
                    isKeepAlive = false;
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                System.out.println("close......");
            }finally {
                if(ret==null || ret.equals("") || !isKeepAlive){
                    try {
                        if(socket!=null)
                            socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isKeepAlive = false;
                }
            }
        }
    }

    /**
     * 关闭所有
     */
    public void allClose(){
        if(socket!=null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
