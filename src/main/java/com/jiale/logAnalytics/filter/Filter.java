package com.jiale.logAnalytics.filter;

import com.jiale.logAnalytics.config.IgnoreStartWithConfig;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component
public class Filter {

    @Resource
    private IgnoreStartWithConfig ignoreStartWithConfig;

    private List<String> accessIgnoreStartWithList;
    private List<String> callIgnoreStartWithList;
    private List<String> consumeIgnoreStartWithList;

    @PostConstruct
    private void init() {
        this.accessIgnoreStartWithList = ignoreStartWithConfig.getAccessIgnoreStartWithList();
        this.callIgnoreStartWithList = ignoreStartWithConfig.getCallIgnoreStartWithList();
        this.consumeIgnoreStartWithList = ignoreStartWithConfig.getConsumeIgnoreStartWithList();
    }

    public List<String> getAccessIgnoreStartWithList() {
        return accessIgnoreStartWithList;
    }

    public void setAccessIgnoreStartWithList(List<String> accessIgnoreStartWithList) {
        this.accessIgnoreStartWithList = accessIgnoreStartWithList;
    }

    public List<String> getCallIgnoreStartWithList() {
        return callIgnoreStartWithList;
    }

    public void setCallIgnoreStartWithList(List<String> callIgnoreStartWithList) {
        this.callIgnoreStartWithList = callIgnoreStartWithList;
    }

    public List<String> getConsumeIgnoreStartWithList() {
        return consumeIgnoreStartWithList;
    }

    public void setConsumeIgnoreStartWithList(List<String> consumeIgnoreStartWithList) {
        this.consumeIgnoreStartWithList = consumeIgnoreStartWithList;
    }

}
