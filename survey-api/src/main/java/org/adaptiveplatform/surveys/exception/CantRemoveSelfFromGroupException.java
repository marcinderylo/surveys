package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class CantRemoveSelfFromGroupException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE = "CANT_REMOVE_SELF_FROM_GROUP";

    public CantRemoveSelfFromGroupException(@Param("groupName") String groupName) {
        super(ERROR_CODE, "Can't remove self from group \"{0}\"", groupName);
    }
}
