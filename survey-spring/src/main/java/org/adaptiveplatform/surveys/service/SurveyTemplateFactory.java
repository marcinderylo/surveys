package org.adaptiveplatform.surveys.service;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.SurveyTemplate;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.SurveyTemplateAlreadyExistsException;
import org.springframework.stereotype.Service;

@Service
public class SurveyTemplateFactory {

    @Resource
    private SurveyTemplateRepository templateRepository;

    public SurveyTemplate createDraft(UserDto creator, String name) {
        if (templateRepository.exists(creator, name)) {
            throw new SurveyTemplateAlreadyExistsException(name);
        }
        SurveyTemplate template = new SurveyTemplate(creator, name);

        templateRepository.persist(template);
        return template;
    }
}
