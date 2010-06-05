package org.adaptiveplatform.surveys.application;

import org.adaptiveplatform.surveys.dto.UserDto;

public interface AuthenticationService {

	boolean isAuthenticated();

	UserDto getCurrentUser();

	void userSecurityCheck(Long ownwerId);
}
