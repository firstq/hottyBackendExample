package su.hotty.editor.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.security.sasl.AuthenticationException;
import java.util.Properties;

/**
 * Конфигурация почтового клиента, отправляющего уведомления пользователям Системы.
 */
@Configuration
public class MailConfig {

    private static final Logger log = LoggerFactory.getLogger(MailConfig.class);

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private int port;

    @Value("${mail.protocol}")
    private String protocol;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.smtp.auth}")
    private boolean smtpAuth;

    @Value("${mail.smtp.starttls.enable}")
    private boolean smtpStartTls;

    @Value("${mail.smtp.ssl}")
    private boolean smtpSsl;

    @Value("${mail.smtp.localhost}")
    private String smtpLocalhost;

    @Value("${mail.smtp.defaultencoding}")
    private String smtpDefaultEncoding;

    @Value("${mail.mime.charset}")
    private String smtpMimeCharset;

    @Value("${mail.smtp.allow8bitmime}")
    private boolean smtpAllow8bitMime;

    @Value("${mail.smtps.allow8bitmime}")
    private boolean smtpsAllow8bitMime;

    @Value("${mail.from.address}")
    private String fromAddress;

    @Bean
    public JavaMailSenderImpl getMailSender() throws AuthenticationException {
        log.debug("Mail server host: {} port: {} username: {} smtp.auth: {} tls: {} fromAddress: {}",
                host, port, username, smtpAuth, smtpStartTls, fromAddress);

        JavaMailSenderImpl ms = new JavaMailSenderImpl();
        ms.setHost(host);
        ms.setPort(port);
        ms.setProtocol(protocol);
        ms.setDefaultEncoding(smtpDefaultEncoding);

        if (smtpAuth) {
            ms.setUsername(username);
            ms.setPassword(password);
        }

        Properties properties = new Properties();
        properties.setProperty("mail.mime.charset", smtpMimeCharset);
        properties.setProperty("mail.smtp.allow8bitmime", String.valueOf(smtpAllow8bitMime));
        properties.setProperty("mail.smtps.allow8bitmime", String.valueOf(smtpsAllow8bitMime));
        properties.setProperty("mail.smtp.auth", String.valueOf(smtpAuth));
        properties.setProperty("mail.smtp.starttls.enable", String.valueOf(smtpStartTls));
        properties.setProperty("mail.smtp.localhost", smtpLocalhost);
        properties.setProperty("mail.smtp.ssl", String.valueOf(smtpSsl));
        ms.setJavaMailProperties(properties);

        return ms;
    }

    @Bean
    public SimpleMailMessage getMessageTemplate() {
        SimpleMailMessage m = new SimpleMailMessage();
        m.setFrom(fromAddress);
        // тема сообщения по умолчание
        m.setSubject("HOTTY.SU:");
        return m;
    }
}
