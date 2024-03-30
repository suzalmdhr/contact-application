package com.thyme.service;

import java.util.Properties;

import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	
public boolean sendEmail(String to,String subject,String message) {
		
		boolean flag=false;
		
		Properties props=new Properties();
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		
		String user="sujalmessisayami@gmail.com";
		String pass="vcyu dvyu frhr zfqf";
		String from="sujalmessisayami@gmail.com";
		
		
		Session session = Session.getInstance(props, new Authenticator() {
			
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, pass);
			}
		});
		
		try {
			
			Message messages=new MimeMessage(session);
			messages.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			messages.setFrom(new InternetAddress(from));
			messages.setSubject(subject);
			messages.setContent(message,"text/html");
			
			Transport.send(messages);
			flag=true;
			
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}

}
