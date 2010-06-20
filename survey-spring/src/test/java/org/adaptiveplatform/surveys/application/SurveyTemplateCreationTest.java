package org.adaptiveplatform.surveys.application;

import static org.adaptiveplatform.surveys.domain.AnswerTemplateBuilder.answer;
import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.multipleChoiceQuestion;
import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.openQuestion;
import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.singleChoiceQuestion;
import static org.adaptiveplatform.surveys.domain.UserAccountBuilder.user;
import static org.adaptiveplatform.surveys.dto.CreateTemplateCommandBuilder.command;
import org.adaptiveplatform.surveys.dto.SurveyTemplateDto;
import static org.adaptiveplatform.surveys.test.Asserts.assertCollectionSize;

import javax.annotation.Resource;
import static org.testng.Assert.assertEquals;
import org.adaptiveplatform.surveys.domain.CoreTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.domain.SurveyTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.dto.CreateSurveyTemplateCommand;
import org.adaptiveplatform.surveys.dto.SurveyTemplateQuery;
import org.adaptiveplatform.surveys.service.AuthenticationServiceMock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Integration tests for survey template services that does not require any
 * initial database state, except for test users.
 *
 * @author Marcin Deryło
 */
@ContextConfiguration("classpath:/testConfigurationContext.xml")
public class SurveyTemplateCreationTest extends AbstractTestNGSpringContextTests {

    /**
     * Login (email) of a user with evaluator privileges.
     */
    public static final String EVALUATOR_LOGIN = "eval@eval.com";
    @Resource
    private AuthenticationServiceMock authentication;
    @Resource
    private SurveyFacade facade;
    @Resource
    private SurveyDao dao;
    @Resource
    private SurveyTestFixtureBuilder fixture;
    @Resource
    private CoreTestFixtureBuilder coreFixture;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        UserAccount evaluator = coreFixture.createUser(user().withEmail(
                EVALUATOR_LOGIN).inRoles(Role.USER, Role.EVALUATOR));


        authentication.authenticate(evaluator.getId(), EVALUATOR_LOGIN,
                Role.USER, Role.EVALUATOR);
    }

    @AfterMethod
    public void afterMethod() throws Exception {
        authentication.logout();
        fixture.cleanUp();
        coreFixture.cleanUp();
    }

    @Test
    public void evaluatorShouldCreateSurveyTemplate() throws Exception {
        // given
        CreateSurveyTemplateCommand createCmd =
                command("test survey").withQuestions(
                openQuestion("an open question"),
                multipleChoiceQuestion("a multiple choice question").
                withAnswers(answer("foo"), answer("bar")),
                singleChoiceQuestion("a single choice question").
                withAnswers(answer("baz").disallowsOtherAnswers(),
                answer("foobar").disallowsOtherAnswers())).build();

        // when
        Long surveyTemplateId = facade.createTemplate(createCmd);

        // then
        assertCollectionSize(dao.queryTemplates(
                new SurveyTemplateQuery()), 1);

        // cleanup
        fixture.deleteSurveyTemplate(surveyTemplateId);
    }

    @Test
    public void evaluatorShouldBeAbleToProvideDescriptionForSurveyTemplate() throws Exception {
        final String description = "a sample survey template";
        // given
        CreateSurveyTemplateCommand createCmd =
                command("test survey").withQuestions(openQuestion("an open question")).
                withDescription(description).build();
        // when
        Long surveyTemplateId = facade.createTemplate(createCmd);
        // then
        final SurveyTemplateDto template = dao.getTemplate(surveyTemplateId);
        assertEquals(template.getDescription(), description);
        //cleanup
        fixture.deleteSurveyTemplate(surveyTemplateId);
    }
}
