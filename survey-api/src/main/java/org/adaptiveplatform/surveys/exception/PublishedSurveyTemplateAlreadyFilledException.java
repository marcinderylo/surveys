package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class PublishedSurveyTemplateAlreadyFilledException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE =
            "PUBLISHED_SURVEY_TEMPLATE_ALREADY_FILLED";

    public PublishedSurveyTemplateAlreadyFilledException(@Param("publicationId")Long publicationId) {
        super(ERROR_CODE, "Published survey template with ID={0} has already "
                + "been filled", publicationId);
    }
}
