package org.adaptiveplatform.surveys.domain;

import static org.adaptiveplatform.surveys.domain.AnswerTemplateBuilder.answer;
import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.multipleChoiceQuestion;
import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.openQuestion;
import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.singleChoiceQuestion;
import static org.adaptiveplatform.surveys.domain.SurveyTemplateBuilder.template;
import static org.adaptiveplatform.surveys.test.Asserts.expectException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.MultipleAnswersForSingleChoiceQuestionException;
import org.adaptiveplatform.surveys.exception.OneOfSelectedAnswersDisallowsOthersException;
import org.adaptiveplatform.surveys.exception.SurveyNotFilledCompletelyException;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Marcin Deryło
 */
public class GivenANewFilledSurveyUserShould {
	@SuppressWarnings("unchecked")
	private List<Integer> EMPTY_LIST = Collections.EMPTY_LIST;
	private FilledSurvey survey;

	@Before
	public void prepareSurvey() {
		UserAccount account = new UserAccount("test", "test@test.com");
		account.addPrivilege(UserPrivilege.EVALUATOR);

		SurveyTemplate template = template("a test template")
				.withQuestions(
						/* multiple choice question (nr 1) */
						multipleChoiceQuestion("multi-choice question").withAnswers(answer("answer 1"),
								answer("answer 2"), answer("other: ...").requiresComment(),
								answer("none of the above").disallowsOtherAnswers()),
						/* single choice question (nr 2) */
						singleChoiceQuestion("single-choice question").withAnswers(
								answer("option 1").disallowsOtherAnswers(), answer("option 2").disallowsOtherAnswers(),
								answer("other: ...").requiresComment()),
						/* open question (nr 3) */
						openQuestion("open question")).byUser("test@test.com").build();
		SurveyPublication publication = mock(SurveyPublication.class);
		when(publication.getSurveyTemplate()).thenReturn(template);
		when(publication.isFillingEnabled(any(DateTime.class))).thenReturn(Boolean.TRUE);

		survey = new FilledSurvey(publication, mock(UserDto.class));
		survey.startFilling();
	}

	@Test
	public void beAbleToSelectMoreThanOneAnswerInMultichoiceQuestion() throws Exception {
		// when
		survey.answerQuestion(1, Arrays.asList(1, 2), null);
		// then
		assertTrue(survey.isAnswerSelected(1, 1));
		assertTrue(survey.isAnswerSelected(1, 2));
		assertFalse(survey.isAnswerSelected(1, 3));
		assertFalse(survey.isAnswerSelected(1, 4));
	}

	@Test
	public void beAbleAnswerASingleChoiceQuestionWithASingleAnswer() throws Exception {
		// when
		survey.answerQuestion(2, Arrays.asList(1), null);
		// then
		assertTrue(survey.isAnswerSelected(2, 1));
		assertFalse(survey.isAnswerSelected(2, 2));
		assertFalse(survey.isAnswerSelected(2, 3));
	}

	@Test(expected = OneOfSelectedAnswersDisallowsOthersException.class)
	public void notBeAbleToSelectAnyOtherAnswerIfSelectedAnAnswerThatRequiresComment() throws Exception {
		// when
		survey.answerQuestion(1, Arrays.asList(2, 3), null);
		// then
		expectException();
	}

	@Test(expected = OneOfSelectedAnswersDisallowsOthersException.class)
	public void notBeAbleToSelectAnyOtherAnswerIfSelectedAnAnswerThatDisallowsIt() throws Exception {
		// when
		survey.answerQuestion(1, Arrays.asList(1, 4), null);
		// then
		expectException();
	}

	@Test(expected = MultipleAnswersForSingleChoiceQuestionException.class)
	public void notBeAbleToAnswerSingleChoiceQuestionWithMultipleAnswers() throws Exception {
		// when
		survey.answerQuestion(2, Arrays.asList(1, 2), null);

		// then
		expectException();
	}

	@Test
	public void selectAnswersOfTypeNoneOfTheAbove() throws Exception {
		// when
		survey.answerQuestion(1, Collections.singletonList(4), null);
		// then
		assertFalse(survey.isAnswerSelected(1, 1));
		assertFalse(survey.isAnswerSelected(1, 2));
		assertFalse(survey.isAnswerSelected(1, 3));
		assertTrue(survey.isAnswerSelected(1, 4));
	}

	@Test(expected = OneOfSelectedAnswersDisallowsOthersException.class)
	public void notSelectAnswerOfTypeNoneOfTheAboveWithAnyOtherAnswer() throws Exception {
		// when
		survey.answerQuestion(1, Arrays.asList(1, 4), null);
		// then
		expectException();
	}

	@Test
	public void submitFullyFilledSurvey() throws Exception {
		// given
		survey.answerQuestion(1, Arrays.asList(1, 2), null);
		survey.answerQuestion(2, Arrays.asList(1), null);
		survey.answerQuestion(3, EMPTY_LIST, "some answer");

		// when
		survey.submit();
		// then
		assertTrue(survey.isFinished());
	}

	@Test(expected = SurveyNotFilledCompletelyException.class)
	public void notBeAbleToSubmitWithoutAnsweringAnOpenQuestion() throws Exception {
		// given
		survey.answerQuestion(1, Arrays.asList(1), null);
		survey.answerQuestion(2, Arrays.asList(1), null);
		// not answering question ID=3

		// when
		survey.submit();

		// then
		expectException();
	}

	@Test(expected = SurveyNotFilledCompletelyException.class)
	public void notBeAbleToSubmitWithoutAnsweringASingleChoiceQuestion() throws Exception {
		// given
		survey.answerQuestion(1, Arrays.asList(1, 2), null);
		// not answering question ID=2
		survey.answerQuestion(3, EMPTY_LIST, "some answer");

		// when
		survey.submit();

		// then
		expectException();
	}

	@Test(expected = SurveyNotFilledCompletelyException.class)
	public void notBeAbleToSubmitWithoutAnsweringAMultipleChoiceQuestion() throws Exception {
		// given
		// not answering question ID=1
		survey.answerQuestion(2, Arrays.asList(1), null);
		survey.answerQuestion(3, EMPTY_LIST, "some answer");

		// when
		survey.submit();
		// then
		expectException();
	}

	@Test(expected = SurveyNotFilledCompletelyException.class)
	public void notBeAbleToSubmitWithoutProvidingCommentToASingleChoiceQuestionIfRequired() throws Exception {
		// given
		survey.answerQuestion(2, Arrays.asList(3), " ");
		survey.answerQuestion(1, Arrays.asList(1), null);
		survey.answerQuestion(3, EMPTY_LIST, "some answer");
		// when
		survey.submit();
		// then
		expectException();
	}

	@Test(expected = SurveyNotFilledCompletelyException.class)
	public void notBeAbleToSubmitWithoutProvidingCommentToAMultipleChoiceQuestionIfRequired() throws Exception {
		// given
		survey.answerQuestion(1, Arrays.asList(3), null);
		survey.answerQuestion(2, Arrays.asList(1), null);
		survey.answerQuestion(3, EMPTY_LIST, "some answer");
		// when
		survey.submit();
		// then
		expectException();
	}
}
