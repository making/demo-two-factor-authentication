package com.example.twofactorauth;

public interface TwoFactorAuthenticationCodeVerifier {

	boolean verify(String code, String secret);

}
