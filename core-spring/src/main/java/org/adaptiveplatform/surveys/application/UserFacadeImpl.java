package org.adaptiveplatform.surveys.application;

import java.util.Set;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.domain.UserPrivilege;
import org.adaptiveplatform.surveys.dto.RegisterAccountCommand;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.security.CantRevokeOwnAdminRights;
import org.adaptiveplatform.surveys.exception.security.NotAllowedToRegisterUserException;
import org.adaptiveplatform.surveys.service.UserAccountFactory;
import org.adaptiveplatform.surveys.service.UserAccountRepository;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.security.access.annotation.Secured;
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

    @Override
    public Long registerUser(RegisterAccountCommand command) {
        ensureCreatorIsAnonymousOrAdmin();
        UserAccount user = accountFactory.registerNewAccount(command.getName(),
                command.getPassword(), command.getEmail());
        return user.getId();
    }

    @Secured({Role.ADMINISTRATOR})
    @Override
    public void setUserRoles(String email, Set<String> grantedRoles) {
        verifyDoesNotRevokeOwnAdminRights(email, grantedRoles);

        final UserAccount user = accountRepository.getExisting(email);
        user.getPrivileges().clear();
        for (String role : grantedRoles) {
            final UserPrivilege privilege = UserPrivilege.getByRole(
                    role);
            user.getPrivileges().add(privilege);
        }
    }

    private void verifyDoesNotRevokeOwnAdminRights(String email,
            Set<String> grantedRoles) {
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
}
