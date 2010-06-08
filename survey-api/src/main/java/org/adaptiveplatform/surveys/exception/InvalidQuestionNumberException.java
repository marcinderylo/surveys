package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class InvalidQuestionNumberException extends BusinessException implements Serializable {

	public static final String ERROR_CODE = "INVALID_QUESTION_NUMBER";

	public InvalidQuestionNumberException(@Param("questionNumber") Integer questionNumber,
			@Param("filledSurveyId") Long filledSurveyId) {
		super(ERROR_CODE, "Question number {0} doesn't belong to filled survey " + "ID={1}", questionNumber,
				filledSurveyId);
	}
}
