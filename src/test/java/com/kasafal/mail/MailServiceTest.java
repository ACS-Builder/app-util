package com.kasafal.mail;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.kasafal.classloader.ApplicationClassLoader;


public class MailServiceTest {

	ApplicationContext ctx;
	
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("Mail-Config.xml");
		System.getProperties().put("logConfigFile", "src/main/resources/App-log4j.xml");
		Class.forName("com.kasafal.classloader.ApplicationClassLoader");
	}

	@Test
	public void test() {
		SimpleMailMessage message = (SimpleMailMessage) ctx.getBean("Default_Message");
		JavaMailSenderImpl sender =  (JavaMailSenderImpl) ctx.getBean("Default_Sender");
		message.setTo("ajaydas.6080@gmail.com");
		message.setText("Thanks for being a part of Kasafal.com");
		sender.send(message);
	}
	
	@Test()
	public void test2() throws InterruptedException {
		MailServiceUtil.sendMail("ajaydas.6080@gmail.com", "Good job", "Test completed successfully <a href='https://kasafal.com'>Kasafal</a>");
		Thread.sleep(10000);
		MailServiceUtil.sendMail("cet.info.ajay@gmail.com", "Good job", "Test completed successfully <a href='https://kasafal.com'>Kasafal</a>");
		Thread.sleep(10000);
	}
	
	@Test
	public void testLogger() {
		Logger logger = Logger.getLogger(CustomJavaMailSenderImpl.class);
		for(int i=0;i<10;i++)
		logger.debug("Logging started......");
	}
	

}
