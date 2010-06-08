package org.adaptiveplatform.surveys.exception.security;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;
import org.adaptiveplatform.surveys.exception.BusinessException;

/**
 * 
 * @author Marcin Deryło
 */
@RemoteException
public class NoSuchUserException extends BusinessException implements Serializable {

	public static final String ERROR_CODE = "NO_SUCH_USER";

	public NoSuchUserException(@Param("id") Long userId) {
		super(ERROR_CODE, "No user exists with ID={0}.", userId);
	}
}
