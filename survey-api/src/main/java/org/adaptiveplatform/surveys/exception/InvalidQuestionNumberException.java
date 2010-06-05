package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class InvalidQuestionNumberException extends BusinessException implements
        Serializable {

    public static final String ERROR_CODE = "INVALID_QUESTION_NUMBER";

    public InvalidQuestionNumberException(int questionNumber,
            Long filledSurveyId) {
        super(ERROR_CODE, "Question number {0} doesn't belong to filled survey "
                + "ID={1}", questionNumber, filledSurveyId);
    }
}
