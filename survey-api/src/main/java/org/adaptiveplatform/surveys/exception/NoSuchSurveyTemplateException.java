package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class NoSuchSurveyTemplateException extends BusinessException implements Serializable {

    public static final String ERROR_CODE = "SURVEY_TEMPLATES_DOES_NOT_EXIST";

    public NoSuchSurveyTemplateException(Long id) {
        super(ERROR_CODE, "No survey template exists with ID={0}.", id);
    }
}
