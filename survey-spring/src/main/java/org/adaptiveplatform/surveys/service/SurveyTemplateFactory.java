package org.adaptiveplatform.surveys.service;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.SurveyTemplate;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

@Service
public class SurveyTemplateFactory {

        @Resource
        private SurveyTemplateRepository templateRepository;

        public SurveyTemplate createDraft(UserDto creator, String name) {
                Validate.isTrue(!templateRepository.exists(creator, name),
                        "You already have template with such name!");
                SurveyTemplate template = new SurveyTemplate(creator, name);
                templateRepository.persist(template);
                return template;
        }
}
