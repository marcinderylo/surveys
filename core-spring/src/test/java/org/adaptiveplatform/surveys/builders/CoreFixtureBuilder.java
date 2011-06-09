package org.adaptiveplatform.surveys.builders;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.application.AuthenticationService;
import org.adaptiveplatform.surveys.application.UserFacade;
import org.adaptiveplatform.surveys.dto.RegisterAccountCommand;
import org.springframework.stereotype.Service;

@Service
public class CoreFixtureBuilder {

	private static final String ADMIN_EMAIL = "adaptserver@gmail.com";
	private static final String ADMIN_PASSWORD = "adapt2010";

	@Resource
	private UserFacade facade;

	@Resource
	private AuthenticationService authentication;

	public Long createUser(UserAccountBuilder user) {
		authentication.logout();
		RegisterAccountCommand command = user.build();
		Long userId = facade.registerUser(command);

		if (!user.getRoles().isEmpty()) {
			loginAsAdmin();
			facade.setUserRoles(command.getEmail(), user.getRoles());
		}
		return userId;
	}

	public void loginAsAdmin() {
		loginAs(ADMIN_EMAIL, ADMIN_PASSWORD);
	}

	public void loginAs(String email) {
		authentication.login(email, UserAccountBuilder.DEFAULT_PASSWORD);
	}

	public void loginAs(String email, String password) {
		authentication.login(email, password);
	}

	public void logout() {
		authentication.logout();
	}
}
