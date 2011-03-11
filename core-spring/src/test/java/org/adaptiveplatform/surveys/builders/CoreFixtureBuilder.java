package org.adaptiveplatform.surveys.builders;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.application.AuthenticationService;
import org.adaptiveplatform.surveys.application.UserFacade;
import org.adaptiveplatform.surveys.domain.UserAccountBuilder;
import org.springframework.stereotype.Service;

@Service
public class CoreFixtureBuilder {

	private static final String ADMIN_EMAIL = "adaptserver@gmail.com";
	private static final String ADMIN_PASSWORD = "adapt2010";

	@Resource
	private UserFacade facade;

	@Resource
	private AuthenticationService authentication;

	public void createUsers(UserAccountBuilder... builders) {
		authentication.logout();
		for (UserAccountBuilder builder : builders) {
			facade.registerUser(builder.buildRegisterCommand());

			if (!builder.getRoles().isEmpty()) {
				loginAsAdmin();
				facade.setUserRoles(builder.getEmail(), builder.getRoles());
			}
		}
	}

	public void loginAsAdmin() {
		authentication.login(ADMIN_EMAIL, ADMIN_PASSWORD);
	}
}
