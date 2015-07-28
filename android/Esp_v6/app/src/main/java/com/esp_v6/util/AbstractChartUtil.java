package com.esp_v6.util;

import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/7/24.
 */
public class AbstractChartUtil {
    XYMultipleSeriesDataset dataset;
    XYMultipleSeriesRenderer renderer;
    /**
     * Builds an XY multiple dataset using the provided values.
     *
     * @param titles the series titles
     * @param xValues the values for the X axis
     * @param yValues the values for the Y axis
     * @return the XY multiple dataset
     */
    public XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues,
                                                   List<double[]> yValues) {
        this.dataset = new XYMultipleSeriesDataset();
        addXYSeries(titles, xValues, yValues, 0);
        return dataset;
    }

    public void refresh(GraphicalView mChartView){
        mChartView.repaint();
    }

    public void addXYSeries(String[] titles, List<double[]> xValues,
                            List<double[]> yValues, int scale) {
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i], scale);
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
    }

    /**
     * get XYservie object
     * @param i number   0. V;1. I. 2.W
     * @return
     */
    public XYSeries[] getXYSerie(){
        XYSeries[] xYSeries = dataset.getSeries();
        return xYSeries;
    }



    /**
     * 移除指定的 xyServies
     *  remove xyseries or add
     * @param xYSeries
     * @param xyEnabled  ture add flase remove
     */
    public void removeXYServes(XYSeries xYSeries,boolean xyEnabled,SimpleSeriesRenderer simpleSeriesRenderer){
        //dataset.getSeries()[i];
       // XYSeries series =  getXYSerie(i);
        if(!xyEnabled){
            renderer.removeSeriesRenderer(simpleSeriesRenderer);
            dataset.removeSeries(xYSeries);
            // XYSeriesRenderer r = xYSeries.
        }
        else {
            renderer.addSeriesRenderer(simpleSeriesRenderer);
            dataset.addSeries(xYSeries);
        }
    }

    /**
     * 移除初始化的值
     * @param xYSeries
     */
    public void resetXYservesAll(XYSeries[] xYSeries){
        for(int i = 0; i < xYSeries.length-1; i++){
            dataset.removeSeries(xYSeries[i]);
        }
    }

    /**
     * Builds an XY multiple series renderer.
     *
     * @param colors the series rendering colors
     * @param styles the series point styles
     * @return the XY multiple series renderers
     */
    public XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
        renderer = new XYMultipleSeriesRenderer();
        setRenderer(renderer, colors, styles);
        return renderer;
    }



    public void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setPointSize(5f);
        renderer.setMargins(new int[]{20, 30, 15, 20});
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }
    }

    /**
     * Sets a few of the series renderer settings.
     *
     * @param renderer the renderer to set the properties to
     * @param title the chart title
     * @param xTitle the title for the X axis
     * @param yTitle the title for the Y axis
     * @param xMin the minimum value on the X axis
     * @param xMax the maximum value on the X axis
     * @param yMin the minimum value on the Y axis
     * @param yMax the maximum value on the Y axis
     * @param axesColor the axes color
     * @param labelsColor the labels color
     */
    public void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
                                    String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
                                    int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }



    /**
     * Builds an XY multiple time dataset using the provided values.
     *
     * @param titles the series titles
     * @param xValues the values for the X axis
     * @param yValues the values for the Y axis
     * @return the XY multiple time dataset
     */
    public XYMultipleSeriesDataset buildDateDataset(String[] titles, List<Date[]> xValues,
                                                       List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            TimeSeries series = new TimeSeries(titles[i]);
            Date[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
        return dataset;
    }

    /**
     * Builds a category series using the provided values.
     *
     * @param title the series titles
     * @param values the values
     * @return the category series
     */
    public CategorySeries buildCategoryDataset(String title, double[] values) {
        CategorySeries series = new CategorySeries(title);
        int k = 0;
        for (double value : values) {
            series.add("Project " + ++k, value);
        }

        return series;
    }

    /**
     * Builds a multiple category series using the provided values.
     *
     * @param titles the series titles
     * @param values the values
     * @return the category series
     */
    public MultipleCategorySeries buildMultipleCategoryDataset(String title,
                                                                  List<String[]> titles, List<double[]> values) {
        MultipleCategorySeries series = new MultipleCategorySeries(title);
        int k = 0;
        for (double[] value : values) {
            series.add(2007 + k + "", titles.get(k), value);
            k++;
        }
        return series;
    }

    /**
     * Builds a category renderer to use the provided colors.
     *
     * @param colors the colors
     * @return the category renderer
     */
    public DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setMargins(new int[] { 20, 30, 15, 0 });
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    /**
     * Builds a bar multiple series dataset using the provided values.
     *
     * @param titles the series titles
     * @param values the values
     * @return the XY multiple bar dataset
     */
    public XYMultipleSeriesDataset buildBarDataset(String[] titles, List<double[]> values) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            CategorySeries series = new CategorySeries(titles[i]);
            double[] v = values.get(i);
            int seriesLength = v.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(v[k]);
            }
            dataset.addSeries(series.toXYSeries());
        }
        return dataset;
    }

    public void removeSimpleSeriesRenderer(SimpleSeriesRenderer SimpleSeriesRenderer,boolean ssrE){
        if(!ssrE){
            renderer.removeSeriesRenderer(SimpleSeriesRenderer);
        }else{
            renderer.addSeriesRenderer(SimpleSeriesRenderer);
        }
    }

    /**
     *
     * @param xYSeries
     * @param x
     * @param y
     * @param mChartView
     * @param ref  是否开始刷新
     */
    public void updateXYDate(XYSeries xYSeries,double x,double y,GraphicalView mChartView,boolean ref){
        xYSeries.add(x, y);
        if(ref) {
            int nowxman = (int) renderer.getXAxisMax();
            int nowmin= (int) renderer.getXAxisMin();
            int minmodle =nowxman-(int)x;  //设置 间隔
            if(minmodle<=2 && minmodle>0) {
                int xmin = (int) renderer.getXAxisMin();
                int xman =(int) renderer.getXAxisMax();
                if(xman-xmin==12||xman-xmin==13) {
                    renderer.setInitialRange(new double[]{xmin, xman, renderer.getXAxisMin(), renderer.getYAxisMax()}, 0);
                }
                xmin++;
                xman++;
//                renderer.setXAxisMin(xmin);
//                renderer.setXAxisMax(xman);
                     renderer.setRange(new double[]{xmin, xman, renderer.getYAxisMin(), renderer.getYAxisMax()});// 设置显示的的范围

            }

            mChartView.repaint();
            //mChartView.findFocus();
        }
    }

    /**
     * Builds a bar multiple series renderer to use the provided colors.
     *
     * @param colors the series renderers colors
     * @return the bar multiple series renderer
     */
    public XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
        renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[i]);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

}
