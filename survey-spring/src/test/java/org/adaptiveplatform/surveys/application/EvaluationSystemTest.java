package org.adaptiveplatform.surveys.application;

import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.openQuestion;
import static org.adaptiveplatform.surveys.domain.ResearchBuilder.research;
import static org.adaptiveplatform.surveys.domain.SurveyTemplateBuilder.template;
import static org.adaptiveplatform.surveys.domain.UserAccountBuilder.user;
import static org.adaptiveplatform.surveys.utils.Collections42.firstOf;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertEqualsNoOrder;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Resource;

import org.adaptiveplatform.surveys.domain.CoreTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.ResearchTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.Role;
import org.adaptiveplatform.surveys.domain.StudentGroup;
import org.adaptiveplatform.surveys.domain.StudentGroupBuilder;
import org.adaptiveplatform.surveys.domain.StudentGroupTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.SurveyPublication;
import org.adaptiveplatform.surveys.domain.SurveyTemplate;
import org.adaptiveplatform.surveys.domain.SurveyTestFixtureBuilder;
import org.adaptiveplatform.surveys.domain.UserAccount;
import org.adaptiveplatform.surveys.dto.CommentQuestionCommand;
import org.adaptiveplatform.surveys.dto.FilledSurveyDto;
import org.adaptiveplatform.surveys.dto.QuestionEvaluationDto;
import org.adaptiveplatform.surveys.dto.ResearchDto;
import org.adaptiveplatform.surveys.dto.SurveyQuestionDto;
import org.adaptiveplatform.surveys.dto.TagAnswerCommand;
import org.adaptiveplatform.surveys.service.AuthenticationServiceMock;
import org.adaptiveplatform.surveys.test.Asserts;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Marcin Deryło
 */
@ContextConfiguration("classpath:/testConfigurationContext.xml")
public class EvaluationSystemTest extends AbstractTestNGSpringContextTests {

    @Resource
    private EvaluationDao dao;
    @Resource
    private EvaluationFacade facade;
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
    private Long researchId, filledSurveyId;

    @BeforeMethod
    public void init() {
        final UserAccount teacher = userFixture.createUser(user("teacher",
                "teacher",
                "teacher@adapt.com", Role.TEACHER));
        final UserAccount evaluator = userFixture.createUser(user("eval", "eval",
                "eval1@adapt.com", Role.EVALUATOR));
        final UserAccount student = userFixture.createUser(user("student",
                "student", "student@adapt.com", Role.USER));
        final StudentGroup group =
                groupFixture.createGroup(StudentGroupBuilder.group("some test group",
                teacher).withEvaluators(evaluator).withStudents(student));

        final SurveyTemplate template1 =
                surveyFixture.createTemplate(template("template1").byUser(
                evaluator).
                withQuestions(openQuestion("question 1.1")));
        final SurveyPublication publication =
                surveyFixture.publishTemplate(template1, group);
        researchId = researchFixture.createResearch(research("Test research",
                template1)).getId();
        researchFixture.addPublicationToResearch(researchId, publication.getId());

        filledSurveyId = surveyFixture.fillSurvey(template1.getId(),
                group.getId(), student).getId();

        authentication.authenticate(evaluator.getId(), "eval1@adapt.com",
                Role.EVALUATOR);
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
    public void shouldAllowToSetEvaluatorCommentsOnQuestions() throws Exception {
        // given
        CommentQuestionCommand cmd = new CommentQuestionCommand();
        cmd.setComment("foobar");
        cmd.setQuestionId(1);
        cmd.setResearchId(researchId);

        // when
        facade.commentQuestion(cmd);

        // then
        final ResearchDto research = dao.get(researchId);
        QuestionEvaluationDto evaluationDto = firstOf(research.getQuestions());
        assertEquals(evaluationDto.getComments(), "foobar");
    }

    @Test
    public void shouldFetchSubmittedSurveysForResearch() throws Exception {
        final ResearchDto research = dao.get(researchId);
        Asserts.assertCollectionSize(research.getSubmittedSurveys(), 1);
    }

    @Test
    public void shouldAllowToTagAnsweredQuestions() throws Exception {
        final String[] TAGS = new String[]{"interesting!", "boring:/"};
        // when
        facade.tagAnswer(createTagCommand(TAGS));
        // then
        final ResearchDto research = dao.get(researchId);
        assertHasDefinedTags(firstOf(research.getQuestions()), TAGS);

        Asserts.assertCollectionSize(research.getSubmittedSurveys(), 1);
        final FilledSurveyDto submittedSurvey =
                firstOf(research.getSubmittedSurveys());
        assertHasTags(firstOf(submittedSurvey.getQuestions()), TAGS);
    }

    @Test
    public void shouldAllowEvaluatorToChangeTags() throws Exception {
        // given
        final String INITIALLY_SET_TAG = "interesting!";
        final String[] FINALLY_SET_TAGS = new String[]{"bad", "good", "ugly"};
        final String[] ALL_TAGS = new String[]{"bad", "good", "ugly",
            "interesting!"};
        facade.tagAnswer(createTagCommand(INITIALLY_SET_TAG));
        // when
        facade.tagAnswer(createTagCommand(FINALLY_SET_TAGS));
        // then
        final ResearchDto research = dao.get(researchId);
        assertHasDefinedTags(firstOf(research.getQuestions()), ALL_TAGS);

        Asserts.assertCollectionSize(research.getSubmittedSurveys(), 1);
        final FilledSurveyDto submittedSurvey =
                firstOf(research.getSubmittedSurveys());
        assertHasTags(firstOf(submittedSurvey.getQuestions()), FINALLY_SET_TAGS);
    }

    private void assertHasDefinedTags(QuestionEvaluationDto questionEvaluation,
            String... tags) {
        assertAreEqual(questionEvaluation.getDefinedTags(), tags);
    }

    private void assertHasTags(SurveyQuestionDto answeredQuestion,
            String... tags) {
        assertAreEqual(answeredQuestion.getTags(), tags);
    }

    private void assertAreEqual(Collection<String> actualStrings, String... expectedStrings) {
        assertEqualsNoOrder(actualStrings.toArray(), expectedStrings);
    }

    private TagAnswerCommand createTagCommand(String... tags) {
        TagAnswerCommand cmd = new TagAnswerCommand();
        cmd.setFilledSurveyId(filledSurveyId);
        cmd.setQuestionNumber(1);
        cmd.setResearchId(researchId);
        cmd.getSetTags().addAll(Arrays.asList(tags));
        return cmd;
    }
}
