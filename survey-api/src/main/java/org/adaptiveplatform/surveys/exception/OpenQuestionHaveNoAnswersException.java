package org.adaptiveplatform.surveys.exception;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 *
 * @author Marcin Deryło
 */
@RemoteException
public class OpenQuestionHaveNoAnswersException extends BusinessException {

    public static final String ERROR_CODE = "OPEN_QUESTION_CANT_HAVE_ANSWERS";

    public OpenQuestionHaveNoAnswersException(@Param("questionText") String text) {
        super(ERROR_CODE, "Question \"{0}\" is an open question and have no answers", text);
    }
}
