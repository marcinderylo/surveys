package org.adaptiveplatform.surveys.service;

import org.adaptiveplatform.surveys.domain.SurveyTemplate;
import org.adaptiveplatform.surveys.dto.UserDto;


public interface SurveyTemplateRepository {

    void persist(SurveyTemplate template);

    SurveyTemplate get(Long templateId);

    void delete(SurveyTemplate template);

    boolean exists(UserDto creator, String name);

    SurveyTemplate getExisting(Long surveyTemplateId);
}
