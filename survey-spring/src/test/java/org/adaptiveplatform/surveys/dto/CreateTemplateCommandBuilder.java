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
                for (QuestionTemplateBuilder question : questions) {
                        cmd.getQuestions().add(question.buildDto());
                }

                return cmd;
        }

        public static CreateTemplateCommandBuilder command(String title) {
                return new CreateTemplateCommandBuilder(title);
        }
}
