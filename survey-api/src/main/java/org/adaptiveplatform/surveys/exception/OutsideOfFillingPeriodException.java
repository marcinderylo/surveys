package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 *
 * @author Marcin Deryło
 */
@RemoteException
public class OutsideOfFillingPeriodException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE = "OUTSIDE_OF_FILLING_PERIOD";

    public OutsideOfFillingPeriodException(@Param("filledSurveyId")Long filledSurveyId,
            @Param("publicationId")Long publicationId) {
        super(ERROR_CODE, "Filled survey ID={0} cannot be filled at the moment,"
                + " because survey publication ID={1} is outside it's filling "
                + "period", filledSurveyId, publicationId);
    }
}
