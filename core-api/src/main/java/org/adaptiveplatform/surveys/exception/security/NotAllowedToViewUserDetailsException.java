package org.adaptiveplatform.surveys.exception.security;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.RemoteException;
import org.adaptiveplatform.surveys.exception.BusinessException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class NotAllowedToViewUserDetailsException extends BusinessException implements Serializable {

    public static final String ERROR_CODE = "NOT_ALLOWED_TO_VIEW_USER_DETAILS";

    public NotAllowedToViewUserDetailsException() {
        super(ERROR_CODE, "Need admin rights to view other users details");
    }
}
