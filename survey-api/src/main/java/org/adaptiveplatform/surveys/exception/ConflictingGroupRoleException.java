package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;
import org.adaptiveplatform.surveys.dto.GroupRoleEnum;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class ConflictingGroupRoleException extends BusinessException implements Serializable {

    public static final String ERROR_CODE = "CONFLICTING_GROUP_ROLE";

    public ConflictingGroupRoleException(@Param("email") String email,
            @Param("groupName") String groupName,
            @Param("groupRole") GroupRoleEnum groupRole) {
        super(ERROR_CODE, "User \"{0}\" already has a role in the \"{1}\" group that would conflict with new"
                + " role \"{2}\".", email, groupName, groupRole.name());
    }
}
