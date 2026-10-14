package com.preppulse.config;
import org.springframework.beans.factory.annotation.Value; import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty; import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration; import org.springframework.mail.javamail.JavaMailSender; import org.springframework.mail.javamail.JavaMailSenderImpl;
@Configuration
public class EmailConfig {
 @Bean @ConditionalOnProperty(name="preppulse.email.enabled", havingValue="true")
 public JavaMailSender preppulseMailSender(@Value("${preppulse.email.host}") String host,@Value("${preppulse.email.port:587}") int port,@Value("${preppulse.email.username:}") String username,@Value("${preppulse.email.password:}") String password,@Value("${preppulse.email.properties.mail.smtp.auth:true}") boolean auth,@Value("${preppulse.email.properties.mail.smtp.starttls.enable:true}") boolean startTls){JavaMailSenderImpl s=new JavaMailSenderImpl();s.setHost(host);s.setPort(port);if(username!=null&&!username.isBlank())s.setUsername(username);if(password!=null&&!password.isBlank())s.setPassword(password);java.util.Properties p=s.getJavaMailProperties();p.put("mail.smtp.auth",String.valueOf(auth));p.put("mail.smtp.starttls.enable",String.valueOf(startTls));p.put("mail.smtp.connectiontimeout","5000");p.put("mail.smtp.timeout","5000");return s;}
}
