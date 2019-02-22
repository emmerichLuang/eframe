package com.module.mng.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mng")
public class MngController {
	
	@RequestMapping("/login")
	public String login(Model model, HttpServletResponse response) {
	    return "/login";
	}
}
