package com.kasafal.mail;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.lang.Nullable;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class CustomJavaMailSenderImpl extends JavaMailSenderImpl implements Runnable{
	
	private Transport transport = null;
	private boolean running = false;
	private long lastCall;
	
	@Override
	protected void doSend(MimeMessage[] mimeMessages, @Nullable Object[] originalMessages) throws MailException {
		Map<Object, Exception> failedMessages = new LinkedHashMap<>();
		try {
			for (int i = 0; i < mimeMessages.length; i++) {

				// Check transport connection first...
				if (transport == null || !transport.isConnected()) {
					if (transport != null) {
						try {
							transport.close();
						}
						catch (Exception ex) {
							// Ignore - we're reconnecting anyway
						}
						transport = null;
					}
					try {
						transport = connectTransport();
					}
					catch (AuthenticationFailedException ex) {
						throw new MailAuthenticationException(ex);
					}
					catch (Exception ex) {
						// Effectively, all remaining messages failed...
						for (int j = i; j < mimeMessages.length; j++) {
							Object original = (originalMessages != null ? originalMessages[j] : mimeMessages[j]);
							failedMessages.put(original, ex);
						}
						throw new MailSendException("Mail server connection failed", ex, failedMessages);
					}
				}

				// Send message via current transport...
				MimeMessage mimeMessage = mimeMessages[i];
				try {
					if (mimeMessage.getSentDate() == null) {
						mimeMessage.setSentDate(new Date());
					}
					String messageId = mimeMessage.getMessageID();
					mimeMessage.saveChanges();
					if (messageId != null) {
						// Preserve explicitly specified message id...
						mimeMessage.setHeader("Message-ID", messageId);
					}
					Address[] addresses = mimeMessage.getAllRecipients();
					transport.sendMessage(mimeMessage, (addresses != null ? addresses : new Address[0]));
					lastCall = System.currentTimeMillis();
					maintainConnection();
				}
				catch (Exception ex) {
					Object original = (originalMessages != null ? originalMessages[i] : mimeMessage);
					failedMessages.put(original, ex);
				}
			}
		}
		finally {
			
		}

		if (!failedMessages.isEmpty()) {
			throw new MailSendException(failedMessages);
		}
	}
	
	private void maintainConnection() {
		
		if(!running) {
			Thread t = new Thread(this,"Mail Connection");
			t.setDaemon(true);
			t.start();
		}
		
	}

	@Override
	protected void finalize() throws Throwable {
		if(transport !=null) {
			Logger.getRootLogger().debug("Closing the mail connection");
			transport.close();
		}
		
	}

	@Override
	public void run() {
		running = true;
		boolean keepRunning = true;
		Logger logger = Logger.getLogger(this.getClass());
		while (keepRunning) {
			
			logger.debug("Keep mail socket connection live");
			if (System.currentTimeMillis() - lastCall > 300000) {
				try {
					logger.debug("Colosing mail socket");
					finalize();
					keepRunning=false;
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(300000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		running = false;
	}
}
