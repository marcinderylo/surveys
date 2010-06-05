package org.adaptiveplatform.surveys.service;


import org.adaptiveplatform.surveys.domain.QuestionTemplate;
import org.adaptiveplatform.surveys.domain.QuestionType;
import org.adaptiveplatform.surveys.dto.AnswerTemplateDto;
import org.adaptiveplatform.surveys.dto.QuestionTemplateDto;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

@Service
public class QuestionTemplateFactory {

        public QuestionTemplate createQuestion(QuestionTemplateDto questionCmd) {
                QuestionType questionType = getQuestionType(questionCmd);

                QuestionTemplate template = new QuestionTemplate(questionCmd.
                        getText(), questionCmd.getHtmlText() ,questionType);
                template.setStyle(questionCmd.getStyle());

                for (AnswerTemplateDto answerCmd :
                        questionCmd.getAnswers()) {
                        boolean requiresComment = nullSafeGet(answerCmd.
                                getRequiresComment());
                        boolean disallowsOtherAnswers = nullSafeGet(
                                answerCmd.getExcludesOtherAnswers());
                        template.addAnswer(answerCmd.getText(),
                                requiresComment, disallowsOtherAnswers);
                }

                questionType.validateQuestion(template);

                return template;
        }

        private QuestionType getQuestionType(QuestionTemplateDto questionCmd) {
                Validate.notNull(questionCmd.getType(),
                        "Missing question type for question '" + questionCmd.
                        getText() + ".");
                String questionTypeName = questionCmd.getType().name();
                QuestionType questionType =
                        QuestionType.valueOf(questionTypeName);
                return questionType;
        }

        private boolean nullSafeGet(Boolean b) {
                return b == null ? Boolean.FALSE : b;
        }
}
