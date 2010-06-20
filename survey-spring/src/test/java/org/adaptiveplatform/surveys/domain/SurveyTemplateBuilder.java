package org.adaptiveplatform.surveys.domain;

import java.util.ArrayList;
import java.util.List;

import org.adaptiveplatform.surveys.dto.UserDto;
import org.adaptiveplatform.surveys.test.UserAccountToDto;


public class SurveyTemplateBuilder {

	private String title;
	private final List<QuestionTemplate> questions = new ArrayList<QuestionTemplate>();
	private Boolean published = true;
	private UserDto creator;
    private String description;

	public SurveyTemplateBuilder(String title) {
		this.title = title;
	}

	public SurveyTemplate build() {
		SurveyTemplate surveyTemplate = new SurveyTemplate(creator, title);
        surveyTemplate.setDescription(description);
		for (QuestionTemplate question : questions) {
			surveyTemplate.addQuestion(question);
		}
		if (published) {
			surveyTemplate.publish();
		}
		return surveyTemplate;
	}

	public SurveyTemplateBuilder unpublished() {
		published = false;
		return this;
	}

	public SurveyTemplateBuilder withQuestions(QuestionTemplateBuilder... builders) {
		for (QuestionTemplateBuilder builder : builders) {
			questions.add(builder.build());
		}
		return this;
	}

	public SurveyTemplateBuilder byUser(UserAccount creator) {
		this.creator = UserAccountToDto.INSTANCE.apply(creator);
		return this;
	}

	public static SurveyTemplateBuilder template(String title) {
		return new SurveyTemplateBuilder(title);
	}

    public SurveyTemplateBuilder describedAs(String description) {
        this.description = description;
        return this;
    }
}
