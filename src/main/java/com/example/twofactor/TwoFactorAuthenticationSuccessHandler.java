package com.example.twofactor;

import java.io.IOException;

import com.example.account.Account;
import com.example.account.AccountUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class TwoFactorAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final AuthenticationSuccessHandler primarySuccessHandler;

	private final AuthenticationSuccessHandler secondarySuccessHandler;

	public TwoFactorAuthenticationSuccessHandler(String secondLoginUrl,
			AuthenticationSuccessHandler primarySuccessHandler) {
		this.primarySuccessHandler = primarySuccessHandler;
		this.secondarySuccessHandler = new SimpleUrlAuthenticationSuccessHandler(secondLoginUrl);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		Account account = accountUserDetails.getAccount();
		if (account.twoFactorEnabled()) {
			SecurityContextHolder.getContext().setAuthentication(new TwoFactorAuthentication(authentication));
			this.secondarySuccessHandler.onAuthenticationSuccess(request, response, authentication);
		}
		else {
			this.primarySuccessHandler.onAuthenticationSuccess(request, response, authentication);
		}
	}

}
