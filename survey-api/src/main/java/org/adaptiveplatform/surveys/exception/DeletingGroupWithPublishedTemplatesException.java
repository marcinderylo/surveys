package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class DeletingGroupWithPublishedTemplatesException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE =
            "DELETING_GROUP_WITH_PUBLISHED_TEMPLATES";

    public DeletingGroupWithPublishedTemplatesException(@Param("groupName") String groupName) {
        super(ERROR_CODE, "Group \"{0}\" has published templates and cannot be "
                + "removed", groupName);
    }
}
