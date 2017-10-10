package com.jiale.logAnalytics.analytics;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineInfoParser {

    private static Pattern pattern = Pattern.compile("T\\[(.*?)T(.*?):(.*?):(.*?)\\+(.*?)\\] STAT\\[(.*?)\\] REQ_T\\[(.*?)\\] URL\\[[GETPOST\\s]+(.*?) HTTP.*?\\]");

    public static LineInfo parse(String line) {

        LineInfo lineInfo = null;

        Matcher matcher = pattern.matcher(line);

        if (matcher.find()) {
            lineInfo = new LineInfo();
            lineInfo.setTime(matcher.group(2) + ":" + matcher.group(3) + ":00");
            lineInfo.setStat(matcher.group(6));
            final String reqStr = matcher.group(7);
            if(!"-".equals(reqStr)) {
                lineInfo.setRequestTime(Double.parseDouble(reqStr));
            }
            String uri = matcher.group(8);
            if(uri.contains("?")) {
                uri = uri.substring(0, uri.indexOf("?"));
            }
            if(uri.contains(";jsessionid=")) {
                uri = uri.substring(0, uri.indexOf(";jsessionid="));
            }
            lineInfo.setUri(uri);

        }

        return lineInfo;
    }

}