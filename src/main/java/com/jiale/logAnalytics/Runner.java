package com.jiale.logAnalytics;

import com.jiale.logAnalytics.analytics.AnalyticsResult;
import com.jiale.logAnalytics.analytics.Analyzer;
import com.jiale.logAnalytics.chart.ChartMaker;
import com.jiale.logAnalytics.chart.LineChartInfo;
import com.jiale.logAnalytics.chart.PieChartInfo;
import com.jiale.logAnalytics.config.CoreConfig;
import com.jiale.logAnalytics.config.IgnoreStartWithConfig;
import com.jiale.logAnalytics.config.MailConfig;
import com.jiale.logAnalytics.filter.Filter;
import com.jiale.logAnalytics.mail.MailSender;
import com.jiale.logAnalytics.util.GetLocalIp;
import com.jiale.logAnalytics.util.MapUtil;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.util.List;

import static com.jtool.apiclient.ApiClient.Api;

@Repository
public class Runner {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private MailConfig mailConfig;

    @Resource
    private CoreConfig coreConfig;

    @Resource
    private IgnoreStartWithConfig ignoreStartWithConfig;

    @Resource
    private Analyzer analyzer;

    @Resource
    private Filter filter;

    @Value("${dateStr}")
    private String dateStr;

    @PostConstruct
    public void run() throws IOException, ParseException {

        //分析日志
        AnalyticsResult analyticsResult = analyticsFile(dateStr);

        //生成图片
        String totalAccessBase64Data = genTotalAccessBase64Data(analyticsResult);
        String urlCallPicChartBase64Data = genUrlCallBase64Data(analyticsResult);
        String consumePieChartBase64Data = genConsumeBase64Data(analyticsResult);

        //发送邮件
        final String content = "<html><body>" +
                "<img src='data:image/png;base64, " + totalAccessBase64Data + "' /><br/><br/>" +
                "<img src='data:image/png;base64, " + urlCallPicChartBase64Data + "' /><br/><br/>" +
                "<img src='data:image/png;base64, " + consumePieChartBase64Data + "' /><br/><br/>" +
                "邮件发送于: <br/>" + fetchLocalIps() +
                "</body></html>";

        final String title = mailConfig.getMailTitle() + " [" + dateStr + "] (总计" + new DecimalFormat("#,###").format(analyticsResult.getTotalAccess()) + "次)";
        boolean isSend = false;
        for(int i = 0; i < 5; i++) {
            try {
                if(MailSender.send(mailConfig, title, content)) {
                    isSend = true;
                    break;
                }
            } catch (EmailException e) {
                log.error("第" + (i + 1) + "发送邮件失败", e);
            }
        }

        log.info("Done, isSend: " + isSend);
    }

    private String fetchLocalIps() {
        String[] ips = GetLocalIp.getAllLocalHostIP();

        String ipsStr = "";
        try {
            ipsStr = Api().get("http://169.254.169.254/latest/meta-data/public-ipv4");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(String ip : ips) {
            ipsStr += ip + "<br/>";
        }
        return ipsStr;
    }

    private String genConsumeBase64Data(AnalyticsResult analyticsResult) {
        try {
            MapUtil.sortByValueDesc(analyticsResult.getConsumeResult());

            PieChartInfo pieChartInfo = new PieChartInfo();
            pieChartInfo.setTitle("Consumed the top 20");

            return ChartMaker.makeConsumePieChart(analyticsResult, pieChartInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private String genUrlCallBase64Data(AnalyticsResult analyticsResult) {
        try {
            MapUtil.sortByValueDesc(analyticsResult.getCallResult());

            PieChartInfo pieChartInfo = new PieChartInfo();
            pieChartInfo.setTitle("Call the top 20");

            return ChartMaker.makeUrlCallPieChart(analyticsResult, pieChartInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

    private String genTotalAccessBase64Data(AnalyticsResult analyticsResult) {
        try {

            LineChartInfo lineChartInfo = new LineChartInfo();
            lineChartInfo.setTitle("Total number of requests(" + analyticsResult.getTotalAccess() + "times)");

            return ChartMaker.makeAccessCountChart(analyticsResult, lineChartInfo);

        } catch (Exception e) {
            log.error("生成总计请求发生错误", e);
            return "";
        }
    }

    private AnalyticsResult analyticsFile(String dateStr) throws ParseException, IOException {
        List<File> logsFile = new ArrayList<>();
        for (String host : coreConfig.getHostList()) {
            logsFile.add(new File(coreConfig.getLogPath() + host + ".access.log_" + dateStr));
        }
        return analyzer.analytics(logsFile, filter);
    }

}
