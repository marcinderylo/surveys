package org.adaptiveplatform.surveys.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.adaptiveplatform.adapt.commons.validation.constraints.NonBlank;
import org.adaptiveplatform.codegenerator.api.RemoteObject;

@RemoteObject
public class CreateSurveyTemplateCommand implements Serializable {

	private static final long serialVersionUID = 365832389182162306L;
	private String name;
	private String description;
	private List<QuestionTemplateDto> questions = new ArrayList<QuestionTemplateDto>();

	@NonBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<QuestionTemplateDto> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionTemplateDto> questions) {
		this.questions = questions;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
