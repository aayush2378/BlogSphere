package com.northeastern.choreless.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.northeastern.choreless.dao.RoommateDAO;
import com.northeastern.choreless.model.ChoreGroup;
import com.northeastern.choreless.model.Roommate;

import jakarta.servlet.ServletContext;

@Controller
public class LoginController {

	@Autowired
	RoommateDAO roomatedao;
	
	@Autowired
	ServletContext context;
	
	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping("/authorize")
	public String authorize(@RequestParam(name = "username") String email,
			@RequestParam(name = "password") String password, Model model) {

//			find email from roommateDAO - return Roommate obj

		Roommate user = roomatedao.getByEmail(email);

		int adminId = user.getChoregroup().getAdmin().getId();

// 			get groupID from roomate object 
		
		
		
		String error = "";

		if (user.getChoregroup().getPassword().equals(password)) {

			model.addAttribute("welcome", "Login Successful, Welcome " + user.getChoregroup().getGroupName() + "!");
			model.addAttribute("chores", user.getChoregroup().getChores());

			model.addAttribute("roommates",user.getChoregroup().getRoommates());
			if (adminId == user.getId()) {
				context.setAttribute("groupid", user.getChoregroup().getGroupId());
				return "admin_home";
			}
			return "home";
		} else {
			System.out.println("Login Failed");
			error = "Login Failed";
		}

		model.addAttribute("error", error);
		model.addAttribute("groupid", user.getChoregroup().getGroupId());
		return "login";
	}

	@RequestMapping("/loginspring")
	public String loginspring() {
		return "loginspring";
	}

	@RequestMapping("/logout")
	public String logout(Model model) {
		model.addAttribute("msg", "Logged out!");
		return "login";
	}

}
