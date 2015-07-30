/*
 * Copyright (C) 2012 yueyueniao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.esp_v6.framents;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.esp_v6.BaseActivity;
import com.esp_v6.BaseDialog;
import com.esp_v6.R;
import com.esp_v6.empty.ESPInfo;
import com.esp_v6.thread.ConnonWifi.WifiConst;
import com.esp_v6.thread.ConnonWifi.wifiAp.WifiApConst;
import com.esp_v6.util.SharedFileUtils;
import com.esp_v6.util.WifiUtils;
import com.esp_v6.view.SettingSwitchButton;


public class RightFragment extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener,
		DialogInterface.OnClickListener  {

	SettingSwitchButton mSettingSwitchButton;
	public View view;
	private CreateAPProcess mCreateApProcess;
	private ConnionWifi mConnionWifi;
	private BaseDialog mDialog; // 提示窗口
	private int wifiapOperateEnum = WifiApConst.NOTHING;
	ChargerMainFrament mChargerMainFrament;
	TelephonyManager mTelephonyManager;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.right_layout, null);
		mWifiUtils = WifiUtils.getInstance(getActivity());
		sharedFileUtils =new SharedFileUtils(getActivity());
		initViews();
		initEvents();
		mTelephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		return view;
	}



	@Override
	protected void initViews() {
		mSettingSwitchButton = (SettingSwitchButton) view.findViewById(R.id.checkbox_sound);
		FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
		mChargerMainFrament = (ChargerMainFrament) fca.getSupportFragmentManager().findFragmentById(R.id.content_frame);

	}

	@Override
	protected void initEvents() {
		mDialog = BaseDialog.getDialog(getActivity(),
				R.string.dialog_tips, "", "确 定", this, "取 消", this);
		mDialog.setButton1Background(R.drawable.btn_default_popsubmit);
		mSettingSwitchButton.setOnCheckedChangeListener(this);
	}

	/** 短暂显示Toast提示(来自res) **/
	protected void showShortToast(int resId) {
		Toast.makeText(getActivity(), getString(resId), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
		switch (compoundButton.getId()) {
			case R.id.checkbox_sound:
				compoundButton.setEnabled(false);
				if(b) {

					// 如果不支持热点创建
					if (mWifiUtils.getWifiApStateInt() == 4) {
						showShortToast(R.string.wifiap_dialog_createap_nonsupport);
						//compoundButton.setChecked(false);
						return;
					}
					// 如果wifi正打开着的，就提醒用户
					if (mWifiUtils.mWifiManager.isWifiEnabled()) {
						wifiapOperateEnum = WifiApConst.CREATE;
						mDialog.setMessage(getString(R.string.wifiap_dialog_createap_closewifi_confirm));
						mDialog.show();
						return;
					}
					mWifiUtils.closeWifi();
					wifiapOperateEnum = WifiApConst.CREATE;
					mDialog.setMessage(getString(R.string.wifiap_dialog_createap_closewifi_confirm));
					mDialog.show();
				}else {
					mWifiUtils.createWiFiAP(mWifiUtils.createWifiInfo(
							mWifiUtils.getApSSID(), WifiApConst.WIFI_AP_PASSWORD,
							3, "ap"), false);
					mWifiUtils.OpenWifi();
					if (mConnionWifi == null) {
						mConnionWifi = new ConnionWifi();
					}
					mConnionWifi.start();
//					mCreateApProcess.start();
					mWifiUtils.startScan();
					wifiapOperateEnum = WifiApConst.CLOSE;

				}
				break;


			default:
				break;
		}
	}

	/** handler 异步更新UI **/
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				// 搜索超时
				case WifiApConst.ApSearchTimeOut:

					break;
				// 搜索结果
				case WifiApConst.ApScanResult:

					break;
				// 连接成功
				case WifiApConst.ApConnectResult:
					mWifiUtils.setNewWifiManagerInfo(); // 更新wifiInfo

					break;

				// 热点创建结果
				case WifiApConst.ApCreateAPResult:

					if (((mWifiUtils.getWifiApStateInt() == 3) || (mWifiUtils.getWifiApStateInt() == 13))
							&& (mWifiUtils.getApSSID().startsWith(WifiApConst.WIFI_AP_HEADER))) {
						mCreateApProcess.stop();
						mSettingSwitchButton.setEnabled(true);
						sharedFileUtils.putString(ESPInfo.ONLINK, ESPInfo.ONLINK_AP);//AP模式
					} else {

					}
					break;
				case WifiConst.WifiConnected:
					mSettingSwitchButton.setEnabled(true);
					mConnionWifi.stop();
					sharedFileUtils.putString(ESPInfo.ONLINK, ESPInfo.ONLINK_STA);//STA模式wifi
					break;
				default:
					break;
			}
		}
	};

	public void startAp(){
		mWifiUtils.closeWifi();
		String apName =WifiApConst.WIFI_AP_HEADER + getLocalHostName();
		String apPass =WifiApConst.WIFI_AP_PASSWORD;
		String apssid = sharedFileUtils.getString(WifiApConst.AP_SSID);
		String appass = sharedFileUtils.getString(WifiApConst.AP_PASS);
		if( apssid ==null || apssid.equals("") || appass ==null || appass.equals("") ){
			sharedFileUtils.putString(ESPInfo.AP_SSID,apName);//添加到缓存
			sharedFileUtils.putString(ESPInfo.AP_PASS,apPass);//添加到缓存
		}else{
			apName = apssid;
			apPass = appass;
		}
		mWifiUtils.createWiFiAP(mWifiUtils.createWifiInfo(apName,
				apPass, 3, "ap"), true);
		if (mCreateApProcess == null) {
			mCreateApProcess = new CreateAPProcess();
		}
		mCreateApProcess.start();
	}

	@Override
	public void onClick(DialogInterface dialogInterface, int i) {
		switch (i) {
			// 确定
			case 0:
				dialogInterface.dismiss();
				switch (wifiapOperateEnum) {
					// 执行创建热点事件
					case WifiApConst.CREATE:
						startAp();
						break;
				}
				break;
			// 取消
			case 1:
				mSettingSwitchButton.setEnabled(true);
				mSettingSwitchButton.setChecked(false);
				dialogInterface.cancel();
		}
	}

	/**
	 * 创建热点线程类
	 *
	 * <p>
	 * 线程启动后，热点创建的结果将通过handler更新
	 * </p>
	 */
	class CreateAPProcess implements Runnable {
		public boolean running = false;
		private long startTime = 0L;
		private Thread thread = null;

		CreateAPProcess() {
		}

		public void run() {
			while (true) {
				if (!this.running)
					return;
				if ((mWifiUtils.getWifiApStateInt() == 3)
						|| (mWifiUtils.getWifiApStateInt() == 13)
						|| (System.currentTimeMillis() - this.startTime >= 30000L)) {
					Message msg = handler.obtainMessage(WifiApConst.ApCreateAPResult);
					handler.sendMessage(msg);
				}
				try {
					Thread.sleep(5L);
				} catch (Exception localException) {
				}
			}
		}

		public void start() {
			try {
				thread = new Thread(this);
				running = true;
				startTime = System.currentTimeMillis();
				thread.start();
			} finally {
			}
		}

		public void stop() {
			try {
				this.running = false;
				this.thread = null;
				this.startTime = 0L;
			} finally {
			}
		}
	}

	/**
	 * 链接wifi
	 *
	 * <p>
	 * 线程启动后，链接wifi 如果连接成功通知 handler
	 * </p>
	 */
	class ConnionWifi implements Runnable {
		public boolean running = false;
		private long startTime = 0L;
		private Thread thread = null;

		ConnionWifi() {
		}

		public void run() {
			while (true) {
				if (!this.running)
					return;
				if ((mWifiUtils.isWifiConnect())
						|| (System.currentTimeMillis() - this.startTime >= 30000L)) {
					Message msg = handler.obtainMessage(WifiConst.WifiConnected);
					handler.sendMessage(msg);
				}
				try {
					Thread.sleep(5L);
				} catch (Exception localException) {
				}
			}
		}

		public void start() {
			try {
				thread = new Thread(this);
				running = true;
				startTime = System.currentTimeMillis();
				thread.start();
			} finally {
			}
		}

		public void stop() {
			try {
				this.running = false;
				this.thread = null;
				this.startTime = 0L;
			} finally {
			}
		}
	}

	@Override
	public void onClick(View view) {

	}

	/**
	 * 设置wifi 热点称 ESP_ + 手机品牌+IMEI4  尾数 形成 热点名称
	 * @return
	 */
	public String getLocalHostName() {
		String str1 = Build.BRAND;
		String IMEI = mTelephonyManager.getDeviceId();
		String str2 =IMEI.substring(IMEI.length()-4,IMEI.length()); // 获取IME
		return str1 + "_" + str2;
	}
}
