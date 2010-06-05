package org.adaptiveplatform.surveys.service;

import java.util.List;

import org.adaptiveplatform.surveys.domain.FilledSurvey;
import org.adaptiveplatform.surveys.dto.UserDto;

public interface SurveyRepository {

	void persist(FilledSurvey survey);

	FilledSurvey get(Long surveyId);
        List<FilledSurvey> list(Long templateId, Long groupId);
        FilledSurvey get(Long templateId, Long groupId, UserDto user);

        List<FilledSurvey> list(Long templateId);
}
