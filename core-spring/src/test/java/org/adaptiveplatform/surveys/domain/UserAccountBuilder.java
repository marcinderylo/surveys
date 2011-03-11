package org.adaptiveplatform.surveys.domain;

import java.util.Arrays;
import java.util.Set;

import org.adaptiveplatform.surveys.dto.RegisterAccountCommand;

import com.google.common.collect.Sets;

public class UserAccountBuilder {

	private final String username = "test username";
	private String password = "test password";
	private String email;
	private Set<String> roles = Sets.newHashSet();

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

	public RegisterAccountCommand buildRegisterCommand() {
		RegisterAccountCommand command = new RegisterAccountCommand();
		command.setEmail(email);
		command.setName(username);
		command.setPassword(password);
		return command;
	}

	public String getEmail() {
		return email;
	}

	public Set<String> getRoles() {
		return roles;
	}
}
