package org.adaptiveplatform.surveys.exception;

import java.io.Serializable;

import org.adaptiveplatform.codegenerator.api.Param;
import org.adaptiveplatform.codegenerator.api.RemoteException;

/**
 * @author Marcin Deryło
 */
@RemoteException
public class SurveyTemplateAlreadyPublishedException extends BusinessException implements Serializable {

	public static final String ERROR_CODE = "SURVEY_TEMPLATE_ALREADY_PUBLISHED";

	public SurveyTemplateAlreadyPublishedException(@Param("surveyTemplateId") Long surveyTemplateId) {
		super(ERROR_CODE, "Survey template ID={0} has already been published "
				+ "in one or more groups and cannot be changed/removed", surveyTemplateId);
	}
}
