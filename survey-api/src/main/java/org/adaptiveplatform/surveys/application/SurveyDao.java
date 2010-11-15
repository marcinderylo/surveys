package org.adaptiveplatform.surveys.application;

import java.util.List;

import org.adaptiveplatform.adapt.commons.validation.constraints.ValidId;
import org.adaptiveplatform.codegenerator.api.RemoteService;
import org.adaptiveplatform.surveys.dto.FilledSurveyDto;
import org.adaptiveplatform.surveys.dto.FilledSurveyQuery;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateQuery;
import org.adaptiveplatform.surveys.dto.SurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.SurveyTemplateQuery;

@RemoteService
public interface SurveyDao {

	/**
	 * For students & evaluators.
	 * 
	 * @return details of given filled survey
	 */
	FilledSurveyDto getSurvey(@ValidId Long surveyId);

	/**
	 * For evaluators.
	 * 
	 * @return detailed view on the survey template
	 */
	SurveyTemplateDto getTemplate(@ValidId Long templateId);

	/**
	 * For evaluators.
	 * 
	 * @return filled surveys matching specified criteria
	 */
	List<FilledSurveyDto> querySurveys(FilledSurveyQuery query);

	/**
	 * For students & evaluators.
	 * 
	 * @return published survey templates matching specified criteria (without
	 *         details)
	 */
	List<PublishedSurveyTemplateDto> queryPublishedTemplates(PublishedSurveyTemplateQuery query);

	/**
	 * For evaluators.
	 * 
	 * @return survey templates (without details) from among those created by
	 *         the calling evaluator, that match specified criteria.
	 */
	List<SurveyTemplateDto> queryTemplates(SurveyTemplateQuery query);
}
