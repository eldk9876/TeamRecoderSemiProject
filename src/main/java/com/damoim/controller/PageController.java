package com.damoim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/")
	public String login() {
		return "login";
	}
	@GetMapping("/")
	public String signUp() {
		return "signUp";
	}
//	
//	@GetMapping("/")
//	public String register() {
//		return "register";
//	}
//	
//	@GetMapping("/")
//	public String mypage() {
//		return "mypage";
//	}
//	
//	
	
}
