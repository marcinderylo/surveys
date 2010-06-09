package org.adaptiveplatform.surveys.exception;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 *
 * @author Marcin Deryło
 */
@RemoteException
public class GroupAlreadyExistsException extends BusinessException {

    public static final String ERROR_CODE = "GROUP_ALREADY_EXISTS";

    public GroupAlreadyExistsException(@Param("groupName") String conflictingGroupName) {
        super(ERROR_CODE, "Group with name \"{0}\" already exists", conflictingGroupName);
    }
}
