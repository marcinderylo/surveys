package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Marcin Deryło
 */
public class InvalidAnswerNumbersException extends BusinessException implements
        Serializable {

    public static final String ERROR_CODE = "INVALID_ANSWER_NUMBERS";

    public InvalidAnswerNumbersException(Collection<Integer> invalidAnswers,
            Long answeredQuestionId) {
        super(ERROR_CODE, "Answered question ID={1} doesn't contain answers "
                + "with numbers {0}.", invalidAnswers, answeredQuestionId);
    }
}
