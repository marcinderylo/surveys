package org.adaptiveplatform.surveys.application;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.adaptiveplatform.adapt.commons.validation.constraints.NonBlank;
import org.adaptiveplatform.codegenerator.api.RemoteService;
import org.adaptiveplatform.surveys.dto.RegisterAccountCommand;
import org.adaptiveplatform.surveys.exception.security.EmailAddressAlreadyRegisteredException;

@RemoteService
public interface UserFacade {

	Long registerUser(@NotNull @Valid RegisterAccountCommand command) throws EmailAddressAlreadyRegisteredException;

	/**
	 * Replaces currenly assigned user's roles with a new set of permissions.
	 * 
	 * @param email
	 *            email of the user to assign/revoke permissions to
	 * @param grantedRoles
	 *            new roles to be assign to the user
	 */
	void setUserRoles(@NonBlank String email, @NotNull @Size(min = 1) Set<String> grantedRoles);
}
