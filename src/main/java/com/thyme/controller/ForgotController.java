package com.thyme.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thyme.dao.UserRepo;
import com.thyme.entities.User;
import com.thyme.message.MyMessage;
import com.thyme.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	@GetMapping("/forgot")
	public String forgotpass() {
		
		return "forgot_page";
	}
	
	@PostMapping("sendOTP")
	public String sendOTP(@RequestParam("email") String email,HttpSession session) {
		System.out.println(email);
		
		Random random = new Random();
		int otp = random.nextInt(999999);
		System.out.println("OTP IS "+otp);
		
		
		//SEND OTP
		
		String subject="Reset password";
		String message= "<div style='border:1px solid #e2e2e2; padding:20px'> "
							+"<h1>Your OTP is "+	otp
							+"</h1></div>";
		
		boolean sendgarekoOTP = this.emailService.sendEmail(email,subject,message);
		
		
		
		User users = this.userRepo.getUserByUserName(email);
		if(users==null) {
			session.setAttribute("message", new MyMessage("Wrong email", "alert-danger"));
			return "forgot_page";
		}
		
		else {
			session.setAttribute("otp", otp);
			session.setAttribute("myemail", email);
			return "verify_otp";
		}
	
		
		//Send wala up to here
		
	
		
	}
	
	
	//verify otp wala part haita
	
	@PostMapping("/verifyOTP")
	public String verifyOTP(@RequestParam("otp") int enteredOTP,HttpSession session,Model m) {
		
		String  myemail = (String) session.getAttribute("myemail");
		int aaekoOTP= (int) session.getAttribute("otp");
		
		if(enteredOTP == aaekoOTP) {
			
			m.addAttribute("sendemail", myemail);
			return "change_password_page";
		}
		else {
			session.setAttribute("message", new MyMessage("WRONG OTP", "alert-danger"));
			return "verify_otp";
		}
		
		
		
		
	}
	
	
	//tyabata submit garesi
	// String password=get password;
	//email ni pathauna paryo kun user ho bhanera
	
	@PostMapping("/changepassword")
	public String changePass(@RequestParam("email") String getEmail,@RequestParam("nPass") String newPass,
								@RequestParam("cPass") String confirmpass,HttpSession session) {
		
		System.out.println(confirmpass);
		System.out.println(newPass);
		User changeKoUser = this.userRepo.getUserByUserName(getEmail);
		System.out.println(changeKoUser);
		
		if(confirmpass.equals(newPass)) {
			changeKoUser.setPassword(passwordEncoder.encode(newPass));
			this.userRepo.save(changeKoUser);
			session.setAttribute("message", new MyMessage("Password changed successfully", "alert-success"));
			return "change_password_page";
		}else {
			session.setAttribute("message", new MyMessage("Password didn't matched", "alert-warning"));
			return "change_password_page";
		}
		
		
			
	}
}
