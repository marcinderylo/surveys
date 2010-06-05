package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class SurveyTemplateAlreadyExistsException extends BusinessException implements
        Serializable {

    public static final String ERROR_CODE = "SURVEY_TEMPLATE_ALREADY_EXISTS";

    public SurveyTemplateAlreadyExistsException(String templateName) {
        super(ERROR_CODE, "Survey template named \"{0}\" already exists.",
                templateName);
    }
}
