package com.esp_v6;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.esp_v6.db.dbHelper;
import com.esp_v6.mysocket.connenAP;
import com.esp_v6.mysocket.util.WifiAdmin;
import com.esp_v6.thread.ConnonWifi.CommonWifi;
import com.esp_v6.thread.ConnonWifi.wifiAp.WifiApConst;
import com.esp_v6.util.SharedFileUtils;
import com.esp_v6.util.TextUtils;
import com.esp_v6.util.WifiUtils;


public class MainActivity extends Activity  {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    // UI references.
    private EditText mssid;
    private EditText mPasswordView;
    private View mLoginFormView;
    private TextView logs;
    private ProgressDialog progressDialog = null;
    public MainActivity mainActivity;
    private WifiAdmin wifiAdmin;
    private String SSID="";
    boolean checkSSID=true;
    private dbHelper db;
    SharedFileUtils sfu;
    SQLiteDatabase sqldbs;
    CommonWifi cw;
    Thread thread;
    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            logs.setVisibility(View.VISIBLE);
            switch(msg.what)
            {
                case 1:
                  //  logs.setVisibility(View.VISIBLE);
                    logs.setText("请打开设备,等待wifi信号,再点击启动!");
                    logs.setTextColor(Color.RED);
                    break;
                case 2:
                    //  logs.setVisibility(View.VISIBLE);
                    logs.setText("正在等待链接设备....");
                    logs.setTextColor(Color.RED);
                    break;
                case 3:
                    logs.setText("已经链接上设备");
                    logs.setTextColor(Color.BLUE);
                    break;
                case 4:
                    logs.setText("输入SSID有误");
                    logs.setTextColor(Color.RED);
                    break;
                case 5:
                    logs.setText("输入路由密码有误");
                    logs.setTextColor(Color.RED);
                    break;

                default:
                    break;
            }
            if(progressDialog!=null){
                progressDialog.dismiss();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sfu = new SharedFileUtils(this);
        mainActivity = this;
        // Set up the login form.
        mssid = (EditText) findViewById(R.id.email);
        // populateAutoComplete();
        mPasswordView = (EditText) findViewById(R.id.password);
        logs = (TextView) findViewById(R.id.logs);
        wifiAdmin = WifiAdmin.getInstance(this);
        db=new dbHelper(this);
        sqldbs=db.getReadableDatabase();
        SSID = wifiAdmin.getSSID();

//        cw = new CommonWifi(wifiAdmin,mHandler);
//        cw.setCheckSSID(true);
//        cw.start();
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    progressDialog = ProgressDialog.show(MainActivity.this, "请稍等...", "设置中...", true);

//                    String values = "ssid"+mssid.getText().length()+""+mssid.getText()+"pass"+mPasswordView.getText().length()+""+mPasswordView.getText();

                    String values = TextUtils.sendSSIDPASS(WifiUtils.getApSsid(mainActivity), WifiApConst.WIFI_AP_PASSWORD,mssid.getText()+"",mPasswordView.getText()+"",sfu);

                connenAP ls = new connenAP();
                    ls.setNewssid(mssid.getText()+"");
                    ls.setSsidPass(mPasswordView.getText() + "");
                    ls.setValues(values);
                    ls.setProgressDialog(progressDialog);
                    ls.setMainActivity(mainActivity);
                    ls.setWifiAdmin(wifiAdmin);
                    ls.setSSID(SSID);
                    ls.setmHandler(mHandler);
                    ls.start();

                //finish();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        //mProgressView = findViewById(R.id.login_progress);
        Intent internt = new Intent();
//                            internt.setClass(mainActivity,
//                                    GetDateActivity.class);
//        internt.setClass(mainActivity,
//                FragmentChangeActivity.class);
//        mainActivity.startActivity(internt);
    }

    @Override
    protected void onPause() {
      //  cw.setCheckSSID(false);
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logs.setText("........");
    }
}
