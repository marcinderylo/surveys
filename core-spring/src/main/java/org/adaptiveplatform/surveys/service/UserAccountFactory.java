package org.adaptiveplatform.surveys.service;

import org.adaptiveplatform.surveys.exception.security.EmailAddressAlreadyRegisteredException;
import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.domain.UserPrivilege;
import org.springframework.stereotype.Service;

@Service("userAccountFactory")
public class UserAccountFactory {

    @Resource
    private UserAccountRepository repository;

    public UserAccount registerNewAccount(String name, String password, String email) {
        checkEmailUniqueness(email);
        UserAccount account = new UserAccount(password, email);
        account.setName(name);
        account.addPrivilege(UserPrivilege.USER);
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
