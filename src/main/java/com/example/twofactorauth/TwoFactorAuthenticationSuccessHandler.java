package com.example.twofactorauth;

import com.example.account.Account;
import com.example.account.AccountUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class TwoFactorAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final AuthenticationSuccessHandler primarySuccessHandler;

	private final AuthenticationSuccessHandler secondarySuccessHandler;

	public TwoFactorAuthenticationSuccessHandler(String secondAuthUrl,
			AuthenticationSuccessHandler primarySuccessHandler) {
		this.primarySuccessHandler = primarySuccessHandler;
		this.secondarySuccessHandler = new SimpleUrlAuthenticationSuccessHandler(secondAuthUrl);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		Account account = accountUserDetails.getAccount();
		if (account.twoFactorEnabled()) {
			this.secondarySuccessHandler.onAuthenticationSuccess(request, response, authentication);
		}
		else {
			this.primarySuccessHandler.onAuthenticationSuccess(request, response, authentication);
		}
	}

}
