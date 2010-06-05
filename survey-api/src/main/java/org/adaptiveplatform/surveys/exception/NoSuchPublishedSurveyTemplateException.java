package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class NoSuchPublishedSurveyTemplateException extends BusinessException
        implements
        Serializable {

    public static final String ERROR_CODE = "NO_SUCH_PUBLISHED_SURVEY_TEMPLATE";

    public NoSuchPublishedSurveyTemplateException(Long id) {
        super(ERROR_CODE, "No published survey template with ID={0} exists.", id);
    }

    public NoSuchPublishedSurveyTemplateException(Long templateId, Long groupId) {
        super(ERROR_CODE,
                "No publication for survey template ID={0} and group ID={1} exists.",
                templateId, groupId);

    }
}
