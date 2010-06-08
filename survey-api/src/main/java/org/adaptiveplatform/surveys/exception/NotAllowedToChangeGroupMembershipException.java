package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class NotAllowedToChangeGroupMembershipException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE =
            "NOT_ALLOWED_TO_CHANGE_GROUP_MEMBERS";

    public NotAllowedToChangeGroupMembershipException(@Param("groupName")String groupName) {
        super(ERROR_CODE, "Not an admin of group \"{0}\" to modify it's members",
                groupName);
    }
}
