package com.example.twofactor;

public interface TwoFactorAuthenticationCodeVerifier {

	boolean verify(String code, String secret);

}
