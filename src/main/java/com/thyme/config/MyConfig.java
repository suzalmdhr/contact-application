package com.thyme.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
//	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		
//		UserDetails adminUser =User.withUsername("suzal")
//									.password(passwordEncoder().encode("password"))
//									.roles("USER")
//									.build();
//		
//		return new InMemoryUserDetailsManager(adminUser);
//		
//	}
	
	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public DaoAuthenticationProvider authProvider() {
		
		DaoAuthenticationProvider prov=new DaoAuthenticationProvider();
		prov.setUserDetailsService(this.getUserDetailsService());
		prov.setPasswordEncoder(passwordEncoder());
		return prov;
		
	}
	
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}
	

		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			
			http.csrf().disable()
			.authorizeHttpRequests()
			.requestMatchers("/user/**")
			.hasRole("USER")
			
			
			.requestMatchers("/**")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.formLogin()
			.loginPage("/signin")
			.loginProcessingUrl("/dologin")
			.defaultSuccessUrl("/user/index");
			
			
			return http.build();
		}
	
}
