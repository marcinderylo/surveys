package org.adaptiveplatform.surveys.domain;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

public class AnsweredQuestionTest {

    @Test
    public void shouldAnswerQuestion() throws Exception {
        // given
        QuestionTemplate template = mock(QuestionTemplate.class);
        AnswerTemplate answer = mock(AnswerTemplate.class);

        List<AnswerTemplate> answerTemplates = Collections.singletonList(answer);

        AnsweredQuestion question = new AnsweredQuestion(template,
                answerTemplates);
        // when
        question.answer(Arrays.asList(1), null);
        // then
        assertTrue(question.isAnswerSelected(1));
    }
}
