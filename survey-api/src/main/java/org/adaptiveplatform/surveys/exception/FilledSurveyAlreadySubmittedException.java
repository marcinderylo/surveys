package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class FilledSurveyAlreadySubmittedException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE = "FILLED_SURVEY_ALREADY_SUBMITTED";

    public FilledSurveyAlreadySubmittedException(Long id) {
        super(ERROR_CODE, "Filled survey ID={0} has already been submitted and "
                + "can be changed no more.", id);
    }
}
