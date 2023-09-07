package com.example.account;

public record Account(String username, String password, String twoFactorSecret, boolean twoFactorEnabled) {

	public Account withPassword(String password) {
		return new Account(this.username, password, this.twoFactorSecret, this.twoFactorEnabled);
	}

	public Account enable2Fa() {
		return new Account(this.username, this.password, twoFactorSecret, true);
	}

	public Account disable2Fa() {
		return new Account(this.username, this.password, this.twoFactorSecret, false);
	}
}
