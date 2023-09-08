package com.example.account.web;

import com.example.account.Account;
import com.example.account.AccountService;
import com.example.account.AccountUserDetails;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

	private final AccountService accountService;

	private final PasswordEncoder passwordEncoder;

	public SignupController(AccountService accountService, PasswordEncoder passwordEncoder) {
		this.accountService = accountService;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping(path = "/signup")
	public String signup() {
		return "signup";
	}

	@PostMapping(path = "/signup")
	public String signup(SignupForm form) {
		String encoded = this.passwordEncoder.encode(form.password());
		String secret = TimeBasedOneTimePasswordUtil.generateBase32Secret();
		Account account = Account.without2Fa(form.username(), encoded, secret);
		this.accountService.insert(account);

		// automatic login after signup
		AccountUserDetails userDetails = new AccountUserDetails(account);
		Authentication token = UsernamePasswordAuthenticationToken.authenticated(userDetails, null,
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(token);
		return "redirect:/";
	}

	record SignupForm(String username, String password) {
	}

}
