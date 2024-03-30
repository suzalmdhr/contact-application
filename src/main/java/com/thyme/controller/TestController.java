package com.thyme.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.thyme.dao.ContactRepo;
import com.thyme.dao.UserRepo;
import com.thyme.entities.Contact;
import com.thyme.entities.User;
import com.thyme.entities.password;
import com.thyme.message.MyMessage;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class TestController {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ContactRepo contactRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	

	
	
	@ModelAttribute
	public void addCommonData(Model m,Principal princi) {
		String name = princi.getName();
		System.out.println(name);
		
		User users = userRepo.getUserByUserName(name);
		System.out.println(users);
		
		m.addAttribute("user", users);
	}
	
	
	@GetMapping("/add-contact")
	public String addContactForm(Model model) {
		
		
		model.addAttribute("title","Add-Contact");
		model.addAttribute("contact",new Contact());
		return "normal/add_contact_form";
		
	}
	
	
	//homepage after signing in
	@GetMapping("/index")
	public String index(Model m,Principal princi) {
		
		m.addAttribute("title","User Dashboard");
		return "normal/user_dashboard";
	}
	
	
	//contact lai save garne in database
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
			@RequestParam("profileImage") MultipartFile file,
			Principal princi,HttpSession session) {
		
			try {
				String uName = princi.getName();
				User upUser = this.userRepo.getUserByUserName(uName);
				
				if(file.isEmpty()) {
					System.out.println("File is empty");
				}
				else {
					contact.setImage(file.getOriginalFilename());
					
					File file2 = new ClassPathResource("static/img").getFile();
					
					Path path = Paths.get(file2.getAbsolutePath()+File.separator+file.getOriginalFilename());
					
					Files.copy(file.getInputStream(),path ,StandardCopyOption.REPLACE_EXISTING);
					
					System.out.println("File also uploaded");
				}
				
				contact.setUser(upUser);
				
				upUser.getContacts().add(contact);
				this.userRepo.save(upUser);
			
				session.setAttribute("message",new MyMessage("Successfully inserted", "alert-success"));
				
				
				
			System.out.println("Users contacts are"+ upUser);
			System.out.println("data" + contact);
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		
		
		return "normal/add_contact_form";
	}
	
	
	//showing contact of a particular user
	@GetMapping("/show_contacts/{page}")
	public String showContacts(@PathVariable("page") int page,Model m,Principal princi) {
		m.addAttribute("title", "Show Contacts");
		m.addAttribute("message", "Message from the god");
		
		String name = princi.getName();
		User user = this.userRepo.getUserByUserName(name);
		
		//current page ra no of contacts in a page chayeko re
		PageRequest pages = PageRequest.of(page,3);
		
		  Page<Contact> contacts = this.contactRepo.getUserByUserName(user.getId(),pages);
		
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentpage",page);
		m.addAttribute("totalpages", contacts.getTotalPages());
		
		return "normal/show_contacts";
	}
	
	//deleting contact of a particular user
	@GetMapping("/delete/{cid}")
	public String del(@PathVariable("cid") int cid,Principal princi,HttpSession session) {
		
		Optional<Contact> contactoptional = this.contactRepo.findById(cid);
		Contact contact = contactoptional.get();
		
		String name = princi.getName();
		User user = this.userRepo.getUserByUserName(name);
		
		if(user.getId()==contact.getUser().getId()) {
		
		this.contactRepo.deleteById(cid);
		
		session.setAttribute("message",new MyMessage("Successfully deleted", "alert-success"));
		}
		else {
			
			session.setAttribute("message", new MyMessage("No such entry", "alert-warning"));
		}
		
		return "redirect:/user/show_contacts/0";
	}
	
	
	//first step of update is to take him to next view to update
	@PostMapping("/update/{cid}")
	
	public String update(@PathVariable("cid") int cid,Model m) {
		m.addAttribute("title","Update-Contacts");
		
		Contact user = this.contactRepo.findById(cid).get();
		m.addAttribute("contact",user);
		return "normal/update_form";
	}
	
	//second step is really updating and processing 
	@PostMapping("/update-contact")
	public String updateHandler(@ModelAttribute Contact contact,Principal princi) {
		
		System.out.println("Name is" + contact.getName());
		System.out.println("Id is "+ contact.getcId());
		
		User user = userRepo.getUserByUserName(princi.getName());
		contact.setUser(user);
		
		this.contactRepo.save(contact);
		
		return "redirect:/user/show_contacts/0";
	}
	
	@GetMapping("/profile")
	public String profile(Model m) {
		
		m.addAttribute("title", "My Profile");
		return "normal/profile";
	}
	
	@GetMapping("/settings")
	public String settings(Model m) {
		m.addAttribute("title","Settings");
		
		return "normal/settings";
	}
	
	
	@PostMapping("/change")
	public String changePassword(@ModelAttribute password pass ,Principal princi,HttpSession session) {
		
		User users1 = this.userRepo.getUserByUserName(princi.getName());
		System.out.println(users1.getPassword());
		
		
		
		System.out.println(pass.getOldPass());
		
		//.matchers bhanne function raicha encoder ma
		
			if(passwordEncoder.matches(pass.getOldPass(), users1.getPassword())) {
				
				
				users1.setPassword(passwordEncoder.encode(pass.getNewPass()));
				this.userRepo.save(users1);
				session.setAttribute("message", new MyMessage("Password changed successfully", "alert-success"));
				
			}
			else {
				
				session.setAttribute("message", new MyMessage("Old password doesnt match", "alert-danger"));
			}
		
		return "normal/settings";
	}


}
