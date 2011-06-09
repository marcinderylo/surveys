package org.adaptiveplatform.surveys.domain;

import static java.util.Arrays.asList;
import static org.adaptiveplatform.surveys.test.Asserts.expectException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.InvalidAnswerNumbersException;
import org.adaptiveplatform.surveys.exception.SurveyNotFilledCompletelyException;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

// TODO rewrite this test to avoid all those mocks
public class FilledSurveyTest {

	/**
	 * id of existing question
	 */
	private static final int QUESTION_NUMBER = 1;
	private SurveyPublication publication;
	private QuestionTemplate questionMock;
	private FilledSurvey survey;

	@Before
	public void beforeMethod() throws Exception {
		publication = mock(SurveyPublication.class, RETURNS_DEEP_STUBS);
		// FIXME won't work if we add tests for checking filling availability
		when(publication.isFillingEnabled(any(DateTime.class))).thenReturn(Boolean.TRUE);
		questionMock = mock(QuestionTemplate.class);

		when(publication.getSurveyTemplate().getQuestion(QUESTION_NUMBER)).thenReturn(questionMock);

		when(publication.getSurveyTemplate().getQuestion(anyInt())).thenThrow(new IllegalArgumentException());

		when(publication.getSurveyTemplate().getQuestions()).thenReturn(Arrays.asList(questionMock));
	}

	@Test
	public void shouldNewSurveyNotBeStarted() throws Exception {
		// given
		survey = new FilledSurvey(publication, new UserDto());
		// then
		assertFalse(survey.isStarted());
	}

	@Test
	public void shouldNewSurveyNotBeFinished() throws Exception {
		// given
		survey = new FilledSurvey(publication, new UserDto());
		// then
		assertFalse(survey.isFinished());
	}

	@Test
	public void shouldAnswerQuestion() throws Exception {
		// given
		addAnswers(1);
		survey = new FilledSurvey(publication, new UserDto());
		survey.startFilling();
		// when
		survey.answerQuestion(1, asList(1), null);
		// then
		assertTrue(survey.isAnswerSelected(1, 1));
	}

	@Test(expected = IllegalStateException.class)
	public void cantAnswerNotStartedSurvey() throws Exception {
		// given
		addAnswers(1);
		survey = new FilledSurvey(publication, new UserDto());
		// when
		survey.answerQuestion(1, asList(1), null);
		// then - exception should be thrown
	}

	@Test
	public void shouldBeStartedAfterStarting() throws Exception {
		// given
		addAnswers();
		survey = new FilledSurvey(publication, new UserDto());
		// when
		survey.startFilling();
		// then
		assertTrue(survey.isStarted());
	}

	@Test
	public void shouldSubmitSurveyWhenAllQuestionsAreAnswered() throws Exception {
		// given
		addAnswers();
		survey = new FilledSurvey(publication, new UserDto());
		when(questionMock.isAnswered(any(AnsweredQuestion.class))).thenReturn(Boolean.TRUE);
		// when
		survey.submit();
		// then
		assertTrue(survey.isFinished());
	}

	@Test(expected = SurveyNotFilledCompletelyException.class)
	public void cantSubmitSurveyWhenNotAllQuestionsAreAnswered() throws Exception {
		// given
		addAnswers();
		survey = new FilledSurvey(publication, new UserDto());
		when(questionMock.isAnswered(any(AnsweredQuestion.class))).thenReturn(Boolean.FALSE);
		// when
		survey.submit();
		// then
		assertTrue(survey.isFinished());
	}

	@Test
	public void shouldAcceptNullAsAnswers() throws Exception {
		// given
		addAnswers();
		survey = new FilledSurvey(publication, new UserDto());
		survey.startFilling();
		// when
		survey.answerQuestion(QUESTION_NUMBER, null, "aslfjkaslf");
		// then
	}

	@Test(expected = IllegalStateException.class)
	public void cantChangeAnswersAfterSubmitting() throws Exception {
		// given
		addAnswers(1);
		survey = new FilledSurvey(publication, new UserDto());
		survey.answerQuestion(QUESTION_NUMBER, asList(1), null);
		survey.submit();
		// when
		survey.answerQuestion(QUESTION_NUMBER, asList(1), null);
		// then
		expectException();
	}

	@Test(expected = IllegalArgumentException.class)
	public void cantAnswerQuestionNotInSurvey() throws Exception {
		// given
		addAnswers(1);
		survey = new FilledSurvey(publication, new UserDto());
		survey.startFilling();
		// when
		survey.answerQuestion(23589, asList(1), null);
		// then
		expectException();
	}

	@Test(expected = InvalidAnswerNumbersException.class)
	public void cantSelectAnswerNotInQuestion() throws Exception {
		// given
		addAnswers();
		survey = new FilledSurvey(publication, new UserDto());
		survey.startFilling();
		// when
		survey.answerQuestion(QUESTION_NUMBER, asList(1), null);
		// then
		expectException();
	}

	@Test
	public void shouldAnswerBeFalseIfNotAnswered() throws Exception {
		// given
		addAnswers(1);
		survey = new FilledSurvey(publication, new UserDto());
		assertFalse(survey.isAnswerSelected(QUESTION_NUMBER, 1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void cantGetAnswerForQuestionThatDoesntExist() throws Exception {
		// given
		addAnswers();
		survey = new FilledSurvey(publication, new UserDto());
		// when
		survey.isAnswerSelected(23423, 12334);
		// then
		expectException();
	}

	@Test(expected = IllegalArgumentException.class)
	public void cantGetAnswerForQuestionThatDoesntHaveThatAnswer() throws Exception {
		// given
		addAnswers();
		survey = new FilledSurvey(publication, new UserDto());
		// when
		survey.isAnswerSelected(QUESTION_NUMBER, 23045);
		// then
		expectException();
	}

	@Test
	public void shouldReplaceSelectedAnswerWhenAnotherAnswerSelected() throws Exception {
		// given

		addAnswers(1, 2);
		survey = new FilledSurvey(publication, new UserDto());
		survey.startFilling();
		survey.answerQuestion(QUESTION_NUMBER, asList(1), null);
		// when
		survey.answerQuestion(QUESTION_NUMBER, asList(2), null);
		// then
		assertTrue(survey.isAnswerSelected(QUESTION_NUMBER, 2));
		assertFalse(survey.isAnswerSelected(QUESTION_NUMBER, 1));
	}

	@Test
	public void shouldDeselectAnswer() throws Exception {
		// given
		addAnswers(1);
		survey = new FilledSurvey(publication, new UserDto());
		survey.startFilling();
		survey.answerQuestion(1, asList(1), null);
		// when
		survey.answerQuestion(1, Collections.EMPTY_LIST, null);
		// then
		assertFalse(survey.isAnswerSelected(1, 1));
	}

	@Test
	public void shouldGiveMultipleAnswers() throws Exception {
		// given
		addAnswers(1, 2, 3);
		survey = new FilledSurvey(publication, new UserDto());
		survey.startFilling();
		// when
		survey.answerQuestion(1, asList(1, 2, 3), "sadfjsojosdnf");
		// then
		assertTrue(survey.isAnswerSelected(QUESTION_NUMBER, 1));
		assertTrue(survey.isAnswerSelected(QUESTION_NUMBER, 2));
		assertTrue(survey.isAnswerSelected(QUESTION_NUMBER, 3));
	}

	@Test
	public void shouldAnswerQuestionWithComment() throws Exception {
		// given
		addAnswers(1);
		survey = new FilledSurvey(publication, new UserDto());
		survey.startFilling();
		// when
		survey.answerQuestion(1, asList(1), "some comment");
		// then
		assertEquals("some comment", survey.getAnsweredQuestion(1).getComment());
	}

	@Test
	public void shouldReturnQuestionEvenIfItWasntAnswered() throws Exception {
		// given
		addAnswers(1);
		survey = new FilledSurvey(publication, new UserDto());
		// when
		AnsweredQuestion question = survey.getAnsweredQuestion(QUESTION_NUMBER);
		// then
		assertFalse(question.isAnswerSelected(1));
	}

	/**
	 * TODO refactor to facilitate adding answers using local IDs
	 */
	private void addAnswers(Integer... answers) {
		reset(questionMock);

		List<AnswerTemplate> answerTemplates = new ArrayList<AnswerTemplate>();
		for (Integer answer : answers) {
			when(questionMock.hasAnswer(answer)).thenReturn(true);
			AnswerTemplate answerMock = mock(AnswerTemplate.class);
			when(questionMock.getAnswer(answer)).thenReturn(answerMock);
			answerTemplates.add(answerMock);
		}
		AnsweredQuestion answeredQuestion = new AnsweredQuestion(questionMock, answerTemplates);
		when(questionMock.answeredQuestion()).thenReturn(answeredQuestion);
	}
}
