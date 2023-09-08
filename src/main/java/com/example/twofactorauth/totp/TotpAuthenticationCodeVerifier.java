package com.example.twofactorauth.totp;

import java.security.GeneralSecurityException;

import com.example.account.Account;
import com.example.twofactorauth.TwoFactorAuthenticationCodeVerifier;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;

import org.springframework.util.StringUtils;

public class TotpAuthenticationCodeVerifier implements TwoFactorAuthenticationCodeVerifier {

	@Override
	public boolean verify(Account account, String code) {
		try {
			return TimeBasedOneTimePasswordUtil.validateCurrentNumber(account.twoFactorSecret(),
					StringUtils.hasText(code) ? Integer.parseInt(code) : 0, 10000);
		}
		catch (GeneralSecurityException e) {
			throw new IllegalStateException(e);
		}
	}

}
