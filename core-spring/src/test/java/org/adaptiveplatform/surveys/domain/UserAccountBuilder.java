package org.adaptiveplatform.surveys.domain;

import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;

public class UserAccountBuilder {

	private final String username = "test username";
	private String password = "test password";
        private String email;
	private List<String> roles = new ArrayList<String>();

	public UserAccountBuilder withPassword(String password) {
		this.password = password;
		return this;
	}

	public UserAccountBuilder withEmail(String email) {
		this.email = email;
		return this;
	}

	public UserAccountBuilder inRoles(String... roles) {
		this.roles.addAll(Arrays.asList(roles));
		return this;
	}

	public UserAccount build() {
		UserAccount account = new UserAccount(password, email);
                account.setName(username);
		for (String role : roles) {
			account.addRole(role);
		}

		return account;
	}

	public static UserAccountBuilder user() {
		return new UserAccountBuilder();
	}

	public static UserAccountBuilder user(String username, String password, String email, String... roles) {
		return new UserAccountBuilder().withPassword(password).withEmail(email).inRoles(roles);
	}
}
