package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class FilledSurveyDoesNotExistException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE = "FILLED_SURVEY_DOES_NOT_EXIST";

    public FilledSurveyDoesNotExistException(@Param("id") Long id) {
        super(ERROR_CODE, "No filled survey with ID={0} exists.", id);
    }
}
