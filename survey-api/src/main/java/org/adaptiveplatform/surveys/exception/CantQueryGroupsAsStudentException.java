package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.RemoteException;

@RemoteException
public class CantQueryGroupsAsStudentException extends BusinessException implements Serializable {

    public static final String ERROR_CORE = "CANT_QUERY_GROUPS_AS_STUDENT";

    public CantQueryGroupsAsStudentException() {
        super(ERROR_CORE, "Doesn't have Student role to be allowed to read "
                + "groups in student mode.");
    }
}
