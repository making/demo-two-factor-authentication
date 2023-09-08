package com.example.account;

import java.io.Serializable;

public record Account(String username, String password, String twoFactorSecret,
		boolean twoFactorEnabled) implements Serializable {

	public static Account without2Fa(String username, String password, String twoFactorSecret) {
		return new Account(username, password, twoFactorSecret, false);
	}

	public Account withPassword(String password) {
		return new Account(this.username, password, this.twoFactorSecret, this.twoFactorEnabled);
	}

	public Account enable2Fa() {
		return new Account(this.username, this.password, twoFactorSecret, true);
	}

}
