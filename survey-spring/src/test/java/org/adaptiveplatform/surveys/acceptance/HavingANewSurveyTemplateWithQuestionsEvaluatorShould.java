package org.adaptiveplatform.surveys.acceptance;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.adaptiveplatform.surveys.builders.AnswerBuilder.answer;
import static org.adaptiveplatform.surveys.builders.GroupBuilder.group;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.multiChoiceQuestion;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.openQuestion;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.singleChoiceQuestion;
import static org.adaptiveplatform.surveys.builders.ResearchBuilder.research;
import static org.adaptiveplatform.surveys.builders.SurveyTemplateBuilder.template;
import static org.adaptiveplatform.surveys.builders.UserAccountBuilder.evaluator;
import static org.adaptiveplatform.surveys.builders.UserAccountBuilder.teacher;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.application.SurveyDao;
import org.adaptiveplatform.surveys.application.SurveyFacade;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.builders.SurveysFixtureBuilder;
import org.adaptiveplatform.surveys.dto.CreateSurveyTemplateCommand;
import org.adaptiveplatform.surveys.dto.PublishSurveyTemplateCommand;
import org.adaptiveplatform.surveys.dto.QuestionTemplateDto;
import org.adaptiveplatform.surveys.dto.QuestionTypeEnum;
import org.adaptiveplatform.surveys.dto.SurveyTemplateDto;
import org.adaptiveplatform.surveys.dto.SurveyTemplateQuery;
import org.adaptiveplatform.surveys.exception.NotAllowedToPublishTemplatesInGroupException;
import org.adaptiveplatform.surveys.exception.SurveyTemplateAlreadyExistsException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration tests requiring an existing survey template and usual user test
 * data.
 * 
 * @author Marcin Deryło
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/testConfigurationContext.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class HavingANewSurveyTemplateWithQuestionsEvaluatorShould {

    /**
     * Login (email) of a user with evaluator privileges.
     */
    public static final String EVALUATOR_LOGIN = "eval@eval.com";
    @Resource
    private SurveyFacade facade;
    @Resource
    private SurveyDao dao;
    @Resource
    private CoreFixtureBuilder users;
    @Resource
    private SurveysFixtureBuilder surveys;

    private Long groupId;
    private Long inaccessibleGroupId;
    private Long existingTemplateId;

    @Before
    public void beforeMethod() throws Exception {
        users.createUser(evaluator(EVALUATOR_LOGIN));
        users.createUser(teacher("teacher@umcs.com.pl"));

        users.loginAs("teacher@umcs.com.pl");
        groupId = surveys.createGroup(group("test group").withEvaluator(EVALUATOR_LOGIN));
        inaccessibleGroupId = surveys.createGroup(group("inaccessible group"));

        users.loginAs(EVALUATOR_LOGIN);
        existingTemplateId = surveys.createTemplate(template("a test template").withQuestions(
                /* multiple choice question (nr 1) */
                multiChoiceQuestion("multi-choice question").withAnswers(answer("answer 1"), answer("answer 2"),
                        answer("other: ...").requiresComment().disallowsOtherAnswers(),
                        answer("none of the above").disallowsOtherAnswers()),
                /* single choice question (nr 2) */
                singleChoiceQuestion("single-choice question").withAnswers(answer("option 1").disallowsOtherAnswers(),
                        answer("option 2").disallowsOtherAnswers(), answer("other: ...").requiresComment()),
                /* open question (nr 3) */
                openQuestion("open question")));
    }

    @Test
    public void beAbleToReadIt() throws Exception {
        // when
        SurveyTemplateDto template = dao.getTemplate(existingTemplateId);
        // then
        assertTemplateName(template, "a test template");
        assertThat(template.getQuestions()).hasSize(3);
        assertQuestion(template, 0, QuestionTypeEnum.MULTIPLE_CHOICE, "multi-choice question");
        assertQuestion(template, 1, QuestionTypeEnum.SINGLE_CHOICE, "single-choice question");
        assertQuestion(template, 2, QuestionTypeEnum.OPEN, "open question");
    }

    @Test
    public void beAbleToQueryHisTemplatesWithoutReceivingDetails() throws Exception {
        // when
        List<SurveyTemplateDto> templates = dao.queryTemplates(new SurveyTemplateQuery());
        // then
        assertThat(getOnlyElement(templates).getQuestions()).isEmpty();
    }

    @Test
    public void beAbleToRemoveIt() throws Exception {
        // when
        facade.removeSurveyTemplate(existingTemplateId);
        // then
        assertThat(dao.queryTemplates(new SurveyTemplateQuery())).isEmpty();
    }

    @Test(expected = NotAllowedToPublishTemplatesInGroupException.class)
    public void notBeAbleToPublishItInAGroupHeDoesntBelongTo() throws Exception {
        // when
        surveys.createResearch(research().withSurvey(existingTemplateId).forGroup(inaccessibleGroupId));
        // then - exception should be thrown
    }

    @Test(expected = SurveyTemplateAlreadyExistsException.class)
    public void notBeAbleToCreateAnotherSurveyWithSameTitle() throws Exception {
        // when
        facade.createTemplate(template("a test template").withQuestions(
                openQuestion("another open question that will be never created")).build());
        // then - exception should be thrown
    }

    @Test
    public void beAbleToUpdateItLeavingNameUnchanged() throws Exception {
        // when
        facade.updateTemplate(existingTemplateId,
                template("a test template").withQuestions(openQuestion("another version of the open question"))
                        .withDescription("changed description").build());
        // then
        SurveyTemplateDto template = dao.getTemplate(existingTemplateId);
        // then
        assertTemplateName(template, "a test template");
        assertThat(template.getQuestions()).hasSize(1);
        assertQuestion(template, 0, QuestionTypeEnum.OPEN, "another version of the open question");
        assertEquals(template.getDescription(), "changed description");
    }

    @Test
    public void beAbleToUpdateChangingItsName() throws Exception {
        // when
        facade.updateTemplate(existingTemplateId,
                template("some test template").withQuestions(openQuestion("another version of the open question"))
                        .build());
        // then
        SurveyTemplateDto template = dao.getTemplate(existingTemplateId);
        // then
        assertTemplateName(template, "some test template");
    }

    @Test(expected = SurveyTemplateAlreadyExistsException.class)
    public void notBeAbleToChangeTheNameWhenAnotherTemplateWithSuchNameExists() throws Exception {
        final String existingTemplateName = "some template name";
        // given
        users.loginAs(EVALUATOR_LOGIN);
        surveys.createTemplate(template(existingTemplateName).withQuestions(openQuestion("some open question")));

        CreateSurveyTemplateCommand command = new CreateSurveyTemplateCommand();
        command.setName(existingTemplateName);
        // when
        facade.updateTemplate(existingTemplateId, command);
        // then - exception should be thrown
    }

    private void assertTemplateName(SurveyTemplateDto template, String expectedName) {
        assertEquals(template.getName(), expectedName);
    }

    private static PublishSurveyTemplateCommand publishCmd(Long surveyTemplateId, Long groupId) {
        PublishSurveyTemplateCommand command = new PublishSurveyTemplateCommand();
        command.getSurveyTemplateIds().add(surveyTemplateId.intValue());
        command.setGroupId(groupId);
        return command;
    }

    private void assertQuestion(SurveyTemplateDto template, int i, QuestionTypeEnum type, String questionText) {
        final QuestionTemplateDto question = template.getQuestions().get(i);
        assertEquals(type, question.getType());
        assertEquals(questionText, question.getText());
    }
}
