package org.adaptiveplatform.surveys.exception;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 *
 * @author Marcin Deryło
 */
@RemoteException
public class MultipleAnswersForSingleChoiceQuestionException extends BusinessException {

    public static final String ERROR_CODE = "MULTIPLE_ANSWERS_FOR_SINGLE_CHOICE_QUESTION";

    public MultipleAnswersForSingleChoiceQuestionException(
            @Param("questionTemplateId") Long questionTemplateId) {
        super(ERROR_CODE, "More than one answer selected in single choice question (ID={0})",
                questionTemplateId);
    }
}
