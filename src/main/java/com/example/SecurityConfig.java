package com.example;

import com.example.twofactor.TwoFactorAuthenticationSuccessHandler;
import com.example.twofactor.TwoFactorAuthorizationManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
			AuthenticationSuccessHandler primarySuccessHandler) throws Exception {
		return http
			.authorizeHttpRequests(authorize -> authorize.requestMatchers("/signup", "/error")
				.permitAll()
				.requestMatchers("/challenge/totp")
				.access(new TwoFactorAuthorizationManager())
				.anyRequest()
				.authenticated())
			.formLogin(form -> form
				.successHandler(new TwoFactorAuthenticationSuccessHandler("/challenge/totp", primarySuccessHandler)))
			.securityContext(securityContext -> securityContext.requireExplicitSave(false))
			.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public AuthenticationSuccessHandler primarySuccessHandler() {
		return new SavedRequestAwareAuthenticationSuccessHandler();
	}

	@Bean
	public AuthenticationFailureHandler primaryFailureHandler() {
		return new SimpleUrlAuthenticationFailureHandler("/login?error");
	}

}
