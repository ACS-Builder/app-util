package com.kasafal.mail;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.kasafal.system.config.BeanConfigConstant;

public class MailServiceUtil {
	public static final String DEFAULT_SENDER= "Default-Sender";
	public static final String DEFAULT_SENDER_NAME= "Default-Sender-Name";

	private static JavaMailSenderImpl service;
	private static Properties properties;
	
	private MailServiceUtil(){
		
	}
	public void setService(JavaMailSenderImpl service) {
		this.service = service;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	private static MailServiceUtil getService() {
		ApplicationContext context = new ClassPathXmlApplicationContext(BeanConfigConstant.EMAIL_CONFIG);
		return (MailServiceUtil) context.getBean(MailServiceUtil.class);
	}

	public static void sendMail(String recipient, String subject, String body, File attachment, String from,
			String senderName) {

		if(null== service) {
			getService();
		}
		service.setJavaMailProperties(properties);
		MimeMessage message = service.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, attachment!=null);
			helper.addTo(recipient);
			helper.setSubject(subject);

			if (body != null)
				helper.setText(body,true);
			if (attachment != null)
				helper.addAttachment(attachment.getName(), attachment);

			helper.setFrom(from != null ? from : properties.getProperty(DEFAULT_SENDER),
					senderName != null ? senderName : properties.getProperty(DEFAULT_SENDER_NAME));
			
			service.send(message);
			
		} catch (MessagingException | IOException e) {
			System.out.println("Error in preparing the message helper");
			e.printStackTrace();
		}

	}

	public static void sendMail(String recipient, String subject, String message, File attachment) {
		sendMail(recipient, subject, message, attachment, null, null);
	}

	public static void sendMail(String recipient, String subject, String message) {
		sendMail(recipient, subject, message, null, null, null);
	}

}
