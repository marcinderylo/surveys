package org.adaptiveplatform.surveys.domain;


import static org.adaptiveplatform.surveys.test.Asserts.expectException;
import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;

import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.exception.PublishingTemplateWithoutQuestionsException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SurveyTemplateTest {

    private QuestionTemplate question;
    private SurveyTemplate template;

    @BeforeMethod
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

    @Test(expectedExceptions = PublishingTemplateWithoutQuestionsException.class)
    public void cantPublishWithoutQuestions() throws Exception {
        // when
        template.publish();
        // then
        expectException();
    }
}
