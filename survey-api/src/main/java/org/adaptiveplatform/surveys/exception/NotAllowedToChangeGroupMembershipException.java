package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class NotAllowedToChangeGroupMembershipException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE =
            "NOT_ALLOWED_TO_CHANGE_GROUP_MEMBERS";

    public NotAllowedToChangeGroupMembershipException(String groupName) {
        super(ERROR_CODE, "Not an admin of group \"{0}\" to modify it's members",
                groupName);
    }
}
