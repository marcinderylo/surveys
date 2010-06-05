package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.surveys.dto.GroupRoleEnum;

/**
 *
 * @author Marcin Deryło
 */
public class NotEligibleForGroupRoleException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE = "NOT_ELIGIBLE_FOR_GROUP_ROLE";

    public NotEligibleForGroupRoleException(String userName, GroupRoleEnum role) {
        super(ERROR_CODE, "User \"{0}\" does not have sufficient privileges to "
                + "be assigned group role \"{1}\"", userName, role.name());
    }
}
