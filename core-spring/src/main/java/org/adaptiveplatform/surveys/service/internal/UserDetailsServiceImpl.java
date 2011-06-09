package org.adaptiveplatform.surveys.service.internal;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.service.UserAccountRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("defaultUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Resource
	private UserAccountRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		UserAccount user = userRepository.get(username);
		if (user == null) {
			throw new UsernameNotFoundException("UserAccount for name \"" + username + "\" not found.");
		}
		return createUser(user);
	}

	private User createUser(UserAccount user) {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (String role : user.getRoles()) {
			authorities.add(new GrantedAuthorityImpl(role));
		}
		return new User(user.getEmail(), user.getPassword(), true, true, true, true, authorities);
	}

}
