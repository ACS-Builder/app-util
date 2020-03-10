package com.kasafal.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;

public class MailService {
	
	@Autowired
	private JavaMailSenderImpl sender;
	private MimeMailMessage message;
	
	public JavaMailSenderImpl getSender() {
		return sender;
	}
	public void setSender(JavaMailSenderImpl sender) {
		this.sender = sender;
	}
	public MimeMailMessage getMessage() {
		return message;
	}
	public void setMessage(MimeMailMessage message) {
		this.message = message;
	}
}
    