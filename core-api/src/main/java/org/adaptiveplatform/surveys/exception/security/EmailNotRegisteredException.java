package org.adaptiveplatform.surveys.exception.security;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;
import org.adaptiveplatform.surveys.exception.BusinessException;

/**
 * @author Rafal Jamroz
 */
@RemoteException
public class EmailNotRegisteredException extends BusinessException {

	private static final String ERROR_CODE = "NO_USER_REGISTERED_EMAIL";

	public EmailNotRegisteredException(@Param("email") String email) {
		super(ERROR_CODE, "No user exists with email: \"{0}\".", email);
	}
}
