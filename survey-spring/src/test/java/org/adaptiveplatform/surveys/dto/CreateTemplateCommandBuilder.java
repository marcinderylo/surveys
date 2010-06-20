package org.adaptiveplatform.surveys.dto;

import java.util.Arrays;
import java.util.List;

import org.adaptiveplatform.surveys.domain.QuestionTemplateBuilder;
import org.testng.collections.Lists;

/**
 *
 * @author Marcin Deryło
 */
public final class CreateTemplateCommandBuilder {

    private final String title;
    private List<QuestionTemplateBuilder> questions = Lists.newArrayList();
    private String description;

    private CreateTemplateCommandBuilder(String title) {
        this.title = title;
    }

    public CreateTemplateCommandBuilder withQuestions(
            QuestionTemplateBuilder... questions) {
        this.questions.addAll(Arrays.asList(questions));
        return this;
    }

    public CreateSurveyTemplateCommand build() {
        CreateSurveyTemplateCommand cmd =
                new CreateSurveyTemplateCommand();
        cmd.setName(title);
        cmd.setDescription(description);
        for (QuestionTemplateBuilder question : questions) {
            cmd.getQuestions().add(question.buildDto());
        }

        return cmd;
    }

    public static CreateTemplateCommandBuilder command(String title) {
        return new CreateTemplateCommandBuilder(title);
    }

    public CreateTemplateCommandBuilder withDescription(String string) {
        this.description = string;
        return this;
    }
}
