package org.adaptiveplatform.surveys.builders;

import static java.util.Arrays.asList;

import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.application.EvaluationFacade;
import org.adaptiveplatform.surveys.application.StudentGroupFacade;
import org.adaptiveplatform.surveys.application.SurveyDao;
import org.adaptiveplatform.surveys.application.SurveyFacade;
import org.adaptiveplatform.surveys.dto.FilledSurveyDto;
import org.adaptiveplatform.surveys.dto.GroupRoleEnum;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateQuery;
import org.adaptiveplatform.surveys.dto.QuestionTypeEnum;
import org.adaptiveplatform.surveys.dto.SurveyQuestionAnswerDto;
import org.adaptiveplatform.surveys.dto.SurveyQuestionDto;
import org.springframework.stereotype.Component;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

@Component
public class SurveysFixtureBuilder {

    @Resource
    private SurveyFacade surveyFacade;

    @Resource
    private EvaluationFacade evaluationFacade;

    @Resource
    private StudentGroupFacade groupFacade;
    @Resource
    private SurveyDao surveyDao;

    public Long createTemplate(SurveyTemplateBuilder survey) {
        return surveyFacade.createTemplate(survey.build());
    }

    public Long createResearch(ResearchBuilder research) {
        return evaluationFacade.createResearch(research.build());
    }

    public Long createGroup(GroupBuilder group) {
        return groupFacade.createGroup(group.build());
    }

    public Long fillSurvey(Long templateId, Long groupId) {
        Long filledSurveyId = getSurveyForFilling(templateId, groupId);
        fillSurvey(filledSurveyId);
        return filledSurveyId;
    }

    public Long fillAndSubmitSurvey(Long surveyTemplateId, Long groupId) {
        Long filledSurveyId = getSurveyForFilling(surveyTemplateId, groupId);
        fillSurvey(filledSurveyId);
        surveyFacade.submit(filledSurveyId);
        return filledSurveyId;
    }

    private void fillSurvey(Long filledSurveyId) {
        FilledSurveyDto survey = surveyDao.getSurvey(filledSurveyId);
        for (SurveyQuestionDto question : survey.getQuestions()) {

            List<Integer> selectedAnswers = null;
            String comment = null;
            if (question.getType() == QuestionTypeEnum.OPEN) {
                comment = "this is my open question answer";
            } else {
                SurveyQuestionAnswerDto selectedAnswer = question.getAnswers().get(0);
                selectedAnswers = asList(selectedAnswer.getNumber());
                if (selectedAnswer.getRequiresComment()) {
                    comment = "this is my comment";
                }
            }
            surveyFacade.answerQuestion(survey.getId(), question.getNumber(), selectedAnswers, comment);
        }
    }

    private Long getSurveyForFilling(Long surveyTemplateId, Long groupId) {
        PublishedSurveyTemplateQuery query = new PublishedSurveyTemplateQuery(GroupRoleEnum.STUDENT);
        List<PublishedSurveyTemplateDto> templates = surveyDao.queryPublishedTemplates(query);
        PublishedSurveyTemplateDto publication = Iterables.find(templates,
                findPublicationByTemplateAndGroup(surveyTemplateId, groupId));

        Long filledSurveyId = surveyFacade.startFilling(publication.getId());
        return filledSurveyId;
    }

    private Predicate<PublishedSurveyTemplateDto> findPublicationByTemplateAndGroup(final Long surveyTemplateId,
            final Long groupId) {
        return new Predicate<PublishedSurveyTemplateDto>() {
            @Override
            public boolean apply(PublishedSurveyTemplateDto input) {
                return surveyTemplateId.equals(input.getTemplateId()) && groupId.equals(input.getGroupId());
            }
        };
    }

}
