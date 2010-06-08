package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * 
 * @author Marcin Deryło
 */
@RemoteException
public class SurveyTemplateAlreadyFilledException extends BusinessException implements Serializable {

	public static final String ERROR_CODE = "SURVEY_TEMPLATE_ALREADY_FILLED";

	public SurveyTemplateAlreadyFilledException(@Param("surveyTemplateId") Long surveyTemplateId) {
		super(ERROR_CODE, "Survey template ID={0} has already been filled and " + "cannot be changed/removed",
				surveyTemplateId);
	}
}
