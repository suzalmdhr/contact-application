package com.thyme.services;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class sessionHelper {
	
	
	public void removeMessageSession() {
		
		
		try {
		HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
			session.removeAttribute("message");
			session.removeAttribute("mymessage");
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
