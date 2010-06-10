package org.adaptiveplatform.surveys.exception;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 *
 * @author Marcin Deryło
 */
@RemoteException
public class OneOfSelectedAnswersDisallowsOthersException extends BusinessException {

    public static final String ERROR_CODE = "ONE_OF_SELECTED_ANSWERS_DISALLOWS_OTHERS";

    public OneOfSelectedAnswersDisallowsOthersException(
            @Param("questionTemplateId") Long questionTemplateId) {
        super(ERROR_CODE, "One of answers selected in question (template ID = {0}) disallows other"
                + "answers.", questionTemplateId);
    }
}
