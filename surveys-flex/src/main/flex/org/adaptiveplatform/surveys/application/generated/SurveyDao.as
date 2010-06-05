package org.adaptiveplatform.surveys.application.generated {
	
  	import mx.collections.ArrayCollection;
  	import org.adaptiveplatform.surveys.dto.generated.FilledSurveyQuery;
  	import org.adaptiveplatform.surveys.dto.generated.SurveyTemplateDto;
  	import org.adaptiveplatform.surveys.dto.generated.FilledSurveyDto;
  	import org.adaptiveplatform.surveys.dto.generated.PublishedSurveyTemplateQuery;
  	import org.adaptiveplatform.surveys.dto.generated.SurveyTemplateQuery;
	import org.adaptiveplatform.surveys.application.ResultHandler;

	/**
	 * NOTE: This file is autogenerated and will be overwritten every time.
	 */
	public interface SurveyDao {
		function getTemplate(long:Number):ResultHandler;
		function getSurvey(long:Number):ResultHandler;
		function querySurveys(filledSurveyQuery:FilledSurveyQuery):ResultHandler;
		function queryPublishedTemplates(publishedSurveyTemplateQuery:PublishedSurveyTemplateQuery):ResultHandler;
		function queryTemplates(surveyTemplateQuery:SurveyTemplateQuery):ResultHandler;
	}
}
