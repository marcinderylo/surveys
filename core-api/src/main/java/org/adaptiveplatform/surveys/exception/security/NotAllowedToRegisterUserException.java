package org.adaptiveplatform.surveys.exception.security;

import java.io.Serializable;
import org.adaptiveplatform.codegenerator.api.RemoteException;
import org.adaptiveplatform.surveys.exception.BusinessException;

@RemoteException
public class NotAllowedToRegisterUserException extends BusinessException implements Serializable {

    public static final String ERROR_CODE = "NOT_ALLOWED_TO_REGISTER_USER";

    public NotAllowedToRegisterUserException() {
        super(ERROR_CODE, "Not allowed to register users. Must be either anonymous or admin.");
    }
}
