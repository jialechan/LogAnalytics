package com.jiale.logAnalytics.analytics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.*;

public class AnalyticsResult {

    private static Logger log = LoggerFactory.getLogger(AnalyticsResult.class);

    private Map<String, Integer> accessResult = initAccessResult();
    private Map<String, Integer> callResult = new HashMap<>();
    private Map<String, Double> consumeAvgResult = new HashMap<>();
    private Map<String, Double> consumeTotalResult = new HashMap<>();
    private Map<String, Integer> httpStatusResult = new HashMap<>();

    private int totalLineScan = 0;
    private int totalAccess = 0;

    private Map<String, Integer> initAccessResult() {
        final Map<String, Integer> result = new LinkedHashMap<>();
        DecimalFormat format = new DecimalFormat("00");
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j++) {
                String key = format.format(i) + ":" + format.format(j) + ":00";
                result.put(key, 0);
            }
        }
        return result;
    }

    public Map<String, Integer> getAccessResult() {
        return accessResult;
    }

    public void setAccessResult(Map<String, Integer> accessResult) {
        this.accessResult = accessResult;
    }

    public Map<String, Integer> getCallResult() {
        return callResult;
    }

    public void setCallResult(Map<String, Integer> callResult) {
        this.callResult = callResult;
    }

    public Map<String, Double> getConsumeAvgResult() {
        return consumeAvgResult;
    }

    public void setConsumeAvgResult(Map<String, Double> consumeAvgResult) {
        this.consumeAvgResult = consumeAvgResult;
    }

    public int getTotalAccess() {
        return totalAccess;
    }

    public void setTotalAccess(int totalAccess) {
        this.totalAccess = totalAccess;
    }

    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        AnalyticsResult.log = log;
    }

    public int getTotalLineScan() {
        return totalLineScan;
    }

    public void setTotalLineScan(int totalLineScan) {
        this.totalLineScan = totalLineScan;
    }

    public Map<String, Double> getConsumeTotalResult() {
        return consumeTotalResult;
    }

    public void setConsumeTotalResult(Map<String, Double> consumeTotalResult) {
        this.consumeTotalResult = consumeTotalResult;
    }

    public Map<String, Integer> getHttpStatusResult() {
        return httpStatusResult;
    }

    public void setHttpStatusResult(Map<String, Integer> httpStatusResult) {
        this.httpStatusResult = httpStatusResult;
    }
}


