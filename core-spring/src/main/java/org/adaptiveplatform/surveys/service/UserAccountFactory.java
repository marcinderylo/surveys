package org.adaptiveplatform.surveys.service;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.exception.security.EmailAddressAlreadyRegisteredException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userAccountFactory")
public class UserAccountFactory {

	@Resource
	private UserAccountRepository repository;
	@Resource
	private PasswordEncoder encoder;

	public UserAccount registerNewAccount(String name, String password, String email) {
		checkEmailUniqueness(email);
		final String encodedPassword = encoder.encodePassword(password, null);
		UserAccount account = new UserAccount(encodedPassword, email);
		account.setName(name);
		repository.persist(account);
		return account;
	}

	private void checkEmailUniqueness(String email) {
		final UserAccount existingUserWithEmail = repository.get(email);
		if (existingUserWithEmail != null) {
			throw new EmailAddressAlreadyRegisteredException(email);
		}
	}
}
