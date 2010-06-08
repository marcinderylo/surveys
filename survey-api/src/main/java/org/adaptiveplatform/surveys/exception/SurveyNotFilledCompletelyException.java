package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 *
 * @author Marcin Deryło
 */
@RemoteException
public class SurveyNotFilledCompletelyException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE = "SURVEY_NOT_FILLED_COMPLETELY";

    public SurveyNotFilledCompletelyException(@Param("filledSurveyId")Long filledSurveyId) {
        super(ERROR_CODE, "Filled survey ID={0} cannot be submitted until it's "
                + "filled completely", filledSurveyId);
    }
}
