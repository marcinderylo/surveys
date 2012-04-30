package org.adaptiveplatform.surveys.acceptance;

import org.adaptiveplatform.surveys.ContainerEnabledTest;
import org.adaptiveplatform.surveys.application.SurveyDao;
import org.adaptiveplatform.surveys.application.SurveyFacade;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.builders.SurveysFixtureCreator;
import org.adaptiveplatform.surveys.dto.GroupRoleEnum;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateQuery;
import org.adaptiveplatform.surveys.exception.SurveyTemplateAlreadyPublishedException;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static org.adaptiveplatform.surveys.builders.AnswerBuilder.answer;
import static org.adaptiveplatform.surveys.builders.CoreFixtureBuilder.EVALUATOR_EMAIL;
import static org.adaptiveplatform.surveys.builders.GroupBuilder.group;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.*;
import static org.adaptiveplatform.surveys.builders.ResearchBuilder.research;
import static org.adaptiveplatform.surveys.builders.SurveyTemplateBuilder.template;
import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Rafał Jamróz
 * @author Marcin Deryło
 */
public class HavingAPublishedSurveyTemplateEvaluatorShould extends ContainerEnabledTest {

    @Resource
    private SurveysFixtureCreator surveys;
    @Resource
    private CoreFixtureBuilder users;

    @Resource
    private SurveyFacade surveyFacade;
    @Resource
    private SurveyDao dao;
    private Long groupId;
    private Long existingTemplateId;

    @Before
    public void beforeMethod() throws Exception {
        users.loginAsTeacher();
        groupId = surveys.createGroup(group("test group").withEvaluator(EVALUATOR_EMAIL));

        users.loginAsEvaluator();
        existingTemplateId = surveys.createTemplate(template("a test template").withQuestions(
                /* multiple choice question (nr 1) */
                multiChoiceQuestion("multi-choice question").withAnswers(answer("answer 1"), answer("answer 2"),
                        answer("other: ...").requiresComment().disallowsOtherAnswers(),
                        answer("none of the above").disallowsOtherAnswers()),
                /* single choice question (nr 2) */
                singleChoiceQuestion("single-choice question").withAnswers(answer("option 1"), answer("option 2"),
                        answer("other: ...").requiresComment()),
                /* open question (nr 3) */
                openQuestion("open question")));
    }

    @Test
    public void beAbleToQueryHisPublications() throws Exception {
        // given
        researchHasBeenCreatedWithTemplate();
        // when
        PublishedSurveyTemplateQuery query = new PublishedSurveyTemplateQuery(GroupRoleEnum.EVALUATOR);

        List<PublishedSurveyTemplateDto> publications = dao.queryPublishedTemplates(query);
        // then
        assertThat(publications).hasSize(1);
    }

    @Test
    public void beAbleToRemoveThePublication() throws Exception {
        // when
        surveyFacade.removeSurveyTemplate(existingTemplateId);
        // then
        PublishedSurveyTemplateQuery query = new PublishedSurveyTemplateQuery(GroupRoleEnum.EVALUATOR);
        assertThat(dao.queryPublishedTemplates(query)).isEmpty();
    }

    @Test(expected = SurveyTemplateAlreadyPublishedException.class)
    public void notBeAbleToRemoveSurveyTemplate() throws Exception {
        // given
        researchHasBeenCreatedWithTemplate();
        // when
        surveyFacade.removeSurveyTemplate(existingTemplateId);
        // then - exception should be thrown
    }

    private void researchHasBeenCreatedWithTemplate() {
        surveys.createResearch(research().withSurvey(existingTemplateId).forGroup(groupId));
    }
}
