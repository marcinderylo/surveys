package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class DeletingGroupWithPublishedTemplatesException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE =
            "DELETING_GROUP_WITH_PUBLISHED_TEMPLATES";

    public DeletingGroupWithPublishedTemplatesException(String groupName) {
        super(ERROR_CODE, "Group \"{0}\" has published templates and cannot be "
                + "removed", groupName);
    }
}
