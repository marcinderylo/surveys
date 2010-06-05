package org.adaptiveplatform.surveys.exception.security;

import java.io.Serializable;

import org.adaptiveplatform.surveys.exception.BusinessException;

/**
 *
 * @author Marcin Deryło
 */
public class NoSuchUserException extends BusinessException implements Serializable {

    public static final String ERROR_CODE = "NO_SUCH_USER";

    public NoSuchUserException(Long userId) {
        super(ERROR_CODE, "No user exists with ID={0}.", userId);
    }

    public NoSuchUserException(String email) {
        super(ERROR_CODE, "No user exists with email: \"{0}\".", email);
    }
}
