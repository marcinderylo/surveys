package org.adaptiveplatform.surveys.builders;

import org.adaptiveplatform.surveys.dto.CreateSurveyTemplateCommand;

public class SurveyTemplateBuilder {

    private CreateSurveyTemplateCommand command = new CreateSurveyTemplateCommand();

    public SurveyTemplateBuilder(String name) {
        command.setName(name);
    }

    public SurveyTemplateBuilder withDescription(String description) {
        command.setDescription(description);
        return this;
    }

    public SurveyTemplateBuilder withQuestions(QuestionBuilder... questions) {
        for (QuestionBuilder questionBuilder : questions) {
            command.getQuestions().add(questionBuilder.build());
        }
        return this;
    }

    public CreateSurveyTemplateCommand build() {
        return command;
    }

    public static SurveyTemplateBuilder template(String name) {
        return new SurveyTemplateBuilder(name);
    }
}
