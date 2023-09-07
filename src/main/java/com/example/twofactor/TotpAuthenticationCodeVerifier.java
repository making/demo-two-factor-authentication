package com.example.twofactor;

import java.security.GeneralSecurityException;

import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TotpAuthenticationCodeVerifier implements TwoFactorAuthenticationCodeVerifier {

	@Override
	public boolean verify(String secret, String code) {
		try {
			return TimeBasedOneTimePasswordUtil.validateCurrentNumber(secret,
					StringUtils.hasText(code) ? Integer.parseInt(code) : 0, 10000);
		}
		catch (GeneralSecurityException e) {
			throw new IllegalStateException(e);
		}
	}

}
