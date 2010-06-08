package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class FilledSurveyAlreadySubmittedException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE = "FILLED_SURVEY_ALREADY_SUBMITTED";

    public FilledSurveyAlreadySubmittedException(@Param("id") Long id) {
        super(ERROR_CODE, "Filled survey ID={0} has already been submitted and "
                + "can be changed no more.", id);
    }
}
