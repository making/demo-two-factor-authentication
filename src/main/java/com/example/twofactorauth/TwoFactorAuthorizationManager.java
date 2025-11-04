package com.example.twofactorauth;

import com.example.account.Account;
import com.example.account.AccountUserDetails;
import com.example.totp.TotpFactor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AllAuthoritiesAuthorizationManager;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.FactorGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class TwoFactorAuthorizationManager implements AuthorizationManager<Object> {

	private final AuthorizationManager<Object> mfa = AllAuthoritiesAuthorizationManager
		.hasAllAuthorities(FactorGrantedAuthority.PASSWORD_AUTHORITY, TotpFactor.TOTP_AUTHORITY);

	private final AuthorizationManager<Object> password = AuthorityAuthorizationManager
		.hasAuthority(FactorGrantedAuthority.PASSWORD_AUTHORITY);

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public static TwoFactorAuthorizationManager twoFa() {
		return new TwoFactorAuthorizationManager();
	}

	private static UsernamePasswordAuthenticationToken mfaToken(UserDetails userDetails, String... factors) {
		List<GrantedAuthority> authorities = new ArrayList<>(userDetails.getAuthorities());
		authorities.addAll(Arrays.stream(factors).map(FactorGrantedAuthority::fromAuthority).toList());
		return UsernamePasswordAuthenticationToken.authenticated(userDetails, null, List.copyOf(authorities));
	}

	public static UsernamePasswordAuthenticationToken twoFaToken(UserDetails userDetails) {
		return mfaToken(userDetails, TotpFactor.TOTP_AUTHORITY, FactorGrantedAuthority.PASSWORD_AUTHORITY);
	}

	public static UsernamePasswordAuthenticationToken passwordToken(UserDetails userDetails) {
		return mfaToken(userDetails, FactorGrantedAuthority.PASSWORD_AUTHORITY);
	}

	@Override
	public @Nullable AuthorizationResult authorize(Supplier<? extends @Nullable Authentication> authentication,
			Object context) {
		log.info("Authorizing {}", authentication.get());
		if (authentication.get() instanceof UsernamePasswordAuthenticationToken upat) {
			if (upat.getPrincipal() instanceof AccountUserDetails accountUserDetails) {
				Account account = accountUserDetails.getAccount();
				if (account.twoFactorEnabled()) {
					return this.mfa.authorize(authentication, context);
				}
				else {
					return password.authorize(authentication, context);
				}
			}
		}
		log.warn("Authentication is not of expected type");
		return new AuthorizationDecision(false);
	}

}
