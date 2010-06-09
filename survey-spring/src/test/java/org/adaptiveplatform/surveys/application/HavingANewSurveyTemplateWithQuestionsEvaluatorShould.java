package org.adaptiveplatform.surveys.application;

import static org.adaptiveplatform.surveys.domain.AnswerTemplateBuilder.answer;
import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.multipleChoiceQuestion;
import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.openQuestion;
import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.singleChoiceQuestion;
import static org.adaptiveplatform.surveys.domain.StudentGroupBuilder.group;
import static org.adaptiveplatform.surveys.domain.SurveyTemplateBuilder.template;
import static org.adaptiveplatform.surveys.domain.UserAccountBuilder.user;
import static org.adaptiveplatform.surveys.dto.CreateTemplateCommandBuilder.command;
import static org.adaptiveplatform.surveys.test.Asserts.assertCollectionSize;
import static org.adaptiveplatform.surveys.test.Asserts.assertEmpty;
import static org.adaptiveplatform.surveys.test.Asserts.expectException;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.CoreTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.domain.StudentGroupTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.SurveyTemplate;
import org.adaptiveplatform.surveys.domain.SurveyTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.dto.CreateSurveyTemplateCommand;
import org.adaptiveplatform.surveys.dto.PublishSurveyTemplateCommand;
import org.adaptiveplatform.surveys.dto.QuestionTemplateDto;
import org.adaptiveplatform.surveys.dto.QuestionTypeEnum;
import org.adaptiveplatform.surveys.dto.SurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.SurveyTemplateQuery;
import org.adaptiveplatform.surveys.exception.NotAllowedToPublishTemplatesInGroupException;
import org.adaptiveplatform.surveys.exception.SurveyTemplateAlreadyExistsException;
import org.adaptiveplatform.surveys.service.AuthenticationServiceMock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Integration tests requiring an existing survey template and usual user test
 * data.
 * @author Marcin Deryło
 */
@ContextConfiguration("classpath:/testConfigurationContext.xml")
public class HavingANewSurveyTemplateWithQuestionsEvaluatorShould extends AbstractTestNGSpringContextTests {

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
    @Resource
    private StudentGroupFacade groupFacade;
    @Resource
    private StudentGroupTestFixtureBuilder groupsFixture;
    private Long groupId;
    private Long inaccessibleGroupId;
    private Long existingTemplateId;
    private UserAccount evaluator;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        evaluator = coreFixture.createUser(user().withEmail(
                EVALUATOR_LOGIN).inRoles(Role.USER, Role.EVALUATOR));

        SurveyTemplate template = fixture.createTemplate(template(
                "a test template").withQuestions(
                /*multiple choice question (nr 1)*/
                multipleChoiceQuestion("multi-choice question").
                withAnswers(answer("answer 1"), answer("answer 2"),
                answer("other: ...").requiresComment(),
                answer("none of the above").disallowsOtherAnswers()),
                /* single choice question (nr 2)*/
                singleChoiceQuestion("single-choice question").
                withAnswers(answer("option 1").disallowsOtherAnswers(),
                answer("option 2").disallowsOtherAnswers(),
                answer("other: ...").requiresComment()),
                /* open question (nr 3) */
                openQuestion("open question")).byUser(evaluator));
        existingTemplateId = template.getId();

        UserAccount teacher = coreFixture.createUser(user().withEmail(
                "teacher@umcs.com.pl").inRoles(Role.TEACHER));
        groupId = groupsFixture.createGroup(group("test group", teacher).
                withEvaluators(evaluator)).getId();

        inaccessibleGroupId = groupsFixture.createGroup(group(
                "inaccessible group", teacher)).getId();

        authentication.authenticate(evaluator.getId(), EVALUATOR_LOGIN,
                Role.USER, Role.EVALUATOR);
    }

    @AfterMethod
    public void afterMethod() throws Exception {
        authentication.logout();
        fixture.cleanUp();
        groupsFixture.cleanUp();
        coreFixture.cleanUp();
    }

    @Test
    public void beAbleToReadIt() throws Exception {
        // when
        SurveyTemplateDto template = dao.getTemplate(existingTemplateId);
        // then
        assertTemplateName(template, "a test template");
        assertCollectionSize(template.getQuestions(), 3);
        assertQuestion(template, 0, QuestionTypeEnum.MULTIPLE_CHOICE,
                "multi-choice question");
        assertQuestion(template, 1, QuestionTypeEnum.SINGLE_CHOICE,
                "single-choice question");
        assertQuestion(template, 2, QuestionTypeEnum.OPEN,
                "open question");
    }

    @Test
    public void beAbleToQueryHisTemplatesWithoutReceivingDetails() throws
            Exception {
        // when
        List<SurveyTemplateDto> templates = dao.queryTemplates(
                new SurveyTemplateQuery());
        // then
        assertCollectionSize(templates, 1);
        assertEmpty(templates.get(0).getQuestions());
    }

    @Test
    public void beAbleToRemoveIt() throws Exception {
        // when
        facade.removeSurveyTemplate(existingTemplateId);

        // then
        assertEmpty(dao.queryTemplates(new SurveyTemplateQuery()));

        // cleanup
        fixture.dontDeleteTemplate(existingTemplateId);
    }

    @Test(expectedExceptions = NotAllowedToPublishTemplatesInGroupException.class)
    public void notBeAbleToPublishItInAGroupHeDoesntBelongTo()
            throws Exception {
        // when
        groupFacade.assignSurveyTemplate(
                publishCmd(existingTemplateId, inaccessibleGroupId));
        // then
        expectException();
    }

    @Test(expectedExceptions = SurveyTemplateAlreadyExistsException.class)
    public void notBeAbleToCreateAnotherSurveyWithSameTitle() throws
            Exception {
        // when
        facade.createTemplate(command("a test template").
                withQuestions(openQuestion(
                "another open question that will be never created")).
                build());
        // then
        expectException();
    }

    @Test
    public void beAbleToUpdateItLeavingNameUnchanged() throws Exception {
        // when
        facade.updateTemplate(existingTemplateId, command(
                "a test template").withQuestions(openQuestion(
                "another version of the open question")).
                build());
        // then
        SurveyTemplateDto template = dao.getTemplate(existingTemplateId);
        // then
        assertTemplateName(template, "a test template");
        assertCollectionSize(template.getQuestions(), 1);
        assertQuestion(template, 0, QuestionTypeEnum.OPEN,
                "another version of the open question");
    }

    @Test
    public void beAbleToUpdateChangingItsName() throws Exception {
        // when
        facade.updateTemplate(existingTemplateId, command(
                "some test template").withQuestions(openQuestion(
                "another version of the open question")).
                build());
        // then
        SurveyTemplateDto template = dao.getTemplate(existingTemplateId);
        // then
        assertTemplateName(template, "some test template");
    }

    @Test(expectedExceptions = SurveyTemplateAlreadyExistsException.class)
    public void notBeAbleToChangeTheNameWhenAnotherTemplateWithSuchNameExists()
            throws Exception {
        final String existingTemplateName = "some template name";
        // given
        fixture.createTemplate(template(existingTemplateName).byUser(evaluator).
                withQuestions(openQuestion("some open question")));

        CreateSurveyTemplateCommand command = new CreateSurveyTemplateCommand();
        command.setName(existingTemplateName);
        // when
        facade.updateTemplate(existingTemplateId, command);
        // then
        expectException();
    }

    private void assertTemplateName(SurveyTemplateDto template,
            String expectedName) {
        assertEquals(template.getName(), expectedName);
    }

    private static PublishSurveyTemplateCommand publishCmd(
            Long surveyTemplateId, Long groupId) {
        PublishSurveyTemplateCommand command =
                new PublishSurveyTemplateCommand();
        command.getSurveyTemplateIds().add(surveyTemplateId.intValue());
        command.setGroupId(groupId);
        return command;
    }

    private void assertQuestion(SurveyTemplateDto template, int i,
            QuestionTypeEnum type, String questionText) {
        final QuestionTemplateDto question =
                template.getQuestions().get(i);
        assertEquals(question.getType(), type);
        assertEquals(question.getText(), questionText);
    }
}
