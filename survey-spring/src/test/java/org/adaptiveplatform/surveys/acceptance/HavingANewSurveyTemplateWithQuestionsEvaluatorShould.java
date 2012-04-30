package org.adaptiveplatform.surveys.acceptance;

import org.adaptiveplatform.surveys.ContainerEnabledTest;
import org.adaptiveplatform.surveys.application.SurveyDao;
import org.adaptiveplatform.surveys.application.SurveyFacade;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.builders.SurveysFixtureCreator;
import org.adaptiveplatform.surveys.dto.*;
import org.adaptiveplatform.surveys.exception.NotAllowedToPublishTemplatesInGroupException;
import org.adaptiveplatform.surveys.exception.SurveyTemplateAlreadyExistsException;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.adaptiveplatform.surveys.builders.AnswerBuilder.answer;
import static org.adaptiveplatform.surveys.builders.CoreFixtureBuilder.EVALUATOR_EMAIL;
import static org.adaptiveplatform.surveys.builders.GroupBuilder.group;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.*;
import static org.adaptiveplatform.surveys.builders.ResearchBuilder.research;
import static org.adaptiveplatform.surveys.builders.SurveyTemplateBuilder.template;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * Integration tests requiring an existing survey template and usual user test
 * data.
 *
 * @author Marcin Deryło
 */
public class HavingANewSurveyTemplateWithQuestionsEvaluatorShould extends ContainerEnabledTest {

    @Resource
    private SurveyFacade facade;
    @Resource
    private SurveyDao dao;
    @Resource
    private CoreFixtureBuilder users;
    @Resource
    private SurveysFixtureCreator surveys;

    private Long inaccessibleGroupId;
    private Long existingTemplateId;

    @Before
    public void beforeMethod() throws Exception {
        users.loginAsTeacher();
        surveys.createGroup(group("test group").withEvaluator(EVALUATOR_EMAIL));
        inaccessibleGroupId = surveys.createGroup(group("inaccessible group"));

        users.loginAsEvaluator();
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
        users.loginAs(EVALUATOR_EMAIL);
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

    private void assertQuestion(SurveyTemplateDto template, int i, QuestionTypeEnum type, String questionText) {
        final QuestionTemplateDto question = template.getQuestions().get(i);
        assertEquals(type, question.getType());
        assertEquals(questionText, question.getText());
    }
}
