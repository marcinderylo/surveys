package org.adaptiveplatform.surveys.builders;

import org.adaptiveplatform.surveys.dto.AnswerTemplateDto;

public class AnswerBuilder {

    private AnswerTemplateDto answer = new AnswerTemplateDto();

    public AnswerBuilder(String text) {
        answer.setText(text);
    }

    public AnswerBuilder requiresComment() {
        answer.setRequiresComment(true);
        return this;
    }

    public AnswerBuilder disallowsOtherAnswers() {
        answer.setExcludesOtherAnswers(true);
        return this;
    }

    public AnswerTemplateDto build() {
        return answer;
    }

    public static AnswerBuilder answer(String text) {
        return new AnswerBuilder(text);
    }

}
