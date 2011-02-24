package org.adaptiveplatform.surveys.application;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.adaptiveplatform.surveys.dto.UserDto;

public interface AuthenticationService {

	void login(String username, String password);
	
	boolean isAuthenticated();

	UserDto getCurrentUser();

	void userSecurityCheck(@NotNull @Valid Long ownwerId);
}
