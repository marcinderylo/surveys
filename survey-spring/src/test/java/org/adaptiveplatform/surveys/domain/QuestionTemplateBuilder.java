package org.adaptiveplatform.surveys.domain;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.adaptiveplatform.surveys.dto.QuestionTemplateDto;
import org.adaptiveplatform.surveys.dto.QuestionTypeEnum;

public class QuestionTemplateBuilder {

        private String text;
        private QuestionType type;
        private List<AnswerTemplateBuilder> answers =
                new ArrayList<AnswerTemplateBuilder>();

        public QuestionTemplateBuilder(String text, QuestionType type) {
                this.text = text;
                this.type = type;
        }

        public QuestionTemplateBuilder withAnswers(
                AnswerTemplateBuilder... answers) {
                this.answers.addAll(asList(answers));
                return this;
        }

        public QuestionTemplate build() {
                QuestionTemplate question = new QuestionTemplate(text, text,
                        type);
                for (AnswerTemplateBuilder answer : answers) {
                        question.addAnswer(answer.getText(), answer.
                                getRequiresComment(),
                                answer.getDisallowsOtherAnswers());
                }
                return question;
        }

        public QuestionTemplateDto buildDto() {
                QuestionTemplateDto question = new QuestionTemplateDto();

                question.setText(text);
                question.setType(QuestionTypeEnum.valueOf(type.name()));
                question.setHtmlText(text);

                for (AnswerTemplateBuilder answerBuilder : answers) {
                        question.getAnswers().add(answerBuilder.buildDto());
                }

                return question;
        }

        public static QuestionTemplateBuilder question(String text,
                QuestionType type) {
                return new QuestionTemplateBuilder(text, type);
        }

        public static QuestionTemplateBuilder multipleChoiceQuestion(String text) {
                return new QuestionTemplateBuilder(text,
                        QuestionType.MULTIPLE_CHOICE);
        }

        public static QuestionTemplateBuilder singleChoiceQuestion(String text) {
                return new QuestionTemplateBuilder(text,
                        QuestionType.SINGLE_CHOICE);
        }

        public static QuestionTemplateBuilder openQuestion(String text) {
                return new QuestionTemplateBuilder(text, QuestionType.OPEN);
        }
}
