package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class FilledSurveyDoesNotExistException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE = "FILLED_SURVEY_DOES_NOT_EXIST";

    public FilledSurveyDoesNotExistException(Long id) {
        super(ERROR_CODE, "No filled survey with ID={0} exists.", id);
    }
}
