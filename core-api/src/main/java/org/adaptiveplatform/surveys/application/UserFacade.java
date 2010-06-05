package org.adaptiveplatform.surveys.application;

import java.util.Set;

import org.adaptiveplatform.codegenerator.api.RemoteService;
import org.adaptiveplatform.surveys.dto.RegisterAccountCommand;

@RemoteService
public interface UserFacade {

	Long registerUser(RegisterAccountCommand command);
        /**
         * Replaces currenly assigned user's roles with a new set of permissions.
         * @param email email of the user to assign/revoke permissions to
         * @param grantedRoles new roles to be assign to the user
         */
        void setUserRoles(String email, Set<String> grantedRoles);
}
