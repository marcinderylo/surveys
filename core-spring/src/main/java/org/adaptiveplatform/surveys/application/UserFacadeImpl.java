package org.adaptiveplatform.surveys.application;

import java.util.Set;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.dto.ChangePasswordCommand;
import org.adaptiveplatform.surveys.dto.RegisterAccountCommand;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.security.CantChangePasswordException;
import org.adaptiveplatform.surveys.exception.security.CantRevokeOwnAdminRights;
import org.adaptiveplatform.surveys.exception.security.NotAllowedToRegisterUserException;
import org.adaptiveplatform.surveys.service.UserAccountFactory;
import org.adaptiveplatform.surveys.service.UserAccountRepository;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RemotingDestination
@Service("userFacade")
@Transactional
public class UserFacadeImpl implements UserFacade {

    @Resource
    private UserAccountFactory accountFactory;
    @Resource
    private UserAccountRepository accountRepository;
    @Resource
    private AuthenticationService authentication;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Long registerUser(RegisterAccountCommand command) {
        ensureCreatorIsAnonymousOrAdmin();
        UserAccount user = accountFactory.registerNewAccount(command.getName(), command.getPassword(),
                command.getEmail());
        return user.getId();
    }

    @Secured({ Role.ADMINISTRATOR })
    @Override
    public void setUserRoles(String email, Set<String> grantedRoles) {
        verifyDoesNotRevokeOwnAdminRights(email, grantedRoles);
        final UserAccount user = accountRepository.getExisting(email);
        user.setRoles(grantedRoles);
    }

    private boolean sameUser(UserAccount affectedUser, UserDto currentUser) {
        return affectedUser.getEmail().equals(currentUser.getEmail());
    }

    private void verifyDoesNotRevokeOwnAdminRights(String email, Set<String> grantedRoles) {
        UserDto caller = authentication.getCurrentUser();
        if (caller.getEmail().equals(email)) {
            if (!grantedRoles.contains(Role.ADMINISTRATOR)) {
                throw new CantRevokeOwnAdminRights();
            }
        }
    }

    private void ensureCreatorIsAnonymousOrAdmin() {
        if (authentication.isAuthenticated() && !isAdmin(authentication.getCurrentUser())) {
            throw new NotAllowedToRegisterUserException();
        }
    }

    private boolean isAdmin(UserDto currentUser) {
        return currentUser.getRoles().contains(Role.ADMINISTRATOR);
    }

    @Override
    @Secured(Role.USER)
    public void changePassword(ChangePasswordCommand command) {
        final UserAccount affectedUser = accountRepository.getExisting(command.getEmail());
        ensureCallerCanChangePasswordOf(affectedUser);
        checkOldPasswordIfRequired(affectedUser, command.getOldPassword());
        String encodedPassword = passwordEncoder.encodePassword(command.getNewPassword(), null);
        affectedUser.setPassword(encodedPassword);
    }

    private void ensureCallerCanChangePasswordOf(UserAccount affectedUser) {
        if (!sameUser(affectedUser, authentication.getCurrentUser())) {
            if (!isAdmin(authentication.getCurrentUser())) {
                throw new CantChangePasswordException();
            }
        }
    }

    private void checkOldPasswordIfRequired(UserAccount user, String oldPassword) {
        if (sameUser(user, authentication.getCurrentUser())) {
            authentication.checkCredentials(user.getEmail(), oldPassword);
        }
    }
}
