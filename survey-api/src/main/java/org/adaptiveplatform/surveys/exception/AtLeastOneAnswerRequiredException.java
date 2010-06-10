package org.adaptiveplatform.surveys.exception;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;
import org.adaptiveplatform.surveys.dto.QuestionTypeEnum;

/**
 *
 * @author Marcin Deryło
 */
@RemoteException
public class AtLeastOneAnswerRequiredException extends BusinessException {

    public static final String ERROR_CODE = "AT_LEAST_ONE_ANSWER_REQUIRED";

    public AtLeastOneAnswerRequiredException(@Param("questionType") QuestionTypeEnum questionType) {
        super(ERROR_CODE, "Question of type {0} requires at least one answer but none created.",
                questionType);
    }
}
