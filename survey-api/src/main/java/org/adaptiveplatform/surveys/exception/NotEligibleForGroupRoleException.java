package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;
import org.adaptiveplatform.surveys.dto.GroupRoleEnum;

/**
 *
 * @author Marcin Deryło
 */
@RemoteException
public class NotEligibleForGroupRoleException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE = "NOT_ELIGIBLE_FOR_GROUP_ROLE";

    public NotEligibleForGroupRoleException(@Param("userName")String userName, @Param("role")GroupRoleEnum role) {
        super(ERROR_CODE, "User \"{0}\" does not have sufficient privileges to "
                + "be assigned group role \"{1}\"", userName, role.name());
    }
}
