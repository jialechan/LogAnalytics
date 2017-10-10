package com.jiale.logAnalytics.chart;

import com.jiale.logAnalytics.analytics.AnalyticsResult;
import com.jiale.logAnalytics.util.MapUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;

public class ChartMaker {

    public static String makeAccessCountChart(AnalyticsResult analyticsResult, LineChartInfo lineChartInfo) throws IOException, ParseException {
        TimeSeriesCollection dataTimeSet = new TimeSeriesCollection();
        TimeSeries timesre = new TimeSeries("Total number of requests", Minute.class);

        for(Map.Entry<String, Integer> item : analyticsResult.getAccessResult().entrySet()) {
            timesre.add(new Minute(new SimpleDateFormat("HH:mm:'00'").parse(item.getKey())), item.getValue());
        }

        dataTimeSet.addSeries(timesre);


        JFreeChart chartTime = ChartFactory.createTimeSeriesChart(lineChartInfo.getTitle(), "server time", "times / min", dataTimeSet, true, true, false);
        chartTime.setBackgroundPaint(Color.white);
        XYPlot plot = chartTime.getXYPlot();
        plot.setBackgroundPaint(Color.white);// 设置网格背景色
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);// 设置网格竖线(Domain轴)颜色
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);// 设置网格横线颜色
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));// 设置曲线图与xy轴的距离

        // 设置X轴（日期轴）
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());

        int width = 1024;
        int height = 768;
        File picFile = new File("/tmp/" + UUID.randomUUID().toString() + ".png");
        ChartUtilities.saveChartAsPNG(picFile, chartTime, width, height, info);

        byte[] bytes = IOUtils.toByteArray(new FileInputStream(picFile));
        picFile.delete();
        return Base64.encodeBase64String(bytes);
    }

    public static String makeUrlCallPieChart(AnalyticsResult analyticsResult, PieChartInfo pieChartInfo) throws IOException {

        DefaultPieDataset dataset = new DefaultPieDataset();

        for(Map.Entry<String, Integer> item : MapUtil.filterFirst20Integer(analyticsResult.getCallResult()).entrySet()) {
            dataset.setValue(item.getKey(), item.getValue());
        }

        JFreeChart pieChart = ChartFactory.createPieChart(pieChartInfo.title, dataset, true, true, true);

        PiePlot plot = (PiePlot) pieChart.getPlot();

        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator("{0} {1} times ({2})", new DecimalFormat("0"), new DecimalFormat("0.00%"));
        plot.setLabelGenerator(gen);

        File picFile = new File("/tmp/" + UUID.randomUUID().toString() + ".png");
        int width = 1024;
        int height = 768;
        ChartUtilities.saveChartAsPNG(picFile, pieChart, width, height);

        byte[] bytes = IOUtils.toByteArray(new FileInputStream(picFile));
        picFile.delete();
        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
    }

    public static String makeConsumePieChart(AnalyticsResult analyticsResult, PieChartInfo pieChartInfo) throws IOException {

        DefaultPieDataset dataset = new DefaultPieDataset();

        for(Map.Entry<String, Double> item : MapUtil.filterFirst20Double(analyticsResult.getConsumeResult()).entrySet()) {
            dataset.setValue(item.getKey(), item.getValue());
        }

        JFreeChart pieChart = ChartFactory.createPieChart(pieChartInfo.getTitle(), dataset, true, true, true);

        PiePlot plot = (PiePlot) pieChart.getPlot();

        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator("{0} [{1}s] ({2})", new DecimalFormat("0.000"), new DecimalFormat("0.00%"));
        plot.setLabelGenerator(gen);

        File picFile = new File("/tmp/" + UUID.randomUUID().toString() + ".png");
        int width = 1024;
        int height = 768;
        ChartUtilities.saveChartAsPNG(picFile, pieChart, width, height);

        byte[] bytes = IOUtils.toByteArray(new FileInputStream(picFile));
        picFile.delete();
        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
    }

}
