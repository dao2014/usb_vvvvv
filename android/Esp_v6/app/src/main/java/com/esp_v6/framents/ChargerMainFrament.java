package com.esp_v6.framents;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esp_v6.R;
import com.esp_v6.db.dbHelper;
import com.esp_v6.mysocket.GetDateSocket;
import com.esp_v6.mysocket.TcpServer;
import com.esp_v6.mysocket.util.MathExtend;
import com.esp_v6.util.AbstractChartUtil;
import com.esp_v6.util.SharedFileUtils;
import com.esp_v6.util.WifiUtils;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.tools.PanListener;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2015/7/23.
 */
public class ChargerMainFrament extends com.esp_v6.BaseActivity {
    /** 主要的数据集的所有系列,包括进入一个图表 */
    private XYMultipleSeriesDataset mDataset;
    /** 包括所有的主要渲染器渲染器定制图表。 */
    private XYMultipleSeriesRenderer mRenderer;
    /** 最近添加的系列。 */
    private XYSeries[] mCurrentSeries;
    /** 最近创建的渲染器,自定义当前系列。 */
    private XYSeriesRenderer mCurrentRenderer;
    /** 图表视图显示数据 */
    private GraphicalView mChartView;
    /**工具*/
    private AbstractChartUtil abstractChartUtil;
    public View view;
    SQLiteDatabase sqldbs;
    private dbHelper db;
  //  private Cont
    private int mColorRes = -1;
    XYSeries[] xYSeries;
    SimpleSeriesRenderer[] simpleSeriesRenderer;
    public TextView text_V;
    public TextView text_I;
    public TextView text_W;
    public TextView view_capacity;
    public TextView view_time;
    public TextView temperature;
    public int second; //秒钟
    public float zoomsize=0L;
    private  TcpServer tcpServer;

    public  GetDateSocket ms; //获取 线程


    public ChargerMainFrament() {
        this(R.color.white);
    }

    public ChargerMainFrament(int colorRes) {
        mColorRes = colorRes;
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        abstractChartUtil = new AbstractChartUtil();
        mWifiUtils = WifiUtils.getInstance(getActivity());
        sharedFileUtils =new SharedFileUtils(getActivity());
         return inflater.inflate(R.layout.xy_chart,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        db=new dbHelper(getActivity());
        sqldbs=db.getReadableDatabase();
        initView();
        //initVlues();
        // set some properties on the main renderer
        mChartView();
        checkbosmsg();
    }



    /**
     * 初始化控件
     */
    public void initView(){
        text_V = (TextView) getActivity().findViewById(R.id.text_V);
        text_I = (TextView) getActivity().findViewById(R.id.text_I);
        text_W = (TextView) getActivity().findViewById(R.id.text_W);
        view_capacity = (TextView) getActivity().findViewById(R.id.view_capacity);
        view_time = (TextView) getActivity().findViewById(R.id.view_time);
        temperature = (TextView) getActivity().findViewById(R.id.temperature);

    }

    /**
     * 初始化 显示值
     */
    public void initVlues(){
        text_V.setText("Voltage:0V");
        text_I.setText("Electricity:0A");
        text_W.setText("Power:0W");
        view_capacity.setText("Has been filling capacity:0A");
        view_time.setText("Have sufficient time:0M");
        temperature.setText("Temperature:0°");
        abstractChartUtil.updateXYDate(xYSeries[0], second, 0, mChartView, false);
        abstractChartUtil.updateXYDate(xYSeries[1], second, 0, mChartView, false);
        abstractChartUtil.updateXYDate(xYSeries[2], second, 0, mChartView, true);
    }


    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x11) {
                Bundle bundle = msg.getData();
                String newMsa[] = bundle.getString("msg").split(",");
                if(newMsa.length >= 4) {
                    newMsa[1] = MathExtend.divide(Double.parseDouble(newMsa[1]), 1000, 2)+"";
                    if (!newMsa[3].equals("0")) {
                        String rtsp = newMsa[3].substring(0, newMsa[3].length());
                        double rt1 = Double.parseDouble(rtsp);
                        double vrt1 = 3300 * rt1;
                        vrt1 = MathExtend.divide(vrt1, 1024, 5);
                        vrt1 = MathExtend.divide(vrt1, 1000, 5);//得到电压值
                        //vrt1 = vrt1/;
                        rt1 = MathExtend.divide(vrt1, 4700, 5);  //得到电流值
                        double vtr2 = 3.3 - vrt1;  //等到热敏电阻 电压值
                        rt1 = MathExtend.divide(vtr2, rt1, 5);  //得到得到欧姆
                        rt1 = MathExtend.divide(rt1, 1000, 5);  //转换单位
                        // rt1 = ()/vrt1/1000;
                        double rt2 = rt1;
                        rt1 = db.selectMax(rt1 + "", sqldbs);
                        newMsa[3] = rt1 + "RT";
                    }
                    double testw=0;
                    if (!newMsa[2].equals("0")){
                        testw = MathExtend.divide(Double.parseDouble(newMsa[2]), 1000, 2);  //电流转换 A
                        newMsa[2] = testw + "";
                        testw = testw * Double.parseDouble(newMsa[1]);
                        testw = MathExtend.round(testw, 2);
                    }
                    text_V.setText("Voltage:"+newMsa[1]+"V");
                    text_I.setText("Electricity:"+newMsa[2]+"A");
                    text_W.setText("Power:"+testw+"W");
                    temperature.setText("Temperature:"+newMsa[3]+"°");
//                    mProgressView.setText("设备编号:" + newMsa[0] + "\n电压值:" + newMsa[1] + "\n电流值:" + newMsa[2]
//                            + "\n温度值:" + newMsa[3]+"电阻值:"+rt2 + "\n更新时间:" + newMsa[4]);
                    // if(newMsa[3]!=null )
                    second++;
                    abstractChartUtil.updateXYDate(xYSeries[0],second,Double.parseDouble(newMsa[1]),mChartView,false );
                    abstractChartUtil.updateXYDate(xYSeries[1],second,Double.parseDouble(newMsa[2]),mChartView,false );
                    abstractChartUtil.updateXYDate(xYSeries[2],second,testw,mChartView,true );
                }else{
                    initVlues();
                }
            }
        }

    };

    /**
     * chebox event
     */
    public void checkbosmsg(){
        //voltage
        CheckBox setshow_V = (CheckBox) this.view.findViewById(R.id.shadow_V);
        setshow_V.setChecked(true);
        setshow_V.setTextColor(Color.BLUE);
        setshow_V.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                abstractChartUtil.removeXYServes(xYSeries[0],isChecked,simpleSeriesRenderer[0]);
                abstractChartUtil.refresh(mChartView);
            }
        });
        //electricity
        CheckBox setshow_I = (CheckBox) this.view.findViewById(R.id.shadow_I);
        setshow_I.setChecked(true);
        setshow_I.setTextColor(Color.GREEN);
        setshow_I.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                abstractChartUtil.removeXYServes(xYSeries[1],isChecked,simpleSeriesRenderer[1]);
                abstractChartUtil.refresh(mChartView);
                //getSlidingMenu().setFadeEnabled(isChecked);
            }
        });
        //power
        CheckBox setshow_W = (CheckBox) this.view.findViewById(R.id.shadow_W);
        setshow_W.setTextColor( Color.CYAN);
        setshow_W.setChecked(true);
        setshow_W.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                abstractChartUtil.removeXYServes(xYSeries[2], isChecked,simpleSeriesRenderer[2]);
                abstractChartUtil.refresh(mChartView);
            }
        });
    }
    /**
     * 渲染图形初始化
     */
    public void mChartView(){
        //图标
        String[] titles = new String[] { "Crete", "Corfu", "Thassos" };
        List<double[]> x = new ArrayList<double[]>();
        for (int i = 0; i < titles.length; i++) {
            x.add(new double[] { 0,0 });
        }
        List<double[]> values = new ArrayList<double[]>(); //初始化xy值
        values.add(new double[] { 0,0 });
        values.add(new double[] { 0,0 });
        values.add(new double[] {0,0 });
        int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.CYAN };//设置 线的颜色
        PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND, PointStyle.TRIANGLE }; //设置点的图形
        mRenderer = abstractChartUtil.buildRenderer(colors, styles);

        int length = mRenderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            ((XYSeriesRenderer) mRenderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        abstractChartUtil.setChartSettings(mRenderer, "Instrumetation", "Second", "Numerical analysis", 0.5, 12.5, 0, 6, Color.LTGRAY, Color.LTGRAY);
      //  mRenderer = new GraphicalView()
        mRenderer.setXLabels(12);
        mRenderer.setYLabels(24);
        mRenderer.setShowGrid(true);
        mRenderer.setXLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setShowLegend(false);
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setPanLimits(new double[]{-10, 900, -10, 400});  //设置滑动极限
        mRenderer.setZoomLimits(new double[]{-10, 900, -10, 400}); ////设置放大缩小时X轴Y轴允许的最大
        mRenderer.setMargins(new int[] { 30, 40, 10, 10 });//设置图表的外边框(上/左/下/右)
        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.chart);
        mDataset =  abstractChartUtil.buildDataset(titles, x, values);
        mChartView = ChartFactory.getCubeLineChartView(getActivity().getApplicationContext(),mDataset,mRenderer, 0.2f);
        //设置背景
        mChartView.setBackgroundColor(Color.BLACK);
        mRenderer.setClickEnabled(true);//设置图表是否允许点击
        mRenderer.setSelectableBuffer(100);//设置点的缓冲半径值(在某点附件点击时,多大范围内都算点击这个点)
        mChartView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // handle the click event on the chart

                SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
                if (seriesSelection == null) {
                    Toast.makeText(getActivity(), "No chart element", Toast.LENGTH_SHORT).show();
                } else {
                    // display information of the clicked point
                    Toast.makeText(
                            getActivity(),
                            "Chart element in series index " + seriesSelection.getSeriesIndex()
                                    + " data point index " + seriesSelection.getPointIndex() + " was clicked"
                                    + " closest point value X=" + seriesSelection.getXValue() + ", Y="
                                    + seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mChartView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
                if (seriesSelection == null) {
                    Toast.makeText(getActivity(), "No chart element was long pressed",
                            Toast.LENGTH_SHORT);
                    return false; // no chart element was long pressed, so let something
                    // else handle the event
                } else {
                    Toast.makeText(getActivity(), "Chart element in series index "
                            + seriesSelection.getSeriesIndex() + " data point index "
                            + seriesSelection.getPointIndex() + " was long pressed", Toast.LENGTH_SHORT);
                    return true; // the element was long pressed - the event has been
                    // handled
                }
            }
        });
        //这段代码处理放大缩小
        //-->start
        mChartView.addZoomListener(new ZoomListener() {
            public void zoomApplied(ZoomEvent e) {
                String type = "out";
                if (e.isZoomIn()) {
                    type = "in";
                }
                zoomsize = e.getZoomRate();
                System.out.println("Zoom " + type + " rate " + e.getZoomRate());
            }

            public void zoomReset() {
                System.out.println("Reset");
                //设置 初始化的值
//                mRenderer.setInitialRange(new double[]{0d, 5d, 0d, 10d},0);
            }
        }, true, true);
        //-->end
        //设置拖动图表时后台打印出图表坐标的最大最小值.
        mChartView.addPanListener(new PanListener() {
            public void panApplied() {
                System.out.println("New X range=[" + mRenderer.getXAxisMin() + ", " + mRenderer.getXAxisMax()
                        + "], Y range=[" + mRenderer.getYAxisMax() + ", " + mRenderer.getYAxisMax() + "]");
            }
        });
        xYSeries = abstractChartUtil.getXYSerie();
        //abstractChartUtil.resetXYservesAll(xYSeries); //初始化值
        simpleSeriesRenderer = mRenderer.getSeriesRenderers();
        layout.addView(mChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        mChartView.repaint();

    }

    /**
     * 开启tcp 服务器
     */
    public void startTcpServer(){
        tcpServer = new TcpServer(myHandler);
        tcpServer.startTcpServer();
    }

    /**
     * 停止 tcp服务器
     */
    public void stopTcpServer(){
        tcpServer.stopTcp();
    }

    public void sendApSSID(String SSID){

    }


    @Override
    public void onPause() {
        super.onPause();
        if(ms!=null){
            ms.setIsKeepAlive(false);
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        ms  = new GetDateSocket();
        ms.setIsKeepAlive(true);
        ms.setmWifiUtils(mWifiUtils);
        ms.setSpf(sharedFileUtils);
        ms.setMyHandler(myHandler);
        ms.start();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mColorRes", mColorRes);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initEvents() {

    }
}
