package org.adaptiveplatform.surveys.builders;

import org.adaptiveplatform.surveys.dto.AnswerTemplateDto;
import org.adaptiveplatform.surveys.dto.QuestionTemplateDto;
import org.adaptiveplatform.surveys.dto.QuestionTypeEnum;

public class QuestionBuilder {
    private QuestionTemplateDto question = new QuestionTemplateDto();

    public QuestionBuilder(QuestionTypeEnum type, String text) {
        question.setType(type);
        question.setText(text);
    }

    public QuestionBuilder withAnswers(AnswerBuilder... answers) {
        for (AnswerBuilder answerBuilder : answers) {
            question.getAnswers().add(answerBuilder.build());
        }
        return this;
    }

    public QuestionTemplateDto build() {
        if (question.getType() == QuestionTypeEnum.SINGLE_CHOICE) {
            for (AnswerTemplateDto answer : question.getAnswers()) {
                answer.setExcludesOtherAnswers(true);
            }
        }
        return question;
    }

    public static QuestionBuilder singleChoiceQuestion(String text) {
        return new QuestionBuilder(QuestionTypeEnum.SINGLE_CHOICE, text);
    }

    public static QuestionBuilder multiChoiceQuestion(String text) {
        return new QuestionBuilder(QuestionTypeEnum.MULTIPLE_CHOICE, text);
    }

    public static QuestionBuilder openQuestion(String text) {
        return new QuestionBuilder(QuestionTypeEnum.OPEN, text);
    }

}
