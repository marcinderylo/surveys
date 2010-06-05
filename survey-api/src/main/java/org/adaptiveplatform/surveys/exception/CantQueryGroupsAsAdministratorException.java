package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class CantQueryGroupsAsAdministratorException extends BusinessException
        implements Serializable {

    public static final String ERROR_CORE = "CANT_QUERY_GROUPS_AS_ADMINISTRATOR";

    public CantQueryGroupsAsAdministratorException() {
        super(ERROR_CORE, "Doesn't have Teacher role to be allowed to read "
                + "groups in administrator mode.");
    }
}
