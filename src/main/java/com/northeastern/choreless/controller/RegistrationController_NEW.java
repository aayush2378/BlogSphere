package com.northeastern.choreless.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.northeastern.choreless.dao.UserDAO;
import com.northeastern.choreless.model.User;

@Controller
public class RegistrationController_NEW {
	@Autowired
	UserDAO userDAO;
	
	@PostMapping("/createGroup")
	public ResponseEntity<User> registerUser(User user){
		User savedUser = userDAO.save(user);
        return ResponseEntity.ok(savedUser);
	}
}
