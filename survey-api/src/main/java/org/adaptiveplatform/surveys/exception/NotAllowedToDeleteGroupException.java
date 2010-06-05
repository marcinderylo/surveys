package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class NotAllowedToDeleteGroupException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE = "NOT_ALLOWED_TO_DELETE_GROUP";

    public NotAllowedToDeleteGroupException(String groupName) {
        super(ERROR_CODE, "Must be admin in group \"{0}\" to delete it",
                groupName);
    }
}
