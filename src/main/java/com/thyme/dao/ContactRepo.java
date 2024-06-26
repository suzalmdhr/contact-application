package com.thyme.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thyme.entities.Contact;

public interface ContactRepo extends JpaRepository<Contact, Integer> {

	
	@Query("from Contact as c where c.user.id =:userId")
	public Page<Contact> getUserByUserName(@Param("userId") int userId,Pageable ppage);
	

}
