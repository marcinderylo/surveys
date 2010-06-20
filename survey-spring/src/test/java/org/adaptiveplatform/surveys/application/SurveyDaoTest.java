package org.adaptiveplatform.surveys.application;

import static org.adaptiveplatform.surveys.domain.AnswerTemplateBuilder.answer;
import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.multipleChoiceQuestion;
import static org.adaptiveplatform.surveys.domain.StudentGroupBuilder.group;
import static org.adaptiveplatform.surveys.domain.SurveyTemplateBuilder.template;
import static org.adaptiveplatform.surveys.domain.UserAccountBuilder.user;
import static org.adaptiveplatform.surveys.utils.Collections42.firstOf;
import static org.adaptiveplatform.surveys.test.Asserts.*;
import static org.testng.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.*;
import org.adaptiveplatform.surveys.dto.*;
import org.adaptiveplatform.surveys.exception.FilledSurveyDoesNotExistException;
import org.adaptiveplatform.surveys.service.AuthenticationServiceMock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.*;

@ContextConfiguration("classpath:/testConfigurationContext.xml")
public class SurveyDaoTest extends AbstractTestNGSpringContextTests {

    @Resource
    private AuthenticationServiceMock authentication;
    @Resource
    private SurveyDao dao;
    @Resource
    private SurveyTestFixtureBuilder fixture;
    @Resource
    private CoreTestFixtureBuilder coreFixture;
    @Resource
    private StudentGroupTestFixtureBuilder groupsFixture;
    private Long userId;
    private Long anotherUserId;
    private Long administratorId;
    private Long teacherId;
    private Long filledTemplateId;
    private Long notFilledTemplateId;
    private Long filledSurveyId;
    private Long groupId;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        UserAccount john = coreFixture.createUser(user().withEmail(
                "john@doe.com").inRoles(Role.USER));
        userId = john.getId();

        UserAccount tom = coreFixture.createUser(user().withEmail(
                "tom@foo.com").inRoles(Role.USER));
        anotherUserId = tom.getId();

        UserAccount keyser = coreFixture.createUser(user().withEmail(
                "keyser@soze.com").inRoles(Role.USER, Role.ADMINISTRATOR,
                Role.EVALUATOR));
        administratorId = keyser.getId();

        UserAccount teacher = coreFixture.createUser(user().withEmail(
                "AnApacheWithATomahawk@foo.com").inRoles(Role.TEACHER));
        teacherId = teacher.getId();


        SurveyTemplate template1 =
                fixture.createTemplate(newTemplate(keyser, "sample survey"));
        filledTemplateId = template1.getId();

        SurveyTemplate template2 = fixture.createTemplate(newTemplate(keyser,
                "another survey"));
        notFilledTemplateId = template2.getId();

        fixture.createTemplate(newTemplate(keyser, "yet another survey"));

        StudentGroup group = groupsFixture.createGroup(group(
                "test group", teacher).withStudents(john).
                withEvaluators(keyser));
        groupId = group.getId();

        StudentGroup anotherGroup = groupsFixture.createGroup(group(
                "another group", teacher).withStudents(tom).
                withEvaluators(keyser));

        fixture.publishTemplate(template1, group);
        fixture.publishTemplate(template2, group);
        fixture.publishTemplate(template1, anotherGroup);

        filledSurveyId = fixture.fillSurvey(filledTemplateId, groupId,
                john).getId();
    }

    @AfterMethod
    public void afterMethod() throws Exception {
        authentication.logout();

        fixture.cleanUp();
        groupsFixture.cleanUp();
        coreFixture.cleanUp();
    }

    @Test
    public void shouldStudentReadHisFilledSurvey() throws Exception {
        // given
        studentIsLoggedIn();
        // when
        final FilledSurveyDto survey = dao.getSurvey(filledSurveyId);
        // then
        assertNotNull(survey);
    }

    @Test
    public void shouldStudentSeeSurveyTemplateDescription() throws Exception {
        // given
        studentIsLoggedIn();
        // when
        final FilledSurveyDto survey = dao.getSurvey(filledSurveyId);
        // then
        assertEquals(survey.getDescription(), "test survey template");
    }

    @Test
    public void shouldEvaluatorReadFilledSurveyForHisPublication() throws
            Exception {
        // given
        evaluatorIsLoggedIn();
        // when
        final FilledSurveyDto survey = dao.getSurvey(filledSurveyId);
        // then
        assertNotNull(survey);
    }

    @Test(expectedExceptions = {FilledSurveyDoesNotExistException.class})
    public void cantReadFilledSurveyIfNotOwnerNorEvaluatorWhoCreatedThePublication()
            throws Exception {
        // given
        authentication.authenticate(teacherId, "teacher@school.com", Role.USER,
                Role.TEACHER, Role.EVALUATOR);
        // when
        dao.getSurvey(filledSurveyId);
        // then
        expectException();
    }

    @Test
    public void shouldEvaluatorReadFilledSurveysForPublishedTemplate() throws
            Exception {
        // given
        FilledSurveyQuery query = new FilledSurveyQuery();
        query.setTemplateId(filledTemplateId);
        query.setGroupId(groupId);

        evaluatorIsLoggedIn();
        // when
        List<FilledSurveyDto> surveys = dao.querySurveys(query);

        // then
        assertCollectionSize(surveys, 1);
    }

    @Test
    public void surveyShouldBeAvailableForStudent()
            throws
            Exception {
        studentIsLoggedIn();
        // when
        List<PublishedSurveyTemplateDto> surveys = dao.queryPublishedTemplates(
                studentQuery());
        // then
        assertSurveyIsSubmitted(getSurveyTemplate(surveys, filledTemplateId));
        assertSurveyFillingIsNotStarted(getSurveyTemplate(surveys,
                notFilledTemplateId));
    }

    @Test
    public void shouldStudentSeeOnlyPublicationsFromHisGroups() throws Exception {
        // given
        authentication.authenticate(anotherUserId, "tom@foo.com", Role.USER);
        // when
        List<PublishedSurveyTemplateDto> surveys = dao.queryPublishedTemplates(
                studentQuery());
        // then
        assertCollectionSize(surveys, 1);
        assertFalse(firstOf(surveys).isFilled());
    }

    private void studentIsLoggedIn() {
        // given
        authentication.authenticate(userId, "john@doe.com",
                Role.USER);
    }

    private SurveyTemplateBuilder newTemplate(UserAccount author, String title) {
        return template(title).byUser(author).
                withQuestions(multipleChoiceQuestion(
                "another not published question").
                withAnswers(answer("dobrze"))).describedAs("test survey template");
    }

    private PublishedSurveyTemplateQuery studentQuery() {
        PublishedSurveyTemplateQuery query =
                new PublishedSurveyTemplateQuery();
        query.setRunAs(GroupRoleEnum.STUDENT);
        return query;
    }

    private PublishedSurveyTemplateDto getSurveyTemplate(
            List<PublishedSurveyTemplateDto> templates, Long id) {
        for (PublishedSurveyTemplateDto surveyTemplateDto : templates) {
            if (id.equals(surveyTemplateDto.getTemplateId())) {
                return surveyTemplateDto;
            }
        }
        fail("No survey template (ID=" + id + ")");
        return null; // unreachable
    }

    private void assertSurveyIsSubmitted(PublishedSurveyTemplateDto survey) {
        assertTrue(survey.getSubmitted(),
                "Expected a filled survey but it has never been filled.");
    }

    private void assertSurveyFillingIsNotStarted(
            PublishedSurveyTemplateDto survey) {
        assertNull(survey.getFilledSurveyId(),
                "Expected a survey that has never been filled but found a filled one.");
    }

    private void evaluatorIsLoggedIn() {
        authentication.authenticate(administratorId, "keyser@soze.com",
                Role.USER, Role.EVALUATOR);
    }
}
