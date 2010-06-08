package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class NotAllowedToPublishTemplatesInGroupException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE =
            "NOT_ALLOWED_TO_PUBLISH_TEMPLATES_IN_GROUP";

    public NotAllowedToPublishTemplatesInGroupException(@Param("groupName")String groupName) {
        super(ERROR_CODE, "Can't assign template to group - not a teacher in "
                + " group :\"{0}\"", groupName);
    }
}
