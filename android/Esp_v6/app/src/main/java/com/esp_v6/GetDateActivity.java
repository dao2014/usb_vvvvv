package com.esp_v6;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.esp_v6.db.dbHelper;
import com.esp_v6.mysocket.GetDateSocket;
import com.esp_v6.mysocket.util.MathExtend;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Administrator on 2015/6/24.
 */
public class GetDateActivity extends Activity {

    public TextView mProgressView;
    public Button start;
    public Socket socket;
    GetDateSocket ms;
    SQLiteDatabase sqldbs;
    private dbHelper db;
    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x11) {
                Bundle bundle = msg.getData();
                String newMsa[] = bundle.getString("msg").split(",");
                if(newMsa.length >= 4) {
                    String rtsp = newMsa[3].substring(0, newMsa[3].length() - 2);
                    double rt1 = Double.parseDouble(rtsp);
                    double vrt1 = 3300*rt1;
                    vrt1 = MathExtend.divide(vrt1,1024,5);
                    vrt1 = MathExtend.divide(vrt1,1000,5);//得到电压值
                    //vrt1 = vrt1/;
                    rt1 = MathExtend.divide(vrt1,4700,5);  //得到电流值
                    double vtr2 = 3.3-vrt1;  //等到热敏电阻 电压值
                    rt1 = MathExtend.divide(vtr2,rt1,5);  //得到得到欧姆
                    rt1 = MathExtend.divide(rt1, 1000, 5);  //转换单位
                   // rt1 = ()/vrt1/1000;
                    double rt2 = rt1;
                    rt1 = db.selectMax(rt1+"",sqldbs);
                    newMsa[3] = rt1+"RT";
                    mProgressView.setText("设备编号:" + newMsa[0] + "\n电压值:" + newMsa[1] + "\n电流值:" + newMsa[2]
                            + "\n温度值:" + newMsa[3]+"电阻值:"+rt2 + "\n更新时间:" + newMsa[4]);
                   // if(newMsa[3]!=null )

                }else{
                    mProgressView.setText(bundle.getString("msg"));
                }
            }
        }

    };

    @Override
    protected void onPause() {
        super.onPause();
        if(ms!=null){
            ms.setIsKeepAlive(false);
            ms.allClose();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mProgressView.setText("请重新点击获取!");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdate);
        mProgressView = (TextView) findViewById(R.id.vies);
        start = (Button) findViewById(R.id.start);
        db=new dbHelper(this);
        sqldbs=db.getReadableDatabase();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ms  = new GetDateSocket();
                ms.setIsKeepAlive(true);
                ms.setMyHandler(myHandler);
                ms.start();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Socket socket=null;
        if(ms!=null){
            ms.setIsKeepAlive(false);
            socket= ms.getSocket();
        }
        if( socket != null )
        {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.finish();
        return super.onKeyDown(keyCode, event);
    }



}
