package org.adaptiveplatform.surveys.exception.security;

import java.io.Serializable;

import org.adaptiveplatform.surveys.exception.BusinessException;

/**
 *
 * @author Marcin Deryło
 */
public class UserNotAuthenticatedException extends BusinessException implements Serializable {

    public static final String ERROR_CODE = "NOT_AUTHENTICATED";

    public UserNotAuthenticatedException() {
        super(ERROR_CODE, "User not authenticated");
    }
}
