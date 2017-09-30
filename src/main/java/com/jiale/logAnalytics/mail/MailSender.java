package com.jiale.logAnalytics.mail;

import com.jiale.logAnalytics.config.MailConfig;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class MailSender {

    public static boolean send(final MailConfig mailConfig, final String title, final String content) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName(mailConfig.getSenderHost());
        email.setAuthenticator(new DefaultAuthenticator(mailConfig.getSenderUsername(), mailConfig.getSenderPassword()));
        email.setTLS(true);
        email.setFrom(mailConfig.getEmailFrom());
        email.setSubject(title);
        email.setHtmlMsg(content);
        email.setCharset("utf8");

        for (String receiver : mailConfig.getReceiverList()) {
            email.addTo(receiver);
        }

        email.send();

        return true;
    }

}
