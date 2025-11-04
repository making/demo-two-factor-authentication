package com.example;

import com.example.twofactorauth.TwoFactorAuthenticationCodeVerifier;
import com.example.twofactorauth.TwoFactorAuthenticationSuccessHandler;
import com.example.twofactorauth.totp.TotpAuthenticationCodeVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import static com.example.twofactorauth.TwoFactorAuthorizationManager.twoFa;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http,
			AuthenticationSuccessHandler primarySuccessHandler) {
		return http
		// @formatter:off
			.authorizeHttpRequests(authorize -> authorize
					.requestMatchers("/signup", "/error").permitAll()
					.requestMatchers("/challenge/totp").hasAuthority(FactorGrantedAuthority.PASSWORD_AUTHORITY)
					.anyRequest().access(twoFa()))
		// @formatter:on
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

	@Bean
	public TwoFactorAuthenticationCodeVerifier twoFactorAuthenticationCodeVerifier() {
		return new TotpAuthenticationCodeVerifier();
	}

}
