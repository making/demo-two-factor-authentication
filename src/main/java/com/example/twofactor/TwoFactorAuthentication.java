package com.example.twofactor;

import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;

public class TwoFactorAuthentication extends AbstractAuthenticationToken {

	private final Authentication primary;

	public TwoFactorAuthentication(Authentication primary) {
		super(List.of());
		this.primary = primary;
	}

	public Object getPrincipal() {
		return this.primary.getPrincipal();
	}

	@Override
	public Object getCredentials() {
		return this.primary.getCredentials();
	}

	@Override
	public void eraseCredentials() {
		if (this.primary instanceof CredentialsContainer) {
			((CredentialsContainer) this.primary).eraseCredentials();
		}
	}

	@Override
	public boolean isAuthenticated() {
		return false;
	}

	public Authentication getPrimary() {
		return this.primary;
	}

}
