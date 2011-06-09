package org.adaptiveplatform.surveys.domain;

import static org.adaptiveplatform.surveys.test.Asserts.expectException;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.PublishingTemplateWithoutQuestionsException;
import org.junit.Before;
import org.junit.Test;

public class SurveyTemplateTest {

	private QuestionTemplate question;
	private SurveyTemplate template;

	@Before
	public void setUp() {
		question = mock(QuestionTemplate.class);
		UserDto evaluator = new UserDto();

		template = new SurveyTemplate(evaluator, "title01");
	}

	@Test
	public void shouldAddQuestionsToTemplate() throws Exception {
		// when
		template.addQuestion(question);
		// then
		assertEquals(template.getTitle(), "title01");
		assertEquals(template.questionsCount(), 1);
	}

	@Test
	public void shouldPublishWithQuestions() throws Exception {
		// given
		template.addQuestion(question);
		// when
		template.publish();
	}

	@Test(expected = PublishingTemplateWithoutQuestionsException.class)
	public void cantPublishWithoutQuestions() throws Exception {
		// when
		template.publish();
		// then
		expectException();
	}
}
