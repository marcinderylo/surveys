package org.adaptiveplatform.surveys.exception.security;

import org.adaptiveplatform.surveys.exception.BusinessException;

/**
 *
 * @author Marcin Deryło
 */
public class NoAuthorizationForOtherUserResourceException extends BusinessException {

    public static final String ERROR_CODE =
            "NO_AUTHORIZATION_FOR_OTHER_USER_RESOURCE";

    public NoAuthorizationForOtherUserResourceException() {
        super(ERROR_CODE, "Not allowed to access other user's resource");
    }
}
