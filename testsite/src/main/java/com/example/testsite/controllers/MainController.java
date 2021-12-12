package com.example.testsite.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/")
	public String greeting(Model model) {
		model.addAttribute("title", "Главная страница");
		return "home";
	}

	@GetMapping("/about")
	public String page1(Model model) {
		model.addAttribute("about", "Об авторе");
		return "about";
	}
	
}
