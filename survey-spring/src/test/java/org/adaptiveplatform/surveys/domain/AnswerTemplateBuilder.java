package org.adaptiveplatform.surveys.domain;

import org.adaptiveplatform.surveys.dto.AnswerTemplateDto;

public class AnswerTemplateBuilder {

    private final String text;
    private boolean requiresComment = false;
    private boolean disallowsOtherAnswers = false;

    public AnswerTemplateBuilder(String text) {
        this.text = text;
    }

    public AnswerTemplateBuilder requiresComment() {
        requiresComment = true;
        return disallowsOtherAnswers();
    }

    public AnswerTemplateBuilder disallowsOtherAnswers() {
        disallowsOtherAnswers = true;
        return this;
    }

    public AnswerTemplateBuilder allowsOtherAnswers() {
        disallowsOtherAnswers = false;
        return this;
    }

    public boolean getRequiresComment() {
        return requiresComment;
    }

    public boolean getDisallowsOtherAnswers() {
        return disallowsOtherAnswers;
    }

    public String getText() {
        return text;
    }

    public static AnswerTemplateBuilder answer(String text) {
        return new AnswerTemplateBuilder(text);
    }

    public AnswerTemplateDto buildDto() {
        AnswerTemplateDto answer = new AnswerTemplateDto();

        answer.setText(text);
        answer.setRequiresComment(requiresComment);
        answer.setExcludesOtherAnswers(disallowsOtherAnswers);

        return answer;
    }
}
