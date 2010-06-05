package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class PublishedSurveyTemplateAlreadyFilledException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE =
            "PUBLISHED_SURVEY_TEMPLATE_ALREADY_FILLED";

    public PublishedSurveyTemplateAlreadyFilledException(Long publicationId) {
        super(ERROR_CODE, "Published survey template with ID={0} has already "
                + "been filled", publicationId);
    }
}
