package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

/**
 *
 * @author Marcin Deryło
 */
public class NoSuchAnswerException extends BusinessException implements
        Serializable {

    public static final String ERROR_CODE = "NO_SUCH_ANSWER";

    public NoSuchAnswerException(Integer answerNumber, Long id) {
        super(ERROR_CODE, "No answer with number {0} in answered question "
                + "ID={1}", answerNumber, id);
    }
}
