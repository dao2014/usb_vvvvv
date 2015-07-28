package com.esp_v6.mysocket.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2015/7/9.
 */
public class WifiAdmin {
    private static  WifiAdmin wifiAdmin = null;

    private List<WifiConfiguration> mWifiConfiguration;
    private List<ScanResult> mWifiList;


    private WifiInfo mWifiInfo;

    WifiManager.WifiLock mWifilock;
    public WifiManager mWifiManager;


    public static WifiAdmin getInstance(Context context) {
        if(wifiAdmin == null) {
            wifiAdmin = new WifiAdmin(context);
            return wifiAdmin;
        }
        return null;
    }
    // 锁定WifiLock
    public void AcquireWifiLock() {
        mWifilock.acquire();
    }
    // 解锁WifiLock
    public void ReleaseWifiLock() {
// 判断时候锁定
        if (mWifilock.isHeld()){
            mWifilock.acquire();
        }
    }

    // 创建一个WifiLock
    public void CreatWifiLock() {
        mWifilock =mWifiManager.createWifiLock("Test");
    }
    public WifiAdmin(Context context) {

        this.mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
    }

    private WifiConfiguration isExsits(String str) {
        Iterator localIterator = this.mWifiManager.getConfiguredNetworks().iterator();
        WifiConfiguration localWifiConfiguration;
        do {
            if(!localIterator.hasNext()) return null;
            localWifiConfiguration = (WifiConfiguration) localIterator.next();
        }while(!localWifiConfiguration.SSID.equals("\"" + str + "\""));
        return localWifiConfiguration;
    }



    public void CreateWifiLock() {
        this.mWifilock = this.mWifiManager.createWifiLock("Test");
    }

    public void ReleaseWifilock() {
        if(mWifilock.isHeld()) { //?ж??????
            mWifilock.acquire();
        }
    }



    public void OpenWifi() {
        if(!this.mWifiManager.isWifiEnabled()){ //???wifi??????
            this.mWifiManager.setWifiEnabled(true);
        }
    }

    public void closeWifi() {
        if(mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    public void disconnectWifi(int paramInt) {
        this.mWifiManager.disableNetwork(paramInt);
    }


    public void addNetwork(WifiConfiguration paramWifiConfiguration) {
        int i = mWifiManager.addNetwork(paramWifiConfiguration);
        mWifiManager.enableNetwork(i, true);
    }

    public void connectConfiguration(int index) {
        // ??????????ú???????????
        if (index > mWifiConfiguration.size()) {
            return;
        }
        //???????ú?????ID??????
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId, true);
    }

    public void createWifiAP(WifiConfiguration paramWifiConfiguration,boolean paramBoolean) {
        try {
            Class localClass = this.mWifiManager.getClass();
            Class[] arrayOfClass = new Class[2];
            arrayOfClass[0] = WifiConfiguration.class;
            arrayOfClass[1] = Boolean.TYPE;
            Method localMethod = localClass.getMethod("setWifiApEnabled",arrayOfClass);
            WifiManager localWifiManager = this.mWifiManager;
            Object[] arrayOfObject = new Object[2];
            arrayOfObject[0] = paramWifiConfiguration;
            arrayOfObject[1] = Boolean.valueOf(paramBoolean);
            localMethod.invoke(localWifiManager, arrayOfObject);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WifiConfiguration createWifiInfo(String ssid, String passawrd,int paramInt, String type) {
        //?????????????
        WifiConfiguration localWifiConfiguration1 = new WifiConfiguration();
        //????????????????
        localWifiConfiguration1.allowedAuthAlgorithms.clear();
        localWifiConfiguration1.allowedGroupCiphers.clear();
        localWifiConfiguration1.allowedKeyManagement.clear();
        localWifiConfiguration1.allowedPairwiseCiphers.clear();
        localWifiConfiguration1.allowedProtocols.clear();

        if(type.equals("wt")) { //wifi????
            localWifiConfiguration1.SSID = ("\"" + ssid + "\"");
            WifiConfiguration localWifiConfiguration2 = isExsits(ssid);
            if(localWifiConfiguration2 != null) {
                mWifiManager.removeNetwork(localWifiConfiguration2.networkId); //???б???????????????????????
            }
            if(paramInt == 1) { //???????
                localWifiConfiguration1.wepKeys[0] = "";
                localWifiConfiguration1.allowedKeyManagement.set(0);
                localWifiConfiguration1.wepTxKeyIndex = 0;
            } else if(paramInt == 2) { //??????
                localWifiConfiguration1.hiddenSSID = true;
                localWifiConfiguration1.wepKeys[0] = ("\"" + passawrd + "\"");
            } else { //wap????
                localWifiConfiguration1.preSharedKey = ("\"" + passawrd + "\"");
                localWifiConfiguration1.hiddenSSID = true;
                localWifiConfiguration1.allowedAuthAlgorithms.set(0);
                localWifiConfiguration1.allowedGroupCiphers.set(2);
                localWifiConfiguration1.allowedKeyManagement.set(1);
                localWifiConfiguration1.allowedPairwiseCiphers.set(1);
                localWifiConfiguration1.allowedGroupCiphers.set(3);
                localWifiConfiguration1.allowedPairwiseCiphers.set(2);
            }
        }else {//"ap" wifi???
            localWifiConfiguration1.SSID = ssid;
            localWifiConfiguration1.allowedAuthAlgorithms.set(1);
            localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            localWifiConfiguration1.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            localWifiConfiguration1.allowedKeyManagement.set(0);
            localWifiConfiguration1.wepTxKeyIndex = 0;
            if (paramInt == 1) {  //???????
                localWifiConfiguration1.wepKeys[0] = "";
                localWifiConfiguration1.allowedKeyManagement.set(0);
                localWifiConfiguration1.wepTxKeyIndex = 0;
            } else if (paramInt == 2) { //??????
                localWifiConfiguration1.hiddenSSID = true;//?????????ssid
                localWifiConfiguration1.wepKeys[0] = passawrd;
            } else if (paramInt == 3) {//wap????
                localWifiConfiguration1.preSharedKey = passawrd;
                localWifiConfiguration1.allowedAuthAlgorithms.set(0);
                localWifiConfiguration1.allowedProtocols.set(1);
                localWifiConfiguration1.allowedProtocols.set(0);
                localWifiConfiguration1.allowedKeyManagement.set(1);
                localWifiConfiguration1.allowedPairwiseCiphers.set(2);
                localWifiConfiguration1.allowedPairwiseCiphers.set(1);
            }
        }
        return localWifiConfiguration1;
    }


    public String getApSSID() {
        try {
            Method localMethod = this.mWifiManager.getClass().getDeclaredMethod("getWifiApConfiguration", new Class[0]);
            if (localMethod == null) return null;
            Object localObject1 = localMethod.invoke(this.mWifiManager,new Object[0]);
            if (localObject1 == null) return null;
            WifiConfiguration localWifiConfiguration = (WifiConfiguration) localObject1;
            if (localWifiConfiguration.SSID != null) return localWifiConfiguration.SSID;
            Field localField1 = WifiConfiguration.class .getDeclaredField("mWifiApProfile");
            if (localField1 == null) return null;
            localField1.setAccessible(true);
            Object localObject2 = localField1.get(localWifiConfiguration);
            localField1.setAccessible(false);
            if (localObject2 == null)  return null;
            Field localField2 = localObject2.getClass().getDeclaredField("SSID");
            localField2.setAccessible(true);
            Object localObject3 = localField2.get(localObject2);
            if (localObject3 == null) return null;
            localField2.setAccessible(false);
            String str = (String) localObject3;
            return str;
        } catch (Exception localException) {
        }
        return null;
    }


    public String getBSSID() {
        if (this.mWifiInfo == null)
            return "NULL";
        return this.mWifiInfo.getBSSID();
    }


    public String getSSID(){
        if (this.mWifiInfo == null)
            return "NULL";
        else
            this.mWifiInfo = this.mWifiManager.getConnectionInfo();
        return this.mWifiInfo.getSSID();
    }


    public List<WifiConfiguration> getConfiguration() {
        return this.mWifiConfiguration;
    }


    public int getIPAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }


    public int getNetworkId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    public int getWifiApState() {
        try {
            int i = ((Integer) this.mWifiManager.getClass()
                    .getMethod("getWifiApState", new Class[0])
                    .invoke(this.mWifiManager, new Object[0])).intValue();
            return i;
        } catch (Exception localException) {
        }
        return 4;   //δ?wifi????
    }

    public WifiInfo getWifiInfo() {
        return this.mWifiManager.getConnectionInfo();
    }

    public List<ScanResult> getWifiList() {
        return this.mWifiList;
    }


    // 检查当前wifi状态
    public int checkState() {
        return mWifiManager.getWifiState();
    }

    //根据已经保存的SSID 进行保存
    public boolean connetGuration(String SSID) {
        return ConnectASaveWifi(IsConfiguration(SSID));
    }

    //获取一个已经保存的wifi信息
    public int IsConfiguration(String SSID){
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();//??????ú?????????
        Log.i("IsConfiguration", String.valueOf(mWifiConfiguration.size()));
        for(int i = 0; i < mWifiConfiguration.size(); i++){
            Log.i(mWifiConfiguration.get(i).SSID,String.valueOf( mWifiConfiguration.get(i).networkId));
            if(mWifiConfiguration.get(i).SSID.equals("\""+SSID+"\"")){//??????
                return mWifiConfiguration.get(i).networkId;
            }
        }
        return -1;
    }

    /**
     * 这个方法有问题
     * 获取连接状态 //status:0--已经连接，1--不可连接，2--可以连接
     * @param wifiId
     * @return
     */
    public int getconnetStatus(int wifiId){
        int status=-0;
          mWifiManager.isWifiEnabled();
        return status;
    }

    //链接并且保持wifi
    public boolean ConnectASaveWifi(int wifiId){
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();//??????ú?????????

        for(int i = 0; i < mWifiConfiguration.size(); i++){
            WifiConfiguration wifi = mWifiConfiguration.get(i);
            if(wifi.networkId == wifiId){
                while(!(mWifiManager.enableNetwork(wifiId, true))){
                    mWifiManager.updateNetwork(wifi);
                    Log.i("ConnectWifi",String.valueOf(mWifiConfiguration.get(wifiId).status));
                }

                return true;
            }
        }
        return false;
    }

    //实时获取 wifiSSID
    public boolean findSSID(String ssid){
        mWifiManager.startScan();
        List<ScanResult> wifiList =  mWifiManager.getScanResults();//? ?
        for(int i = 0;i < wifiList.size(); i++){
            ScanResult wifi = wifiList.get(i);
            if(wifi.SSID.equals(ssid)){
                return true;
            }
        }
        return false;
    }

    //保持一个wifi信息
    public int AddWifiConfig(String ssid,String pwd){
        int wifiId = -1;
        mWifiManager.startScan();
        List<ScanResult> wifiList =  mWifiManager.getScanResults();//????????
        //mWifiManager.re
        for(int i = 0;i < wifiList.size(); i++){
            ScanResult wifi = wifiList.get(i);
            if(wifi.SSID.equals(ssid)){
                Log.i("AddWifiConfig","equals");
                WifiConfiguration wifiCong = new WifiConfiguration();
                wifiCong.SSID = "\""+wifi.SSID+"\"";//\"????????"
                wifiCong.preSharedKey = "\""+pwd+"\"";//WPA-PSK????
                wifiCong.hiddenSSID = false;
                wifiCong.status = WifiConfiguration.Status.ENABLED;
                wifiId = mWifiManager.addNetwork(wifiCong);//?????ú?????WIFI??????????,????????????????????????????ID???????-1
                if(wifiId != -1){
                    return wifiId;
                }
            }
        }
        return wifiId;
    }

    /**
     * 获取IP地址
     * @return
     */
    public  int getWifiInfos(){

        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
        if(mWifiInfo == null )
            return 0;
        int ids  = mWifiInfo.getIpAddress();

       return ids;
    }

    /**
     * 删除一个 路由。测试路由不需要保持，直接删除即可
     * @param netId
     */
    public void deleWifi(int netId){
        mWifiManager.removeNetwork(netId);
    }

    //得到连接的ID
    public int getNetWordId(){
        return (mWifiInfo==null)?0:mWifiInfo.getNetworkId();
    }
    //断开指定ID的网络
    public void disConnectionWifi(int netId){
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    public StringBuilder lookUpScan() {
        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; i < mWifiList.size(); i++)
        {
            localStringBuilder.append("Index_"+new Integer(i + 1).toString() + ":");
            localStringBuilder.append((mWifiList.get(i)).toString());
            localStringBuilder.append("\n");
        }
        return localStringBuilder;
    }

    public void setWifiList() {
        this.mWifiList = this.mWifiManager.getScanResults();
    }
    public void startScan() {
        this.mWifiManager.startScan();
    }
    public String GetBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }


    private WifiConfiguration IsExsits(String SSID)
    {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs)
        {
            if (existingConfig.SSID.equals("\""+SSID+"\""))
            {
                return existingConfig;
            }
        }
        return null;
    }
}
