package com.thyme.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.thyme.dao.UserRepo;
import com.thyme.entities.User;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;
		
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		//database bata user lai lyaune re eta
		
		
		User users = userRepo.getUserByUserName(email);
		
		if(users == null) {
			throw new UsernameNotFoundException("Could not found user");
			
			
		}
		CustomUserDetails customs =new CustomUserDetails(users);
		return customs;
	}

}
