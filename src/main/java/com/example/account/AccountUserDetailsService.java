package com.example.account;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountUserDetailsService implements UserDetailsService, UserDetailsPasswordService {

	private final AccountService accountService;

	public AccountUserDetailsService(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Account account = this.accountService.findByUsername(username);
			return new AccountUserDetails(account);
		}
		catch (EmptyResultDataAccessException e) {
			throw new UsernameNotFoundException("user not found", e);
		}
	}

	@Override
	public UserDetails updatePassword(UserDetails user, String newPassword) {
		this.accountService.updatePassword(user.getUsername(), newPassword);
		final Account account = ((AccountUserDetails) user).getAccount();
		return new AccountUserDetails(account.withPassword(newPassword));
	}

}
