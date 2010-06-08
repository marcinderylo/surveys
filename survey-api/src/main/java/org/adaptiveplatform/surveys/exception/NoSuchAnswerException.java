package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class NoSuchAnswerException extends BusinessException implements
        Serializable {

    public static final String ERROR_CODE = "NO_SUCH_ANSWER";

    public NoSuchAnswerException(@Param("answerNumber") Integer answerNumber, @Param("id") Long id) {
        super(ERROR_CODE, "No answer with number {0} in answered question "
                + "ID={1}", answerNumber, id);
    }
}
