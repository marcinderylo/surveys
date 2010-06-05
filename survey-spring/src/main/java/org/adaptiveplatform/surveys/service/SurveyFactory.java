package org.adaptiveplatform.surveys.service;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.FilledSurvey;
import org.adaptiveplatform.surveys.domain.StudentGroup;
import org.adaptiveplatform.surveys.domain.SurveyPublication;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.joda.time.DateTime;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class SurveyFactory {

    @Resource
    private SurveyRepository surveyRepository;
    @Resource
    private SurveyPublicationRepository publicationRepository;

    public FilledSurvey createSurvey(Long publicationId, UserDto user) {
        SurveyPublication publication = publicationRepository.get(publicationId);
        if (publication == null) {
            throw new IllegalArgumentException(
                    "No such survey template assigned to the group");
        }
        if (!publication.isFillingEnabled(new DateTime())) {
            throw new IllegalStateException(
                    "Selected survey template is not currently published");
        }
        // TODO add test for checking user's role
        StudentGroup group = publication.getGroup();
        if (!group.isStudent(user)) {
            throw new AccessDeniedException(
                    "Must be a student in the group to fill the survey");
        }
        FilledSurvey survey = new FilledSurvey(publication, user);
        surveyRepository.persist(survey);
        return survey;
    }
}
