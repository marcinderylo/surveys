package org.adaptiveplatform.surveys.application;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.adaptiveplatform.adapt.commons.validation.constraints.ValidId;
import org.adaptiveplatform.codegenerator.api.RemoteService;
import org.adaptiveplatform.surveys.dto.CreateSurveyTemplateCommand;

@RemoteService
public interface SurveyFacade {

	Long createTemplate(@NotNull @Valid CreateSurveyTemplateCommand template);

	void updateTemplate(@ValidId Long templateId, @NotNull @Valid CreateSurveyTemplateCommand template);

	Long startFilling(@ValidId Long publicationId);

	void submit(@ValidId Long surveyId);

	void answerQuestion(@ValidId Long surveyId, @NotNull @Min(1) Integer questionNumber, List<Integer> answerNumbers,
			String comment);

	/**
	 * For evaluators. Deletes designated survey template, which must have been
	 * created by the caller. Additionally, only unpublished and never filled
	 * templates can be removed.
	 * 
	 * @param templateId
	 */
	void removeSurveyTemplate(@ValidId Long templateId);
}
