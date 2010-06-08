package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;
import java.util.Collection;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class InvalidAnswerNumbersException extends BusinessException implements Serializable {

	public static final String ERROR_CODE = "INVALID_ANSWER_NUMBERS";

	public InvalidAnswerNumbersException(
			@Param(value = "invalidAnswers", collectionType = Integer.class) Collection<Integer> invalidAnswers,
			@Param("answeredQuestionId") Long answeredQuestionId) {
		super(ERROR_CODE, "Answered question ID={1} doesn't contain answers " + "with numbers {0}.", invalidAnswers,
				answeredQuestionId);
	}
}
