package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class SurveyTemplateAlreadyPublishedException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE = "SURVEY_TEMPLATE_ALREADY_PUBLISHED";

    public SurveyTemplateAlreadyPublishedException(
            Long surveyTemplateId) {
        super(ERROR_CODE, "Survey template ID={0} has already been published "
                + "in one or more groups and cannot be changed/removed",
                surveyTemplateId);
    }
}
