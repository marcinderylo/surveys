package org.adaptiveplatform.surveys.domain;

import java.util.ArrayList;
import java.util.List;

import org.adaptiveplatform.surveys.dto.QuestionTypeEnum;
import org.adaptiveplatform.surveys.exception.AtLeastOneAnswerRequiredException;
import org.adaptiveplatform.surveys.exception.MultipleAnswersForSingleChoiceQuestionException;
import org.adaptiveplatform.surveys.exception.OneOfSelectedAnswersDisallowsOthersException;
import org.adaptiveplatform.surveys.exception.OpenQuestionHaveNoAnswersException;
import org.adaptiveplatform.surveys.exception.SingleChoiceQuestionAnswersMustDisallowEachOtherException;
import org.springframework.util.CollectionUtils;
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
            if (!template.hasAnswers()) {
                throw new AtLeastOneAnswerRequiredException(QuestionTypeEnum.SINGLE_CHOICE);
            }
            for (int answerNo = 0; answerNo < template.getAnswers().size(); ++answerNo) {
                final AnswerTemplate answer = template.getAnswers().get(answerNo);
                if (!Boolean.TRUE.equals(answer.disallowsOtherAnswers())) {
                    throw new SingleChoiceQuestionAnswersMustDisallowEachOtherException(template.
                            getText(),
                            answerNo);
                }
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
                throw new MultipleAnswersForSingleChoiceQuestionException(question.
                        getQuestionTemplateId());
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
            if (selectedAnswerNumbers != null && selectedAnswerNumbers.size() > 1) {
                throw new MultipleAnswersForSingleChoiceQuestionException(question.getId());
            }
        }
    },
    /**
     * Question allowing multiple answers to be selected.
     */
    MULTIPLE_CHOICE {

        @Override
        public void validateQuestion(QuestionTemplate template) {
            if (!template.hasAnswers()) {
                throw new AtLeastOneAnswerRequiredException(QuestionTypeEnum.MULTIPLE_CHOICE);
            }
        }

        @Override
        public boolean isAnswered(
                AnsweredQuestion question) {
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
                        throw new OneOfSelectedAnswersDisallowsOthersException(question.getQuestionTemplateId());
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
                List<Integer> selectedAnswerNumbers,
                String comment) {
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
                throw new OneOfSelectedAnswersDisallowsOthersException(question.getId());
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
            if (template.hasAnswers()) {
                throw new OpenQuestionHaveNoAnswersException(template.getText());
            }
        }

        @Override
        public boolean isAnswered(AnsweredQuestion question) {
            return StringUtils.hasText(question.getComment());
        }

        @Override
        protected void checkAnswer(QuestionTemplate question,
                List<Integer> selectedAnswerNumbers,
                String comment) {
            if (!CollectionUtils.isEmpty(selectedAnswerNumbers)) {
                throw new OpenQuestionHaveNoAnswersException(question.getText());
            }
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
            // not changing to a business exception as this would be a developer's mistake
            throw new IllegalArgumentException("Expected question of "
                    + "type " + this.name() + " but found: " + question.getType().
                    name());
        }
        checkAnswer(question, selectedAnswerIds, comment);
    }

    protected abstract void checkAnswer(QuestionTemplate question,
            List<Integer> selectedAnswerNumbers, String comment);
}
