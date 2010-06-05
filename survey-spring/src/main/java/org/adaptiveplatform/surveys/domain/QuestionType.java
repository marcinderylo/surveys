package org.adaptiveplatform.surveys.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.util.StringUtils;

/**
 * Supported types of survey questions.
 * 
 * @author Marcin Deryło
 */
public enum QuestionType implements QuestionValidator, AnswerValidator {

    /**
     * Question allowing only a single answer to be selected.
     */
    SINGLE_CHOICE {

        @Override
        public void validateQuestion(QuestionTemplate template) {
            Validate.isTrue(template.hasAnswers(),
                    "Single choice questions must have answers.");
            for (AnswerTemplate answer : template.getAnswers()) {
                Validate.isTrue(answer.disallowsOtherAnswers()
                        != null
                        && answer.disallowsOtherAnswers(),
                        "Each answers of single choice questions "
                        + "must disallow any other answers");
            }
        }

        @Override
        public boolean isAnswered(AnsweredQuestion question) {
            int selections = 0;
            boolean requiresComment = false;
            for (AnsweredQuestionAnswer answer :
                    question.getAnswers()) {
                if (answer.isSelected()) {
                    selections++;
                    requiresComment = answer.getAnswerTemplate().
                            requiresComment();
                }

            }
            if (selections > 1) {
                throw new IllegalStateException("More than one answer selected in "
                        + "single choice question!");
            }
            if (selections == 1) {
                if (requiresComment) {
                    return StringUtils.hasText(question.getComment());
                }
                return true;
            }
            return false;
        }

        @Override
        protected void checkAnswer(QuestionTemplate question,
                List<Integer> selectedAnswerNumbers, String comment) {
            Validate.isTrue(selectedAnswerNumbers == null || selectedAnswerNumbers.
                    size() <= 1,
                    "single choice question accepts only single answers");
        }
    },
    /**
     * Question allowing multiple answers to be selected.
     */
    MULTIPLE_CHOICE {

        @Override
        public void validateQuestion(QuestionTemplate template) {
            Validate.isTrue(template.hasAnswers(),
                    "Multiple choice questions must have answers.");
        }

        @Override
        public boolean isAnswered(AnsweredQuestion question) {
            List<AnsweredQuestionAnswer> selectedAnswers =
                    new ArrayList<AnsweredQuestionAnswer>();
            for (AnsweredQuestionAnswer answer :
                    question.getAnswers()) {
                if (answer.isSelected()) {
                    selectedAnswers.add(answer);
                }
            }
            if (selectedAnswers.isEmpty()) {
                return false;
            }
            for (AnsweredQuestionAnswer answer : selectedAnswers) {
                // multiple choice with other?
                if (answer.getAnswerTemplate().disallowsOtherAnswers()) {
                    if (selectedAnswers.size() > 1) {
                        throw new IllegalArgumentException(
                                "Selected answer of type 'other' but not alone");
                    }
                    if (answer.getAnswerTemplate().requiresComment()) {
                        return StringUtils.hasText(question.getComment());
                    }
                }
            }
            return true;
        }

        @Override
        protected void checkAnswer(QuestionTemplate question,
                List<Integer> selectedAnswerNumbers, String comment) {
            boolean excludingSelected = false;
            boolean regularSelected = false;
            for (int i = 0; i < question.getAnswers().size(); i++) {
                AnswerTemplate answer = question.getAnswers().get(i);
                int number = i + 1;
                if (selectedAnswerNumbers.contains(number)) {
                    if (answer.disallowsOtherAnswers()) {
                        excludingSelected = true;
                    } else {
                        regularSelected = true;
                    }
                }
            }
            for (int i = 0; i < question.getAnswers().size(); i++) {
                int number = i + 1;
                if (selectedAnswerNumbers.contains(number)) {
                    if (question.getAnswers().get(i).disallowsOtherAnswers()) {
                        excludingSelected = true;
                    } else {
                        regularSelected = true;
                    }
                }
            }
            if (excludingSelected && regularSelected) {
                throw new IllegalArgumentException("Answer requiring comment must be "
                        + "selected alone");
            }
        }
    },
    /**
     * Question allowing no answers to be selected but requiring a text comment
     * to be provided as answer.
     */
    OPEN {

        @Override
        public void validateQuestion(QuestionTemplate template) {
            Validate.isTrue(!template.hasAnswers(),
                    "Open questions can't have answers.");
        }

        @Override
        public boolean isAnswered(AnsweredQuestion question) {
            return StringUtils.hasText(question.getComment());
        }

        @Override
        protected void checkAnswer(QuestionTemplate question,
                List<Integer> selectedAnswerNumbers, String comment) {
            Validate.isTrue(selectedAnswerNumbers == null || selectedAnswerNumbers.
                    isEmpty(),
                    "Open question doesn't have answers");
        }
    };

    @Override
    public boolean isAnswered(AnsweredQuestion question) {
        return false;
    }

    @Override
    public abstract void validateQuestion(QuestionTemplate template);

    @Override
    public void validateAnswer(QuestionTemplate question,
            List<Integer> selectedAnswerIds, String comment) {
        if (!this.equals(question.getType())) {
            throw new IllegalArgumentException("Expected question of "
                    + "type " + this.name() + " but found: " + question.getType().
                    name());
        }
        checkAnswer(question, selectedAnswerIds, comment);
    }

    protected abstract void checkAnswer(QuestionTemplate question,
            List<Integer> selectedAnswerNumbers, String comment);
}
