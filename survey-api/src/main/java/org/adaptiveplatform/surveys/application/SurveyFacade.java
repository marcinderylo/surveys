package org.adaptiveplatform.surveys.application;

import java.util.List;

import org.adaptiveplatform.codegenerator.api.RemoteService;
import org.adaptiveplatform.surveys.dto.CreateSurveyTemplateCommand;


@RemoteService
public interface SurveyFacade {

	Long createTemplate(CreateSurveyTemplateCommand template);

	void updateTemplate(Long templateId, CreateSurveyTemplateCommand template);

	Long startFilling(Long publicationId);

	void submit(Long surveyId);

	void answerQuestion(Long surveyId, Integer questionNumber, List<Integer> answerNumbers, String comment);

	/**
	 * For evaluators. Deletes designated survey template, which must have been
	 * created by the caller. Additionally, only unpublished and never filled
	 * templates can be removed.
	 * 
	 * @param templateId
	 */
	void removeSurveyTemplate(Long templateId);
}
