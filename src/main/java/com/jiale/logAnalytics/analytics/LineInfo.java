package com.jiale.logAnalytics.analytics;

public class LineInfo {

    private String time;
    private String stat;
    private Double requestTime;
    private String uri;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public Double getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Double requestTime) {
        this.requestTime = requestTime;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}