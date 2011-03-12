package org.adaptiveplatform.surveys.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.adaptiveplatform.surveys.application.AuthenticationService;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class AuthenticationServiceMock implements AuthenticationService, UserDetailsService {

	private static final String PASSWORD = "s3cr3t";

	private UserDto currentUser;

	@Override
	public UserDto getCurrentUser() {
		return currentUser;
	}

	@Override
	public boolean isAuthenticated() {
		return currentUser != null;
	}

	@Override
	public void userSecurityCheck(Long ownwerId) {
		// do nothing
	}

	// FIXME load user from database
	public void authenticate(Long id, String email, String... userRoles) {
		currentUser = new UserDto();
		currentUser.setId(id);
		currentUser.setEmail(email);
		currentUser.setRoles(Arrays.asList(userRoles));

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, PASSWORD);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	public void logout() {
		currentUser = null;
		SecurityContextHolder.clearContext();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (String role : currentUser.getRoles()) {
			authorities.add(new GrantedAuthorityImpl(role));
		}
		return new User(username, PASSWORD, true, true, true, true, authorities);
	}

	@Override
	public void login(String username, String password) {
		throw new RuntimeException("to be implemented"); // TODO implement
	}

    @Override
    public void checkCredentials(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
