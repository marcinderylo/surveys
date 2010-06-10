package org.adaptiveplatform.surveys.application;

import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.openQuestion;
import static org.adaptiveplatform.surveys.domain.ResearchBuilder.research;
import static org.adaptiveplatform.surveys.domain.StudentGroupBuilder.group;
import static org.adaptiveplatform.surveys.domain.SurveyTemplateBuilder.template;
import static org.adaptiveplatform.surveys.domain.UserAccountBuilder.user;
import static org.adaptiveplatform.surveys.test.Asserts.assertCollectionSize;
import static org.adaptiveplatform.surveys.test.Asserts.assertEmpty;
import static org.adaptiveplatform.surveys.test.Asserts.expectException;
import static org.adaptiveplatform.surveys.utils.Collections42.firstOf;

import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.CoreTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.FilledSurvey;
import org.adaptiveplatform.surveys.domain.ResearchTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.domain.StudentGroup;
import org.adaptiveplatform.surveys.domain.StudentGroupTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.SurveyTemplate;
import org.adaptiveplatform.surveys.domain.SurveyTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.dto.ResearchDto;
import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.service.AuthenticationServiceMock;
import org.adaptiveplatform.surveys.test.UserAccountToDto;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Marcin Deryło
 */
@ContextConfiguration("classpath:/testConfigurationContext.xml")
public class GivenExistingFilledSurveysEvaluatorShould extends AbstractTestNGSpringContextTests {

    @Resource
    private EvaluationDao dao;
    @Resource
    private SurveyTestFixtureBuilder surveyFixture;
    @Resource
    private CoreTestFixtureBuilder userFixture;
    @Resource
    private ResearchTestFixtureBuilder researchFixture;
    @Resource
    private AuthenticationServiceMock authentication;
    @Resource
    private StudentGroupTestFixtureBuilder groupFixture;
    private Long templateId, groupId;
    private Long researchId;
    private UserDto evaluator;
    private UserAccount student;

    @BeforeMethod
    public void init() throws Exception {
        UserAccount user1 = userFixture.createUser(user("eval1", "eval",
                "eval1@adapt.com", Role.EVALUATOR));
        evaluator = UserAccountToDto.INSTANCE.apply(user1);

        SurveyTemplate template1 =
                surveyFixture.createTemplate(template("template1").byUser(user1).
                withQuestions(openQuestion("question 1.1")));
        templateId = template1.getId();
        researchId = researchFixture.createResearch(research("Test research",
                template1)).getId();
        authentication.authenticate(user1.getId(), "eval1@adapt.com",
                Role.EVALUATOR);
        final UserDto admin = UserAccountToDto.INSTANCE.apply(
                userFixture.createUser(user("admin", "foo", "admin@adapt.com",
                Role.TEACHER, Role.USER)));
        student = userFixture.createUser(user("student",
                "bar", "student@adapt.com", Role.USER));
        final StudentGroup group =
                groupFixture.createGroup(group("group", admin));
        group.addEvaluator(evaluator);
        group.addStudent(UserAccountToDto.INSTANCE.apply(student));
        groupId = group.getId();
        final Long publishedTemplateId =
                surveyFixture.publishTemplate(templateId, groupId).getId();

        researchFixture.addPublicationToResearch(researchId, publishedTemplateId);
    }

    @AfterMethod
    public void cleanUp() {
        authentication.logout();
        researchFixture.cleanUp();
        surveyFixture.cleanUp();
        groupFixture.cleanUp();
        userFixture.cleanUp();
    }

    @Test
    public void shouldFetchSubmittedSurveys() throws Exception {
        // given
        final FilledSurvey filledSurvey =
                surveyFixture.fillSurvey(templateId, groupId, student);
        // when
        final ResearchDto research = dao.get(researchId);
        // then
        assertCollectionSize(research.getSubmittedSurveys(), 1);
        Assert.assertEquals(firstOf(research.getSubmittedSurveys()).getId(), filledSurvey.getId());
    }

    @Test
    public void shouldNotFetchStartedButNotYetSubmittedSurveys() throws Exception {
        // given
        surveyFixture.startFilling(templateId, groupId, student);
        // when
        final ResearchDto research = dao.get(researchId);
        // then
        assertEmpty(research.getSubmittedSurveys());
    }
}
