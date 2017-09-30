package com.jiale.logAnalytics.core;

import com.jiale.logAnalytics.Application;
import org.apache.commons.mail.EmailException;
import org.junit.Test;
import org.springframework.boot.SpringApplication;

import java.io.IOException;
import java.text.ParseException;

public class RunTest {

    @Test
    public void coreTest() throws ParseException, EmailException, IOException {
        String[] args = {"--dateStr=2017-09-28"};
        SpringApplication.run(Application.class, args);
    }

}