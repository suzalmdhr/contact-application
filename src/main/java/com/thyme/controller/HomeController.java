package com.thyme.controller;

import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.thyme.dao.UserRepo;
import com.thyme.entities.User;
import com.thyme.message.MyMessage;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	
	@RequestMapping("/")
	public String home2(Model m) {
		m.addAttribute("title", "Home- Smart Contact Manager");
		
		
		return "home";
	}
	@RequestMapping("/arko")
	public String essai() {
		
		return "arko";
	}
	
	@RequestMapping("/about")
	public String about(Model m) {
		m.addAttribute("title","About- Smart Contact Manager");
		
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("user", new User());
		return "signup";
	}
	
	@PostMapping("/do_register")
	public String processF(@Valid @ModelAttribute("user") User user,BindingResult result,
	@RequestParam(value="agreeds" ,defaultValue = "false") boolean agreement,	Model m,HttpSession session) {
		
		try {
			
			if(!agreement) {
				
				throw new Exception("You have not agreed terms and conditions");
				
			}
			
			
			if(result.hasErrors()) {
				m.addAttribute("user", user);
				return "signup";
		
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("Bike.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			 this.userRepo.save(user);
			m.addAttribute("user", new User());
			session.setAttribute("message", new MyMessage("Successfully inserted", "alert-success"));
			
		
			System.out.println(user);
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			m.addAttribute("user", user);
			
			session.setAttribute("message", new MyMessage("Something went wrong "+e.getMessage(),"alert-danger"));
			return "signup";
		}
		
			
		
		return "signup";
	}

	@GetMapping("/signin")
	public String login( Model m) {
		m.addAttribute("title","Login Page");
		
		return "login";
	}
	
}
