package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class SurveyTemplateAlreadyExistsException extends BusinessException implements Serializable {

	public static final String ERROR_CODE = "SURVEY_TEMPLATE_ALREADY_EXISTS";

	public SurveyTemplateAlreadyExistsException(@Param("templateName") String templateName) {
		super(ERROR_CODE, "Survey template named \"{0}\" already exists.", templateName);
	}
}
