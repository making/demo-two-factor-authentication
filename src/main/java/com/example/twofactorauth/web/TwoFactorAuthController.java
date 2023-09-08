package com.example.twofactorauth.web;

import java.io.IOException;

import com.example.account.Account;
import com.example.account.AccountService;
import com.example.account.AccountUserDetails;
import com.example.twofactorauth.TwoFactorAuthentication;
import com.example.twofactorauth.TwoFactorAuthenticationCodeVerifier;
import com.example.twofactorauth.totp.QrCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TwoFactorAuthController {

	private final AccountService accountService;

	private final TwoFactorAuthenticationCodeVerifier codeVerifier;

	private final QrCode qrCode;

	private final AuthenticationSuccessHandler successHandler;

	private final AuthenticationFailureHandler failureHandler;

	public TwoFactorAuthController(AccountService accountService, TwoFactorAuthenticationCodeVerifier codeVerifier,
			QrCode qrCode, AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler) {
		this.accountService = accountService;
		this.codeVerifier = codeVerifier;
		this.qrCode = qrCode;
		this.successHandler = successHandler;
		this.failureHandler = failureHandler;
	}

	@GetMapping(path = "/enable-2fa")
	public String requestEnableTwoFactor(@AuthenticationPrincipal AccountUserDetails accountUserDetails, Model model) {
		Account account = accountUserDetails.getAccount();
		String otpAuthUrl = "otpauth://totp/%s?secret=%s&digits=6".formatted("Demo: " + account.username(),
				account.twoFactorSecret());
		model.addAttribute("qrCode", this.qrCode.dataUrl(otpAuthUrl));
		model.addAttribute("secret", account.twoFactorSecret());
		return "enable-2fa";
	}

	@PostMapping(path = "/enable-2fa")
	public String processEnableTwoFactor(@RequestParam String code,
			@AuthenticationPrincipal AccountUserDetails accountUserDetails, Model model) {
		Account account = accountUserDetails.getAccount();
		if (account.twoFactorEnabled()) {
			return "redirect:/";
		}
		if (!this.codeVerifier.verify(account.twoFactorSecret(), code)) {
			model.addAttribute("message", "Invalid code");
			return this.requestEnableTwoFactor(accountUserDetails, model);
		}
		this.accountService.enable2Fa(account.username());
		Authentication token = UsernamePasswordAuthenticationToken
			.authenticated(new AccountUserDetails(account.enable2Fa()), null, accountUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(token);
		return "redirect:/";
	}

	@GetMapping(path = "/challenge/totp")
	public String requestTotp() {
		return "totp";
	}

	@PostMapping(path = "/challenge/totp")
	public void processTotp(@RequestParam String code, TwoFactorAuthentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Authentication primaryAuthentication = authentication.getPrimary();
		AccountUserDetails accountUserDetails = (AccountUserDetails) primaryAuthentication.getPrincipal();
		Account account = accountUserDetails.getAccount();
		if (this.codeVerifier.verify(account.twoFactorSecret(), code)) {
			SecurityContextHolder.getContext().setAuthentication(primaryAuthentication);
			this.successHandler.onAuthenticationSuccess(request, response, primaryAuthentication);
		}
		else {
			this.failureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("Invalid code"));
		}
	}

}
