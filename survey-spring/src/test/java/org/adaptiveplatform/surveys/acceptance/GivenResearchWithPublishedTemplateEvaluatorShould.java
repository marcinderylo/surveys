package org.adaptiveplatform.surveys.acceptance;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.adaptiveplatform.surveys.builders.CoreFixtureBuilder.EVALUATOR_EMAIL;
import static org.adaptiveplatform.surveys.builders.CoreFixtureBuilder.STUDENT_EMAIL;
import static org.adaptiveplatform.surveys.builders.GroupBuilder.group;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.openQuestion;
import static org.adaptiveplatform.surveys.builders.ResearchBuilder.research;
import static org.adaptiveplatform.surveys.builders.SurveyTemplateBuilder.template;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.application.EvaluationDao;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.builders.SurveysFixtureBuilder;
import org.adaptiveplatform.surveys.dto.ResearchDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Rafał Jamróz
 * @author Marcin Deryło
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/testConfigurationContext.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class GivenResearchWithPublishedTemplateEvaluatorShould {

    @Resource
    private EvaluationDao dao;
    @Resource
    private SurveysFixtureBuilder surveys;
    @Resource
    private CoreFixtureBuilder users;

    private Long templateId, groupId;
    private Long researchId;

    @Before
    public void init() throws Exception {
        users.loginAsTeacher();
        groupId = surveys.createGroup(group("group").withEvaluator(EVALUATOR_EMAIL).withStudent(STUDENT_EMAIL));

        users.loginAsEvaluator();
        templateId = surveys.createTemplate(template("template1").withQuestions(openQuestion("question 1.1")));
        researchId = surveys.createResearch(research().forGroup(groupId).withSurvey(templateId));
    }

    @Test
    public void beAbleToFetchSubmittedSurveys() throws Exception {
        // given
        users.loginAsStudent();
        Long filledSurveyId = surveys.fillAndSubmitSurvey(templateId, groupId);
        // when
        users.loginAsEvaluator();
        ResearchDto research = dao.get(researchId);
        // then
        assertEquals(filledSurveyId, getOnlyElement(research.getSubmittedSurveys()).getId());
    }

    @Test
    public void notBeAbleToFetchStartedButNotYetSubmittedSurveys() throws Exception {
        // given
        users.loginAsStudent();
        surveys.fillSurvey(templateId, groupId);
        // when
        users.loginAsEvaluator();
        final ResearchDto research = dao.get(researchId);
        // then
        assertThat(research.getSubmittedSurveys()).isEmpty();
    }

    @Test
    public void beAbleToFetchGroupNamesInResearch() throws Exception {
        // when
        final ResearchDto research = dao.get(researchId);
        // then
        assertThat(research.getGroups()).hasSize(1);
    }
}