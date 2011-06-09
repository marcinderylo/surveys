package org.adaptiveplatform.surveys.service;

import static org.adaptiveplatform.surveys.domain.AnswerTemplateBuilder.answer;
import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.multipleChoiceQuestion;
import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.openQuestion;
import static org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder.singleChoiceQuestion;
import static org.adaptiveplatform.surveys.test.Asserts.expectException;
import static org.junit.Assert.assertEquals;

import org.adaptiveplatform.surveys.domain.QuestionTemplate;
import org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder;
import org.adaptiveplatform.surveys.domain.QuestionType;
import org.adaptiveplatform.surveys.exception.AnswerWithCommentAllowingOtherAnswersException;
import org.adaptiveplatform.surveys.exception.AtLeastOneAnswerRequiredException;
import org.adaptiveplatform.surveys.exception.OpenQuestionHaveNoAnswersException;
import org.adaptiveplatform.surveys.exception.SingleChoiceQuestionAnswersMustDisallowEachOtherException;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Marcin Deryło
 */
public class AQuestionTemplateFactoryShould {

	private QuestionTemplateFactory factory;
	private QuestionTemplate question;

	@Before
	public void init() {
		factory = new QuestionTemplateFactory();
		question = null;
	}

	@Test
	public void createOpenQuestionWithoutAnswers() throws Exception {
		// given
		whenCreatingAQuestion(openQuestion("Open question?"));
		expectOpenQuestionCreated("Open question?");
	}

	@Test
	public void createSingleChoiceQuestionWithAnswers() throws Exception {
		// given
		whenCreatingAQuestion(singleChoiceQuestion("Single choice question?").withAnswers(
				answer("answer 01").disallowsOtherAnswers()));
		expectSingleChoiceQuestionCreated("Single choice question?");
	}

	@Test(expected = SingleChoiceQuestionAnswersMustDisallowEachOtherException.class)
	public void notCreateSingleChoiceQuestionIfAnyAnswerAllowsOtherAnswersToBeSelected() throws Exception {
		whenCreatingAQuestion(singleChoiceQuestion("Single choice question?").withAnswers(
				answer("answer 01").allowsOtherAnswers()));
		expectThatCreationFails();
	}

	@Test
	public void createMultipleChoiceQuestionWithAnswerThatRequiresComment() throws Exception {
		whenCreatingAQuestion(multipleChoiceQuestion("Multiple choice question?").withAnswers(
				answer("answer 01").requiresComment().disallowsOtherAnswers()));
		expectMultipleChoiceQuestionCreated("Multiple choice question?");
	}

	@Test(expected = AnswerWithCommentAllowingOtherAnswersException.class)
	public void notAllowAnswersRequiringCommentThatAllowOtherAnswers() throws Exception {
		whenCreatingAQuestion(multipleChoiceQuestion("multiple choice question?").withAnswers(
				answer("answer 01").requiresComment().allowsOtherAnswers()));
		expectThatCreationFails();
	}

	@Test
	public void createMultipleChoiceQuestionWithAnswers() throws Exception {
		whenCreatingAQuestion(multipleChoiceQuestion("Multiple choice question?").withAnswers(answer("answer 01")));
		expectMultipleChoiceQuestionCreated("Multiple choice question?");
	}

	@Test(expected = OpenQuestionHaveNoAnswersException.class)
	public void notCreateOpenQuestionWithAnswers() throws Exception {
		whenCreatingAQuestion(openQuestion("Open question?").withAnswers(answer("answer 01")));
		expectThatCreationFails();
	}

	@Test(expected = AtLeastOneAnswerRequiredException.class)
	public void notCreateSingleChoiceQuestionWithoutAnswers() throws Exception {
		whenCreatingAQuestion(singleChoiceQuestion("single choice question"));
		expectThatCreationFails();
	}

	@Test(expected = AtLeastOneAnswerRequiredException.class)
	public void notCreateMultipleChoiceQuestionWithoutAnswers() throws Exception {
		whenCreatingAQuestion(multipleChoiceQuestion("multiple choice question"));
		expectThatCreationFails();
	}

	private void expectSingleChoiceQuestionCreated(String expectedText) {
		assertQuestionType(question, expectedText, QuestionType.SINGLE_CHOICE);
	}

	private void expectMultipleChoiceQuestionCreated(String expectedText) {
		assertQuestionType(question, expectedText, QuestionType.MULTIPLE_CHOICE);
	}

	private void expectOpenQuestionCreated(String expectedText) {
		assertQuestionType(question, expectedText, QuestionType.OPEN);
	}

	private void assertQuestionType(QuestionTemplate question, String expectedText, QuestionType expectedType) {
		assertEquals(expectedText, question.getText());
		assertEquals(expectedType, question.getType());
	}

	private void whenCreatingAQuestion(QuestionTemplateBuilder command) {
		question = factory.createQuestion(command.buildDto());
	}

	private void expectThatCreationFails() {
		expectException();
	}
}
