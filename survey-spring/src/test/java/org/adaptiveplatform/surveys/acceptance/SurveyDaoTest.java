package org.adaptiveplatform.surveys.acceptance;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.adaptiveplatform.surveys.builders.GroupBuilder.group;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.openQuestion;
import static org.adaptiveplatform.surveys.builders.ResearchBuilder.research;
import static org.adaptiveplatform.surveys.builders.SurveyTemplateBuilder.template;
import static org.adaptiveplatform.surveys.builders.UserAccountBuilder.evaluator;
import static org.adaptiveplatform.surveys.builders.UserAccountBuilder.student;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.application.SurveyDao;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.builders.SurveysFixtureBuilder;
import org.adaptiveplatform.surveys.dto.FilledSurveyDto;
import org.adaptiveplatform.surveys.dto.FilledSurveyQuery;
import org.adaptiveplatform.surveys.dto.GroupRoleEnum;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.PublishedSurveyTemplateQuery;
import org.adaptiveplatform.surveys.exception.FilledSurveyDoesNotExistException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/testConfigurationContext.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SurveyDaoTest {

    @Resource
    private SurveyDao dao;
    @Resource
    private SurveysFixtureBuilder surveys;
    @Resource
    private CoreFixtureBuilder users;

    private Long filledTemplateId;
    private Long notFilledTemplateId;
    private Long filledSurveyId;
    private Long groupId;
    private Long anotherGroupId;

    @Before
    public void beforeMethod() throws Exception {
        users.createUser(student("john@doe.com"));
        users.createUser(student("tom@foo.com"));
        users.createUser(evaluator("evaluator@adapt.com"));
        users.createUser(evaluator("anotherEvaluator@ada.pt"));

        groupId = surveys.createGroup(group("test group").withEvaluator("evaluator@adapt.com").withStudent(
                "john@doe.com"));
        anotherGroupId = surveys.createGroup(group("another group").withStudent("tom@foo.com").withEvaluator(
                "evaluator@adapt.com"));

        users.loginAs("evaluator@adapt.com");
        filledTemplateId = surveys.createTemplate(template("sample survey").withDescription("test survey template")
                .withQuestions(openQuestion("open")));
        notFilledTemplateId = surveys.createTemplate(template("another survey").withQuestions(openQuestion("open")));
        surveys.createTemplate(template("yet another survey").withQuestions(openQuestion("open")));

        surveys.createResearch(research().withSurvey(filledTemplateId).forGroup(groupId));
        surveys.createResearch(research().withSurvey(notFilledTemplateId).forGroup(groupId));
        surveys.createResearch(research().withSurvey(filledTemplateId).forGroup(anotherGroupId));

        users.loginAs("john@doe.com");
        filledSurveyId = surveys.fillAndSubmitSurvey(filledTemplateId, groupId);
    }

    @Test
    public void shouldStudentReadHisFilledSurvey() throws Exception {
        // given
        users.loginAs("john@doe.com");
        // when
        final FilledSurveyDto survey = dao.getSurvey(filledSurveyId);
        // then
        assertEquals("test survey template", survey.getDescription());
    }

    @Test
    public void shouldEvaluatorReadFilledSurveyForHisPublication() throws Exception {
        // given
        users.loginAs("evaluator@adapt.com");
        // when
        final FilledSurveyDto survey = dao.getSurvey(filledSurveyId);
        // then
        assertNotNull(survey);
    }

    @Test(expected = FilledSurveyDoesNotExistException.class)
    public void cantReadFilledSurveyIfNotOwnerNorEvaluatorWhoCreatedThePublication() throws Exception {
        // given
        users.loginAs("anotherEvaluator@ada.pt");
        // when
        dao.getSurvey(filledSurveyId);
        // then - exception should be thrown
    }

    @Test
    public void shouldEvaluatorReadFilledSurveysForPublishedTemplate() throws Exception {
        // given
        FilledSurveyQuery query = new FilledSurveyQuery();
        query.setTemplateId(filledTemplateId);
        query.setGroupId(groupId);

        users.loginAs("evaluator@adapt.com");
        // when
        List<FilledSurveyDto> surveys = dao.querySurveys(query);

        // then
        assertThat(surveys).hasSize(1);
    }

    @Test
    public void surveyShouldBeAvailableForStudent() throws Exception {
        users.loginAs("john@doe.com");
        // when
        List<PublishedSurveyTemplateDto> surveys = dao.queryPublishedTemplates(new PublishedSurveyTemplateQuery(
                GroupRoleEnum.STUDENT));
        // then
        assertSurveyIsNotInTheList(surveys, filledTemplateId);
        assertSurveyFillingIsNotStarted(getSurveyTemplate(surveys, notFilledTemplateId));
    }

    @Test
    public void shouldStudentSeeOnlyPublicationsFromHisGroups() throws Exception {
        // given
        users.loginAs("john@doe.com");
        // when
        List<PublishedSurveyTemplateDto> surveys = dao.queryPublishedTemplates(new PublishedSurveyTemplateQuery(
                GroupRoleEnum.STUDENT));
        // then
        assertFalse(getOnlyElement(surveys).isFilled());
    }

    @Test
    public void shouldStudentSeeStartedButNotYetSubmittedSurveys() throws Exception {
        users.loginAs("tom@foo.com");
        surveys.fillSurvey(filledTemplateId, anotherGroupId);

        List<PublishedSurveyTemplateDto> publishedSurveys = dao
                .queryPublishedTemplates(new PublishedSurveyTemplateQuery(GroupRoleEnum.STUDENT));
        assertThat(publishedSurveys).hasSize(1);
    }

    private PublishedSurveyTemplateDto getSurveyTemplate(List<PublishedSurveyTemplateDto> templates, Long id) {
        PublishedSurveyTemplateDto found = getSurveyTemplateOrNull(templates, id);
        if (found == null) {
            fail("No survey template (ID=" + id + ")");
        }
        return found;
    }

    private PublishedSurveyTemplateDto getSurveyTemplateOrNull(List<PublishedSurveyTemplateDto> templates, Long id) {
        for (PublishedSurveyTemplateDto surveyTemplateDto : templates) {
            if (id.equals(surveyTemplateDto.getTemplateId())) {
                return surveyTemplateDto;
            }
        }
        return null;
    }

    private void assertSurveyFillingIsNotStarted(PublishedSurveyTemplateDto survey) {
        assertNull("Expected a survey that has never been filled but found a filled one.", survey.getFilledSurveyId());
    }

    private void assertSurveyIsNotInTheList(List<PublishedSurveyTemplateDto> surveys, Long filledTemplateId) {
        assertNull(getSurveyTemplateOrNull(surveys, filledTemplateId));
    }
}
