package com.example.twofactorauth;

import com.example.account.Account;

public interface TwoFactorAuthenticationCodeVerifier {

	boolean verify(Account account, String code);

}
