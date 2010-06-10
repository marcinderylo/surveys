package org.adaptiveplatform.surveys.exception;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 *
 * @author Marcin Deryło
 */
@RemoteException
public class SingleChoiceQuestionAnswersMustDisallowEachOtherException extends BusinessException {

    public static final String ERROR_CODE =
            "SINGLE_CHOICE_QUESTION_ANSWERS_MUST_DISALLOW_EACH_OTHER";

    public SingleChoiceQuestionAnswersMustDisallowEachOtherException(
            @Param("questionText") String questionText,
            @Param("questionNumber") Integer answerNumber) {
        super(ERROR_CODE, "Answer no.{1} in question \"{0}\" allows other answers, but it is a single"
                + "choice question", questionText, answerNumber);
    }
}
