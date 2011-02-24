package org.adaptiveplatform.surveys.application;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.domain.UserPrivilege;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.security.NoAuthorizationForOtherUserResourceException;
import org.adaptiveplatform.surveys.exception.security.UserNotAuthenticatedException;
import org.adaptiveplatform.surveys.service.UserAccountRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("defaultAuthenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {

	@Resource
	private UserAccountRepository userRepository;

	@Resource
	private AuthenticationManager authenticationManager;
	
	@Override
	public void login(String username, String password) {
		UsernamePasswordAuthenticationToken request = new UsernamePasswordAuthenticationToken(username, password);
		Authentication authentication = authenticationManager.authenticate(request);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

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
			return userToDto(userRepository.get(email));
		}
		return null;
	}

	private UserDto userToDto(UserAccount user) {
		UserDto dto = new UserDto();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setName(user.getName());
		List<String> roles = new ArrayList<String>();
		for (UserPrivilege privilege : user.getPrivileges()) {
			roles.add(privilege.role);
		}
		dto.setRoles(roles);
		return dto;
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
