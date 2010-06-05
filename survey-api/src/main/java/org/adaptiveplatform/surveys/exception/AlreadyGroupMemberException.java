package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.surveys.dto.GroupRoleEnum;

/**
 *
 * @author Marcin Deryło
 */
public class AlreadyGroupMemberException extends BusinessException implements
        Serializable {

    public static final String ERROR_CODE = "ALREADY_A_GROUP_MEMBER";

    public AlreadyGroupMemberException(String email, String groupName,
            GroupRoleEnum groupRole) {
        super(ERROR_CODE, "User \"{0}\" is already a member of group \"{1}\" "
                + "with role \"{2}\".", email, groupName, groupRole.name());
    }
}
