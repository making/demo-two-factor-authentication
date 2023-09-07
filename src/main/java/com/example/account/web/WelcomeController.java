package com.example.account.web;

import com.example.account.AccountUserDetails;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

	@GetMapping(path = "/")
	public String index(@AuthenticationPrincipal AccountUserDetails accountUserDetails, Model model) {
		model.addAttribute("account", accountUserDetails.getAccount());
		return "index";
	}

}
