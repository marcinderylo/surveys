package org.adaptiveplatform.surveys.domain;

import java.util.List;

/**
 * Validates whether submitted answer is valid for given type of question.
 * @author Marcin Deryło
 */
public interface AnswerValidator {

        /**
         * Checks whether the question can be considered answered.
         * @param question
         * @return {@code true} if the question is answered, {@code false} otherwise
         */
        boolean isAnswered(AnsweredQuestion question);

        /**
         * Verifies whether given answer is correct for the question.
         * 
         * @param question
         * @param selectedAnswerIds
         * @param comment
         */
        void validateAnswer(QuestionTemplate question, List<Integer> selectedAnswerIds,
                String comment);
}
