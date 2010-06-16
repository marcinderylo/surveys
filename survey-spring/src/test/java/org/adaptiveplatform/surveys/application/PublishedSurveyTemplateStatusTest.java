package org.adaptiveplatform.surveys.application;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

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
import org.adaptiveplatform.surveys.service.AuthenticationServiceMock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.*;

@ContextConfiguration("classpath:/testConfigurationContext.xml")
public class PublishedSurveyTemplateStatusTest extends AbstractTestNGSpringContextTests {

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
    private Long filledPublicationId;
    private Long notFilledPublicationId;

    @BeforeMethod
    public void havingTwoPublicationsWithSameGroupAndSurveyTemplate() {
        UserAccount student = coreFixture.createUser(user().withEmail(
                "john@doe.com").inRoles(Role.USER));
        UserAccount keyser = coreFixture.createUser(user().withEmail(
                "keyser@soze.com").inRoles(Role.USER, Role.ADMINISTRATOR,
                Role.EVALUATOR));
        UserAccount teacher = coreFixture.createUser(user().withEmail(
                "AnApacheWithATomahawk@foo.com").inRoles(Role.TEACHER));

        SurveyTemplate template =
                fixture.createTemplate(template("some survey").byUser(keyser).
                withQuestions(multipleChoiceQuestion("another not published question").withAnswers(
                answer("dobrze"))));
        StudentGroup group = groupsFixture.createGroup(group("test group", teacher).
                withStudents(student).withEvaluators(keyser));
        final SurveyPublication publication1 = fixture.publishTemplate(template, group);
        filledPublicationId = publication1.getId();
        fixture.fillSurvey(filledPublicationId, student);
        final SurveyPublication publication2 = fixture.publishTemplate(template, group);
        notFilledPublicationId = publication2.getId();
        authentication.authenticate(student.getId(), student.getEmail(), Role.STUDENT, Role.USER);
    }

    @AfterMethod
    public void afterMethod() throws Exception {
        authentication.logout();
        fixture.cleanUp();
        groupsFixture.cleanUp();
        coreFixture.cleanUp();
    }

    /**
     *  This is a regression test for following bug: when a student queries for fillable survey
     *  templates the published survey templte is marked as filled is there's a filled survey for
     *  this user with the same group & template id. Currently, with researches, it's possible that
     *  multiple published survey templates have same group & survey template IDs and each of them
     *  should have it's status calculated separately.
     */
    @Test
    public void shouldCalculatePublishedSurveyTemplateStatusForConcretePublication() {
        PublishedSurveyTemplateQuery query = new PublishedSurveyTemplateQuery();
        query.setRunAs(GroupRoleEnum.STUDENT);
        final List<PublishedSurveyTemplateDto> fillablePublications =
                dao.queryPublishedTemplates(query);
        assertPublicationIsFilled(fillablePublications, filledPublicationId);
        assertPublicationIsNotFilled(fillablePublications, notFilledPublicationId);
    }

    private void assertPublicationIsFilled(List<PublishedSurveyTemplateDto> publications,
            Long publicationId) {
        assertPublicationStatus(publications, publicationId,
                SurveyStatusEnum.SUBMITTED);
    }

    private void assertPublicationIsNotFilled(List<PublishedSurveyTemplateDto> publications,
            Long publicationId) {
        assertPublicationStatus(publications, publicationId, SurveyStatusEnum.PENDING);
    }

    private void assertPublicationStatus(List<PublishedSurveyTemplateDto> publications,
            Long publicationId, SurveyStatusEnum expectedStatus) {
        PublishedSurveyTemplateDto publication = selectPublication(publicationId, publications);
        assertEquals(publication.getStatus(), expectedStatus, "Published survey template status");
    }

    private PublishedSurveyTemplateDto selectPublication(Long publicationId,
            List<PublishedSurveyTemplateDto> publications) {
        for (PublishedSurveyTemplateDto publication : publications) {
            if (publicationId.equals(publication.getId())) {
                return publication;
            }
        }
        fail("No such published survey template (ID=" + publicationId + ")");
        return null;
    }
}
