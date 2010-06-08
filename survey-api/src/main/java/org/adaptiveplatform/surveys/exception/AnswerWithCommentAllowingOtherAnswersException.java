package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class AnswerWithCommentAllowingOtherAnswersException extends BusinessException implements Serializable {

	public static final String ERROR_CODE = "ANSWER_WITH_COMMENT_MUST_DISALLOW_OTHER_ANSWERS";

	public AnswerWithCommentAllowingOtherAnswersException(@Param("answerText") String answerText,
			@Param("questionTemplateId") Long questionTemplateId) {
		super(ERROR_CODE, "Attempted to add to question template ID={1} answer"
				+ " \"{0}\" that requires comment but allows other answers.", answerText, questionTemplateId);
	}
}
