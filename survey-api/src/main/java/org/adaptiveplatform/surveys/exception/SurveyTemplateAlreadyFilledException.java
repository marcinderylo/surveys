package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class SurveyTemplateAlreadyFilledException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE = "SURVEY_TEMPLATE_ALREADY_FILLED";

    public SurveyTemplateAlreadyFilledException(Long surveyTemplateId) {
        super(ERROR_CODE, "Survey template ID={0} has already been filled and "
                + "cannot be changed/removed", surveyTemplateId);
    }
}
