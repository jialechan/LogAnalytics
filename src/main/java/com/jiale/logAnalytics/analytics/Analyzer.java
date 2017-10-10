package com.jiale.logAnalytics.analytics;

import com.jiale.logAnalytics.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class Analyzer {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public AnalyticsResult analytics(List<File> logFiles, Filter filter) {

        AnalyticsResult analyticsResult = new AnalyticsResult();

        for(File logFile : logFiles) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)))) {
                String line;
                while ((line = br.readLine()) != null) {
                    analyticsLine(analyticsResult, line, filter);
                }
            } catch (Exception e) {
                log.error("分析日志文件出错", e);
            }
        }

        log.info("扫描行数(没经过过滤关键字的): {}", analyticsResult.getTotalLineScan());

        return analyticsResult;
    }

    private void analyticsLine(AnalyticsResult analyticsResult, String line, Filter filter) {
        analyticsResult.setTotalLineScan(analyticsResult.getTotalLineScan() + 1);
        LineInfo lineInfo = LineInfoParser.parse(line);
        if(lineInfo != null) {
            if(isNotInKeyWord(lineInfo.getUri(), filter.getAccessIgnoreStartWithList())) {
                analyticsResult.setTotalAccess(analyticsResult.getTotalAccess() + 1);
                addAccessCount(analyticsResult, lineInfo);
                addHttpStatusCount(analyticsResult, lineInfo);
            }
            if(isNotInKeyWord(lineInfo.getUri(), filter.getCallIgnoreStartWithList())) {
                addCallCount(analyticsResult, lineInfo);
            }
            if(isNotInKeyWord(lineInfo.getUri(), filter.getConsumeIgnoreStartWithList())) {
                addConsumeAvgCount(analyticsResult, lineInfo);
                addConsumeTotalCount(analyticsResult, lineInfo);
            }

        }
    }

    private boolean isNotInKeyWord(String key, List<String> shouldNotStartWithList) {
        for(String shouldNotStartWith : shouldNotStartWithList) {
            if(key.startsWith(shouldNotStartWith)) {
                return false;
            }
        }
        return true;
    }

    private void addConsumeAvgCount(AnalyticsResult analyticsResult, LineInfo lineInfo) {
        if(lineInfo.getRequestTime() != null) {
            double consumeCount = analyticsResult.getConsumeAvgResult().get(lineInfo.getUri()) == null ?
                    0.0 : analyticsResult.getConsumeAvgResult().get(lineInfo.getUri());
            analyticsResult.getConsumeAvgResult().put(lineInfo.getUri(), (consumeCount + lineInfo.getRequestTime()) / 2);
        }
    }

    private void addConsumeTotalCount(AnalyticsResult analyticsResult, LineInfo lineInfo) {
        if(lineInfo.getRequestTime() != null) {
            double consumeCount = analyticsResult.getConsumeTotalResult().get(lineInfo.getUri()) == null ?
                    0.0 : analyticsResult.getConsumeTotalResult().get(lineInfo.getUri());
            analyticsResult.getConsumeTotalResult().put(lineInfo.getUri(), consumeCount + lineInfo.getRequestTime());
        }
    }

    private void addCallCount(AnalyticsResult analyticsResult, LineInfo lineInfo) {
        int callCount = analyticsResult.getCallResult().get(lineInfo.getUri()) == null ?
                0 : analyticsResult.getCallResult().get(lineInfo.getUri());
        analyticsResult.getCallResult().put(lineInfo.getUri(), callCount + 1);
    }

    private void addAccessCount(AnalyticsResult analyticsResult, LineInfo lineInfo) {
        int accessCount = analyticsResult.getAccessResult().get(lineInfo.getTime()) == null ?
                0 : analyticsResult.getAccessResult().get(lineInfo.getTime());
        analyticsResult.getAccessResult().put(lineInfo.getTime(), accessCount + 1);
    }

    private void addHttpStatusCount(AnalyticsResult analyticsResult, LineInfo lineInfo) {
        int count = analyticsResult.getHttpStatusResult().get(lineInfo.getStat()) == null ?
                0 : analyticsResult.getHttpStatusResult().get(lineInfo.getStat());
        analyticsResult.getHttpStatusResult().put(lineInfo.getStat(), count + 1);
    }

}
