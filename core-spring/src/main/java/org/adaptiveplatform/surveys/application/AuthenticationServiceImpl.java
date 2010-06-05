package org.adaptiveplatform.surveys.application;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.security.NoAuthorizationForOtherUserResourceException;
import org.adaptiveplatform.surveys.exception.security.UserNotAuthenticatedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service("defaultAuthenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {

	@Resource
	private UserDao userDao;

	@Override
	public boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && authentication.isAuthenticated();
	}

	@Override
	public UserDto getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			String email = authentication.getName();
			return userDao.getByEmail(email);
		}
		return null;
	}

	@Override
	public void userSecurityCheck(Long ownwerId) {
		if (!isAuthenticated()) {
			throw new UserNotAuthenticatedException();
		}
		if (!getCurrentUser().getId().equals(ownwerId)) {
			throw new NoAuthorizationForOtherUserResourceException();
		}
		// OK - do nothing
	}
}
