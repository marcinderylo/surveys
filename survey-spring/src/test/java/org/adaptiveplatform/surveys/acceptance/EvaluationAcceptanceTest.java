package org.adaptiveplatform.surveys.acceptance;

import org.adaptiveplatform.surveys.ContainerEnabledTest;
import org.adaptiveplatform.surveys.application.EvaluationDao;
import org.adaptiveplatform.surveys.application.EvaluationFacade;
import org.adaptiveplatform.surveys.builders.CoreFixtureBuilder;
import org.adaptiveplatform.surveys.builders.SurveysFixtureCreator;
import org.adaptiveplatform.surveys.dto.*;
import org.adaptiveplatform.surveys.test.Asserts;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.adaptiveplatform.surveys.builders.CoreFixtureBuilder.EVALUATOR_EMAIL;
import static org.adaptiveplatform.surveys.builders.CoreFixtureBuilder.STUDENT_EMAIL;
import static org.adaptiveplatform.surveys.builders.GroupBuilder.group;
import static org.adaptiveplatform.surveys.builders.QuestionBuilder.openQuestion;
import static org.adaptiveplatform.surveys.builders.ResearchBuilder.research;
import static org.adaptiveplatform.surveys.builders.SurveyTemplateBuilder.template;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * @author Rafał Jamróz
 * @author Marcin Deryło
 */
public class EvaluationAcceptanceTest extends ContainerEnabledTest {

    @Resource
    private EvaluationDao dao;
    @Resource
    private EvaluationFacade facade;
    @Resource
    private SurveysFixtureCreator surveys;
    @Resource
    private CoreFixtureBuilder users;

    private Long researchId, filledSurveyId;

    @Before
    public void setupInitialData() {
        users.loginAsTeacher();
        Long groupId = surveys.createGroup(group("some test group").withEvaluator(EVALUATOR_EMAIL).withStudent(
                STUDENT_EMAIL));

        users.loginAsEvaluator();
        Long template1 = surveys.createTemplate(template("template1").withQuestions(openQuestion("question 1.1")));
        researchId = surveys.createResearch(research().withSurvey(template1).forGroup(groupId));

        users.loginAsStudent();
        filledSurveyId = surveys.fillAndSubmitSurvey(template1, groupId);

        users.loginAsEvaluator();
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
        QuestionEvaluationDto evaluationDto = getOnlyElement(research.getQuestions());
        assertEquals("foobar", evaluationDto.getComments());
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
        assertHasDefinedTags(getOnlyElement(research.getQuestions()), TAGS);

        Asserts.assertCollectionSize(research.getSubmittedSurveys(), 1);
        final FilledSurveyDto submittedSurvey = getOnlyElement(research.getSubmittedSurveys());
        assertHasTags(getOnlyElement(submittedSurvey.getQuestions()), TAGS);
    }

    @Test
    public void shouldAllowEvaluatorToChangeTags() throws Exception {
        // given
        final String INITIALLY_SET_TAG = "interesting!";
        final String[] FINALLY_SET_TAGS = new String[]{"bad", "good", "ugly"};
        final String[] ALL_TAGS = new String[]{"bad", "good", "ugly", "interesting!"};
        facade.tagAnswer(createTagCommand(INITIALLY_SET_TAG));
        // when
        facade.tagAnswer(createTagCommand(FINALLY_SET_TAGS));
        // then
        final ResearchDto research = dao.get(researchId);
        assertHasDefinedTags(getOnlyElement(research.getQuestions()), ALL_TAGS);

        Asserts.assertCollectionSize(research.getSubmittedSurveys(), 1);
        final FilledSurveyDto submittedSurvey = getOnlyElement(research.getSubmittedSurveys());
        assertHasTags(getOnlyElement(submittedSurvey.getQuestions()), FINALLY_SET_TAGS);
    }

    private void assertHasDefinedTags(QuestionEvaluationDto questionEvaluation, String... tags) {
        assertAreEqual(questionEvaluation.getDefinedTags(), tags);
    }

    private void assertHasTags(SurveyQuestionDto answeredQuestion, String... tags) {
        assertAreEqual(answeredQuestion.getTags(), tags);
    }

    private void assertAreEqual(Collection<String> actualStrings, String... expectedStrings) {
        assertThat(actualStrings).containsOnly((Object[]) expectedStrings);
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
