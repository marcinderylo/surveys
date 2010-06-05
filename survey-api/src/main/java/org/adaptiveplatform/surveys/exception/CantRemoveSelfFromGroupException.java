package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class CantRemoveSelfFromGroupException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE = "CANT_REMOVE_SELF_FROM_GROUP";

    public CantRemoveSelfFromGroupException(String groupName) {
        super(ERROR_CODE, "Can't remove self from group \"{0}\"", groupName);
    }
}
