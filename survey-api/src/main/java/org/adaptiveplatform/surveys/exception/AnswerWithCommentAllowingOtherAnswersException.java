package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class AnswerWithCommentAllowingOtherAnswersException extends BusinessException
        implements Serializable {

    public static final String ERROR_CODE =
            "ANSWER_WITH_COMMENT_MUST_DISALLOW_OTHER_ANSWERS";

    public AnswerWithCommentAllowingOtherAnswersException(String answerText,
            Long questionTemplateId) {
        super(ERROR_CODE, "Attempted to add to question template ID={1} answer"
                + " \"{0}\" that requires comment but allows other answers.",
                answerText, questionTemplateId);
    }
}
