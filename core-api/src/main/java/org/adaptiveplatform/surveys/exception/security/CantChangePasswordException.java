package org.adaptiveplatform.surveys.exception.security;

import java.io.Serializable;

import org.adaptiveplatform.surveys.exception.BusinessException;

public class CantChangePasswordException extends BusinessException implements Serializable {

    public static final String ERROR_CODE = "CANT_CHANGE_ANOTHER_USER_PASSWORD";

    public CantChangePasswordException() {
        super(ERROR_CODE, "Can't change password of another user without admin rights");
    }
}
