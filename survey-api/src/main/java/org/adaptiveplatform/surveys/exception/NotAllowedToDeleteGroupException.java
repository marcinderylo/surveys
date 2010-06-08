package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class NotAllowedToDeleteGroupException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE = "NOT_ALLOWED_TO_DELETE_GROUP";

    public NotAllowedToDeleteGroupException(@Param("groupName")String groupName) {
        super(ERROR_CODE, "Must be admin in group \"{0}\" to delete it",
                groupName);
    }
}
