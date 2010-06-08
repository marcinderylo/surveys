package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class CantQueryGroupsAsEvaluatorException extends BusinessException
        implements Serializable {

    public static final String ERROR_CORE = "CANT_QUERY_GROUPS_AS_EVALUATOR";

    public CantQueryGroupsAsEvaluatorException() {
        super(ERROR_CORE, "Doesn't have Evaluator role to be allowed to read "
                + "groups in evaluator mode.");
    }
}
